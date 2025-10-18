package br.com.enginer.infrastructure.configuration;

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

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.instance.UIDomain;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainId;
import br.com.enginer.domain.ui.usercase.utils.ReflectionUtils;
import br.com.enginer.domain.ui.usercase.utils.StringsUtils;
import br.com.enginer.domain.ui.usercase.utils.UUIDGenerator;
import br.com.enginer.infrastructure.tracking.TrackingLogConfigurer;
import br.com.enginer.infrastructure.utils.PackageScannerUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Component
public class DomainResolver implements HandlerMethodArgumentResolver {

    private final TrackingLogConfigurer trackingLogConfigurer;

    public DomainResolver(TrackingLogConfigurer trackingLogConfigurer) {
        this.trackingLogConfigurer = trackingLogConfigurer;
    }

	/**
	 *
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UIDomain.class);
	}

	/**
	 *
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		
		trackingLogConfigurer.setInnerId(UUIDGenerator.generate());

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		String urlDomain = request.getHeader("X-UI-Url");
		String domainName = request.getHeader("X-UIDomain");
		String modal = request.getHeader("X-UIModal");
		String disabled = request.getHeader("X-UI-Mode");
		
		String id = extractIdFromUri(request.getRequestURI());

		if (domainName != null) {
			
			Boolean isModal = modal != null && !modal.isEmpty() ? Boolean.valueOf(modal) : Boolean.FALSE;
			Boolean isDisabled = disabled.equals(Constants.HASH1) ? disabled.equals(Constants.HASH1) : !disabled.equals(Constants.HASH2);
			
			Object object = PackageScannerUtils.findClassBySimpleName(Constants.PACKAGE_NAME_DOMAIN, StringsUtils.firstUpper(domainName));

			Class<?> clazz = object.getClass();

			if (clazz != null) {

				if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
					throw new IllegalArgumentException("Classe " + clazz.getName() + " não pode ser instanciada diretamente.");
				}

				Domain<?> domain = (Domain<?>) ReflectionUtils.newInstance(clazz);
				
				if (id != null && !id.isEmpty()) {
					
					if (domain instanceof DomainId) {
						
						ReflectionUtils.extractKeyCompositedByQueryParameter(id, domain);
						
					} else {
						
						ReflectionUtils.extractKeyByQueryParameter(id, clazz, domain);
					}
				}

				ReflectionUtils.executeMethod(domain, StringsUtils.setMethod("modal"), isModal);
				ReflectionUtils.executeMethod(domain, StringsUtils.setMethod("disabled"), isDisabled);
				extractMainDomainFromUri(domain, urlDomain);

				return domain;
			}
		}
		return null;
	}

	/**
	 * @param uri
	 * @return
	 */
	private String extractIdFromUri(String uri) {
		String[] parts = Arrays.stream(uri.split("/")).filter(s -> !s.isEmpty()).toArray(String[]::new);
		if (parts.length > 2) {
			String last = parts[parts.length - 1];
			String[] reserved = Constants.WORDS_RESERVED;
			for (String keyword : reserved) {
				if (keyword.equalsIgnoreCase(last)) {
					return null;
				}
			}
			if (last.matches("[a-zA-Z0-9\\-]+")) {
				return last;
			} else if(last.contains("id.")) {
				return last;
			}
		}
		return null;
	}
	
	/**
	 * Extrai o domínio principal de uma URI.
	 * @param uri A URI completa.
	 * @return O domínio principal extraído, ou null se não for possível extrair
	 * @throws Exception		
	 */
	private void extractMainDomainFromUri(Domain<?> domain, String uri) throws Exception {
		if(uri != null) {
			String mainDomain = thirdSegment(uri).orElse(null);
			ReflectionUtils.executeMethod(domain, StringsUtils.setMethod("mainDomain"), mainDomain);
		}
	}
	
	/**
	 * Retorna o terceiro segmento de uma URL ou path.
	 * Exemplo: para "http://example.com/one/two/three", retorna Optional com "three".
	 * Exemplo: para "/one/two/three", retorna Optional com "three".
	 * Exemplo: para "/one/two", retorna Optional vazio.
	 * 
	 * @param urlOrPath A URL completa ou apenas o path.
	 * @return Optional com o terceiro segmento, ou vazio se não existir.
	 */
	private Optional<String> thirdSegment(String urlOrPath) {
		String path;
		try {
			path = URI.create(urlOrPath).getPath();
		} catch (Exception e) {
			path = urlOrPath;
		}
		return Arrays.stream(path.split("/")).filter(s -> !s.isEmpty())
				.skip(2)
				.findFirst();
	}
}
