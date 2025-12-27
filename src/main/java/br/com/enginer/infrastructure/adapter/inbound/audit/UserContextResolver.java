package br.com.enginer.infrastructure.adapter.inbound.audit;

import java.util.Optional;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolver de informações do usuário a partir da requisição HTTP.
 *
 * Responsável por extrair dados de identificação do usuário enviados pelo
 * frontend através de headers.
 *
 * Utilizado principalmente em: - Auditoria - Logging - Aspects
 *
 * Observação: Este resolver NÃO depende de Spring Security. Os dados são
 * considerados informativos (telemetria), principalmente para ambientes de
 * teste.
 */
@Component
public class UserContextResolver {

	/**
	 * Recupera o nome do usuário. Header esperado: X-Username
	 *
	 * @param request requisição HTTP
	 * @return username ou Optional.empty()
	 */
	public Optional<String> username(HttpServletRequest request) {
		if (request == null)
			return Optional.empty();

		String v = request.getHeader("X-Username");
		if (v == null || v.isBlank())
			return Optional.empty();

		return Optional.of(v.trim());
	}

	/**
	 * Recupera o identificador do usuário. Header esperado: X-User-Id
	 *
	 * @param request requisição HTTP
	 * @return userId ou Optional.empty()
	 */
	public Optional<String> userId(HttpServletRequest request) {
		if (request == null)
			return Optional.empty();

		String v = request.getHeader("X-User-Id");
		if (v == null || v.isBlank())
			return Optional.empty();

		return Optional.of(v.trim());
	}
}
