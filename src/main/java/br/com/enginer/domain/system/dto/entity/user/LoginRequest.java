package br.com.enginer.domain.system.dto.entity.user;

/**
 * 
 */
public record LoginRequest(String username, String password, String provider, // "KEYCLOAK"
		String tenant // "dev"
) {
}
