package br.com.enginer.infrastructure.configuration.resolver;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UIDomain;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainId;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import br.com.enginer.infrastructure.configuration.cache.UserAccountCache;
import br.com.enginer.infrastructure.configuration.scanner.PackageScannerCache;
import br.com.enginer.infrastructure.configuration.security.ProviderIdentity;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolver do @UIDomain:
 * - Instancia o Domain a partir do header X-UIDomain (PackageScannerCache)
 * - Extrai o id do path (simples ou composto)
 * - Aplica flags da UI (modal/disabled/mainDomain)
 * - Injeta credenciais (ActionLogger.userAccount) via cache TTL global
 */
@Component
public class DomainResolver implements HandlerMethodArgumentResolver {

    /**
     * Segmentos finais que NÃO representam ID (rotas do seu gateway UI).
     */
    private static final Set<String> RESERVED_LAST_SEGMENTS = Set.of(
            "tab", "row", "form", "filter", "module", "action",
            "search", "calendar", "autocomplete", "research"
    );

    private final PackageScannerCache scanner;
    private final UserAccountCache userAccountCache;

    /**
     * @param scanner cache de classes por simpleName
     * @param userAccountCache cache TTL do UserAccount (provider|tenant|subject)
     */
    public DomainResolver(PackageScannerCache scanner, UserAccountCache userAccountCache) {
        this.scanner = scanner;
        this.userAccountCache = userAccountCache;
    }

    /**
     * Este resolver atende somente parâmetros anotados com @UIDomain.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UIDomain.class);
    }

    /**
     * Resolve o Domain:
     * - Header X-UIDomain define qual classe instanciar
     * - Último segmento do path pode ser id
     * - Headers complementam o estado (modal/disabled/mainDomain)
     * - Injeta userAccount e createdBy/updatedBy automaticamente
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        
        if (request == null) {
            throw new IllegalStateException("HttpServletRequest não disponível no contexto.");
        }

        // Headers da UI
        String urlDomain  = StringsUtils.trimToNull(request.getHeader("X-UI-Url"));
        String domainName = StringsUtils.trimToNull(request.getHeader("X-UIDomain"));
        String modalHdr   = StringsUtils.trimToNull(request.getHeader("X-UIModal"));
        String modeHdr    = StringsUtils.trimToNull(request.getHeader("X-UI-Mode"));

        // X-UIDomain é obrigatório para saber qual classe instanciar
        if (domainName == null) {
            throw new IllegalArgumentException("Header X-UIDomain é obrigatório para resolver @UIDomain.");
        }

        boolean isModal    = StringsUtils.parseBoolean(modalHdr, false);
        boolean isDisabled = parseDisabled(modeHdr);

        // id é o último segmento quando não é rota reservada
        String id = extractIdFromUri(request.getRequestURI());

        // Cria instância do domínio via scanner/cache
        Object found = scanner.newInstance(domainName);
        
        if (found == null) {
            throw new IllegalArgumentException("Domínio não encontrado: " + domainName);
        }

        Class<?> clazz = found instanceof Class<?> c ? c : found.getClass();
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new IllegalArgumentException("Classe " + clazz.getName() + " não pode ser instanciada diretamente.");
        }

        Domain<?> domain = (Domain<?>) ReflectionUtils.newInstance(clazz);

        // Preenche id a partir do path (simples ou composto)
        if (id != null) {
            if (domain instanceof DomainId) {
                ReflectionUtils.extractKeyCompositedByQueryParameter(id, domain);
            } else {
                ReflectionUtils.extractKeyByQueryParameter(id, clazz, domain);
            }
        }

        // Flags da UI no domínio
        ReflectionUtils.execute(domain, StringsUtils.setMethod("modal"), isModal);
        ReflectionUtils.execute(domain, StringsUtils.setMethod("disabled"), isDisabled);

        // mainDomain a partir de X-UI-Url
        if (urlDomain != null) {
            String mainDomain = StringsUtils.thirdSegment(urlDomain).orElse(null);
            if (mainDomain != null) {
                ReflectionUtils.execute(domain, StringsUtils.setMethod("mainDomain"), mainDomain);
            }
        }
        
        if (isFilterTemplate(request)) {
            domain.setCreatedById(null);
            domain.setUpdatedById(null);
            domain.setActionLogger(null);
            return domain;
        }

        // Injeta UserAccount e metadados de auditoria
        injectUserAccount(domain);

        return domain;
    }

    /**
     * Injeta credenciais no domínio:
     * - Resolve o Jwt do SecurityContext
     * - Converte para ProviderIdentity
     * - Carrega UserAccount via cache TTL
     * - Preenche ActionLogger.userAccount
     * - Decide create/update com base no id do Domain
     *
     * Obs: aqui usamos os setters do próprio Domain (createdBy/updatedBy)
     * para não depender de reflection em métodos de ActionLogger.
     */
    private void injectUserAccount(Domain<?> domain) {

        try {

            Jwt jwt = resolveJwt();
            if (jwt == null) return; // endpoint sem auth

            ProviderIdentity identity = ProviderIdentity.fromJwt("KEYCLOAK", jwt);

            UserAccount user = userAccountCache.getOrLoad(identity);
            if (user == null) return;

            // Garante ActionLogger dentro do domain
            ActionLogger al = domain.getActionLogger();
            if (al == null) {
                al = new ActionLogger();
                domain.setActionLogger(al);
            }
            al.setUserAccount(user);

            // CREATE: seta createdBy e limpa updatedBy
            // UPDATE: seta updatedBy e limpa createdBy
            if (domain.isIdNull()) {
                domain.setCreatedById(user.getId());
                domain.setUpdatedById(null);
            } else {
                domain.setUpdatedById(user.getId());
                domain.setCreatedById(null);
            }

        } catch (Exception ignored) {
            // Não derruba o endpoint se falhar a injeção de credenciais
        }
    }

    /**
     * Resolve Jwt do SecurityContext (Resource Server padrão).
     */
    private Jwt resolveJwt() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;

        Object principal = auth.getPrincipal();
        if (principal instanceof Jwt jwt) return jwt;

        Object credentials = auth.getCredentials();
        if (credentials instanceof Jwt jwt) return jwt;

        return null;
    }

    /**
     * Interpreta o header X-UI-Mode (hashes do seu projeto) em "disabled".
     */
    public static boolean parseDisabled(String modeHdr) {
        if (modeHdr == null) return false;
        if (modeHdr.equals(Constants.HASH1)) return true;  // "disabled"
        if (modeHdr.equals(Constants.HASH2)) return false; // "enabled"
        return true;
    }

    /**
     * Extrai o ID do path:
     * - ignora rotas reservadas (tab/row/form/...)
     * - ignora keywords do seu projeto
     * - aceita uuid, long, string, e o padrão "id."
     */
    private String extractIdFromUri(String uri) {

        String[] parts = Arrays.stream(uri.split("/"))
                .filter(s -> !s.isBlank())
                .toArray(String[]::new);

        if (parts.length == 0) return null;

        String last = parts[parts.length - 1];

        if (RESERVED_LAST_SEGMENTS.contains(last.toLowerCase())) return null;

        for (String keyword : Constants.WORDS_RESERVED) {
            if (keyword.equalsIgnoreCase(last)) return null;
        }

        if (last.contains("id.")) return last;
        if (last.matches("[a-zA-Z0-9._\\-]+")) return last;

        return null;
    }
    
    /**
     * @param request
     * @return
     */
    private boolean isFilterTemplate(HttpServletRequest request) {
        if (request == null) return false;

        String uri = request.getRequestURI();
        if (uri != null && uri.contains("/module/filter")) {
            return true;
        }

        // fallback opcional: se sua UI manda X-UI-Url apontando o template
        String uiUrl = StringsUtils.trimToNull(request.getHeader("X-UI-Url"));
        return uiUrl != null && uiUrl.contains("/filter");
    }
}