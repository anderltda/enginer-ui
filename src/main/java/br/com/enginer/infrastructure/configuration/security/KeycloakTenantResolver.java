package br.com.enginer.infrastructure.configuration.security;

public final class KeycloakTenantResolver {

	private KeycloakTenantResolver() {
	}

	public static String fromIssuer(String issuer) {
		if (issuer == null || issuer.isBlank())
			return "default";
		// procura "/realms/<realm>"
		int idx = issuer.indexOf("/realms/");
		if (idx < 0)
			return issuer; // fallback
		String tail = issuer.substring(idx + "/realms/".length());
		// remove qualquer path extra
		int slash = tail.indexOf('/');
		return (slash >= 0) ? tail.substring(0, slash) : tail;
	}
}