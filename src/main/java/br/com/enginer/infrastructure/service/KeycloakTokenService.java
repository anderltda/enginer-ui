package br.com.enginer.infrastructure.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Serviço responsável por trocar credenciais pelo token no Keycloak. Usa
 * grant_type=password e grant_type=refresh_token.
 */
@Service
public class KeycloakTokenService {

	private final WebClient webClient;

	@Value("${security.keycloak.base-url}")
	private String baseUrl; // ex: http://localhost:9090

	@Value("${security.keycloak.realm}")
	private String realm; // ex: enginer

	@Value("${security.keycloak.client-id}")
	private String clientId; // ex: enginer-web

	// Se seu client for Confidential, configure:
	@Value("${security.keycloak.client-secret:}")
	private String clientSecret;

	public KeycloakTokenService(WebClient.Builder builder) {
		this.webClient = builder.codecs(c -> c.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)).build();
	}

	public record TokenResponse(String access_token, Integer expires_in, Integer refresh_expires_in, String refresh_token, String token_type, String scope) {
		
		public String accessToken() {
			return access_token;
		}

		public Integer expiresIn() {
			return expires_in;
		}

		public String refreshToken() {
			return refresh_token;
		}

		public String tokenType() {
			return token_type;
		}
	}

	/**
	 * grant_type=password
	 */
	public TokenResponse passwordGrant(String username, String password) {
		String tokenUrl = tokenUrl();

		var form = new org.springframework.util.LinkedMultiValueMap<String, String>();
		form.add("client_id", clientId);
		form.add("grant_type", "password");
		form.add("username", username);
		form.add("password", password);

		// confidential client
		if (clientSecret != null && !clientSecret.isBlank()) {
			form.add("client_secret", clientSecret);
		}

		return webClient.post().uri(tokenUrl).contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue(form)
				.retrieve().bodyToMono(TokenResponse.class).block(Duration.ofSeconds(10));
	}

	/**
	 * grant_type=refresh_token
	 */
	public TokenResponse refreshGrant(String refreshToken) {
		String tokenUrl = tokenUrl();

		var form = new org.springframework.util.LinkedMultiValueMap<String, String>();
		form.add("client_id", clientId);
		form.add("grant_type", "refresh_token");
		form.add("refresh_token", refreshToken);

		// confidential client
		if (clientSecret != null && !clientSecret.isBlank()) {
			form.add("client_secret", clientSecret);
		}

		return webClient.post().uri(tokenUrl).contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue(form)
				.retrieve().bodyToMono(TokenResponse.class).block(Duration.ofSeconds(10));
	}

	private String tokenUrl() {
		// Keycloak token endpoint
		return baseUrl + "/realms/" + realm + "/protocol/openid-connect/token";
	}
}