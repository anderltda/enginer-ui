package br.com.enginer.infrastructure.adapter.inbound.audit;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.enginer.infrastructure.configuration.DomainResolver;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Provedor de contexto da requisição HTTP atual.
 *
 * Responsável por centralizar o acesso a informações do request,
 * como IP do cliente, identificador da requisição e o próprio
 * HttpServletRequest.
 *
 * Utilizado principalmente por:
 * - Auditoria
 * - Logging
 * - Aspects / Interceptors
 */
@Component
public class RequestContextProvider {

    /**
     * Obtém a requisição HTTP atual.
     */
    public Optional<HttpServletRequest> currentRequest() {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            return Optional.ofNullable(sra.getRequest());
        }
        return Optional.empty();
    }
    
	/**
	 * Recupera a fonte da requisição (MOBILE ou WEB).
	 */
	public Optional<String> source(HttpServletRequest request) {
		if (request == null)
			return Optional.empty();

		if (isMobile(request)) {
			return Optional.of("MOBILE");
		} 

		return Optional.of("WEB");
	}    

    /**
     * Recupera o IP do cliente.
     */
    public String ipAddress(HttpServletRequest request) {
        if (request == null) return null;

        String xff = request.getHeader("X-Forwarded-For");
        if (xff != null && !xff.isBlank()) {
            return xff.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    /**
     * Recupera o Request-Id da requisição.
     */
    public UUID requestId(HttpServletRequest request) {
        if (request == null) return UUID.randomUUID();

        String header = request.getHeader("X-Request-Id");
        if (header != null && !header.isBlank()) {
            try {
                return UUID.fromString(header.trim());
            } catch (Exception ignored) {
            }
        }
        return UUID.randomUUID();
    }

    /**
     * URL da tela atual no frontend (rota ou página).
     * Header: X-UI-Url
     */
    public String uiUrl(HttpServletRequest request) {
        return request != null ? request.getHeader("X-UI-Url") : null;
    }

    /**
     * Identifica se a ação ocorreu dentro de um modal.
     * Header: X-UIModal
     */
    public Boolean isModal(HttpServletRequest request) {
        if (request == null) return null;
        String modal = request.getHeader("X-UIModal");
        return modal != null ? Boolean.parseBoolean(modal) : null;
    }

    /**
     * Domínio da UI que originou a ação.
     * Header: X-UIDomain
     */
    public String uiDomain(HttpServletRequest request) {
        return request != null ? request.getHeader("X-UIDomain") : null;
    }

    /**
     * Indica se a requisição veio de um dispositivo mobile.
     * Header: X-Is-Mobile
     */
    public Boolean isMobile(HttpServletRequest request) {
        if (request == null) return null;

        String mobile = request.getHeader("X-Is-Mobile");
        return mobile != null ? Boolean.parseBoolean(mobile) : null;
    }

    /**
     * Modo da UI (ex: VIEW, EDIT, CREATE).
     * Header: X-UI-Mode
     */
    public String uiMode(HttpServletRequest request) {
        return request != null ? request.getHeader("X-UI-Mode") : null;
    }

    /**
     * Indica se a tela está em modo desabilitado,
     * derivado do modo da UI.
     */
    public Boolean isDisabled(HttpServletRequest request) {
        if (request == null) return null;

        String mode = request.getHeader("X-UI-Mode");
        return DomainResolver.parseDisabled(mode);
    }
}
