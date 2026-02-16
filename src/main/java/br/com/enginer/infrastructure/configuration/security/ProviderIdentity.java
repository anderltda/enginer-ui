package br.com.enginer.infrastructure.configuration.security;

import org.springframework.security.oauth2.jwt.Jwt;

import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;

/**
 * 
 */
public record ProviderIdentity(String provider, String providerTenant, String providerSubject, String email, String username, String firstName, String lastName) {

	/**
	 * Cria identidade a partir de um JWT (OAuth2 / OIDC).
	 *
	 * @param provider nome do provider (KEYCLOAK, COGNITO, AUTH0, ...)
	 * @param jwt      token autenticado
	 */
	public static ProviderIdentity fromJwt(String provider, Jwt jwt) {

		if (provider == null || provider.isBlank()) {
			throw new IllegalArgumentException("provider é obrigatório");
		}
		if (jwt == null) {
			throw new IllegalArgumentException("jwt é obrigatório");
		}

		String providerSubject = jwt.getSubject(); // sub

		if (providerSubject == null || providerSubject.isBlank()) {
			throw new IllegalStateException("JWT sem claim 'sub'");
		}
		
		String providerTenant = extractTenant(jwt);
		String email = jwt.getClaimAsString("email");
		String username = StringsUtils.firstNonBlank(jwt.getClaimAsString("preferred_username"), jwt.getClaimAsString("username"));
		String firstName = jwt.getClaimAsString("given_name");
		String lastName = jwt.getClaimAsString("family_name");

		return new ProviderIdentity(provider, providerTenant, providerSubject, email, username, firstName, lastName);
	}

	/**
	 * Extrai tenant a partir do issuer (ex: realm do Keycloak). Pode ser
	 * sobrescrito no futuro para outros providers.
	 */
	private static String extractTenant(Jwt jwt) {
		
		if (jwt.getIssuer() == null) {
			return null;
		}

		String issuer = jwt.getIssuer().toString();

		// ex: http://localhost:9090/realms/enginer -> enginer
		int idx = issuer.lastIndexOf('/');
		return idx >= 0 ? issuer.substring(idx + 1) : issuer;
	}
}