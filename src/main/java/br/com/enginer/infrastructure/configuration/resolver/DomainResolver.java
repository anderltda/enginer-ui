package br.com.enginer.infrastructure.configuration.resolver;

import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import br.com.enginer.domain.system.usecase.core.annotation.instance.UIDomain;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainId;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import br.com.enginer.infrastructure.configuration.scanner.PackageScannerCache;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class DomainResolver implements HandlerMethodArgumentResolver {
	
	private final PackageScannerCache scanner;
	
	/**
	 * @param scanner
	 */
	public DomainResolver(PackageScannerCache scanner) {
		this.scanner = scanner;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UIDomain.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		if (request == null) {
			throw new IllegalStateException("HttpServletRequest não disponível no contexto.");
		}

		String urlDomain = trimToNull(request.getHeader("X-UI-Url"));
		String domainName = trimToNull(request.getHeader("X-UIDomain"));
		String modalHdr = trimToNull(request.getHeader("X-UIModal"));
		String modeHdr = trimToNull(request.getHeader("X-UI-Mode"));

		if (domainName == null) {
			// Se @UIDomain está presente, eu prefiro falhar logo:
			// (se você realmente quer opcional, aí mantém return null)
			throw new IllegalArgumentException("Header X-UIDomain é obrigatório para resolver @UIDomain.");
		}

		boolean isModal = parseBoolean(modalHdr, false);
		boolean isDisabled = parseDisabled(modeHdr);

		String id = extractIdFromUri(request.getRequestURI());

		Object found = scanner.newInstance(domainName);
		
		if (found == null) {
			throw new IllegalArgumentException("Domínio não encontrado: " + domainName);
		}

		Class<?> clazz = found instanceof Class<?> c ? c : found.getClass();

		if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
			throw new IllegalArgumentException("Classe " + clazz.getName() + " não pode ser instanciada diretamente.");
		}

		Domain<?> domain = (Domain<?>) ReflectionUtils.newInstance(clazz);

		if (id != null) {
			if (domain instanceof DomainId) {
				ReflectionUtils.extractKeyCompositedByQueryParameter(id, domain);
			} else {
				ReflectionUtils.extractKeyByQueryParameter(id, clazz, domain);
			}
		}

		ReflectionUtils.execute(domain, StringsUtils.setMethod("modal"), isModal);
		ReflectionUtils.execute(domain, StringsUtils.setMethod("disabled"), isDisabled);

		if (urlDomain != null) {

			String mainDomain = thirdSegment(urlDomain).orElse(null);
			
			if(mainDomain != null) {
				ReflectionUtils.execute(domain, StringsUtils.setMethod("mainDomain"), mainDomain);
			}
		}

		return domain;
	}

	private static String trimToNull(String s) {
		if (s == null)
			return null;
		String t = s.trim();
		return t.isEmpty() ? null : t;
	}

	private static boolean parseBoolean(String value, boolean defaultValue) {
		return value == null ? defaultValue : Boolean.parseBoolean(value);
	}

	public static boolean parseDisabled(String modeHdr) {
		if (modeHdr == null)
			return false;
		if (modeHdr.equals(Constants.HASH1))
			return true; // ex: "disabled"
		if (modeHdr.equals(Constants.HASH2))
			return false; // ex: "enabled"
		// fallback (se vier qualquer outro valor, decide uma regra)
		return true;
	}

	private String extractIdFromUri(String uri) {
		String[] parts = Arrays.stream(uri.split("/")).filter(s -> !s.isBlank()).toArray(String[]::new);

		if (parts.length <= 2)
			return null;

		String last = parts[parts.length - 1];

		for (String keyword : Constants.WORDS_RESERVED) {
			if (keyword.equalsIgnoreCase(last))
				return null;
		}

		// Mais permissivo: aceita uuid, números, hífen, underscore, ponto, etc.
		if (last.matches("[a-zA-Z0-9._\\-]+"))
			return last;

		// compat com seu caso "id."
		if (last.contains("id."))
			return last;

		return null;
	}

	private Optional<String> thirdSegment(String urlOrPath) {
		String path;
		try {
			path = URI.create(urlOrPath).getPath();
		} catch (Exception e) {
			path = urlOrPath;
		}

		return Arrays.stream(path.split("/")).filter(s -> !s.isBlank()).skip(2).findFirst();
	}
}