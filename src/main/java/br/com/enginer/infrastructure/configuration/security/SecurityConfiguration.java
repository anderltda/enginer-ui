package br.com.enginer.infrastructure.configuration.security;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SecurityConfiguration
 * -----------------------------------------------------------------------------
 * Configuração principal de segurança do backend (Spring Security).
 *
 * Cenário arquitetural:
 * - Backend atua como Resource Server (valida JWT emitido pelo Keycloak).
 * - Frontend (Angular) obtém e renova tokens diretamente no Keycloak.
 * - Não existe sessão HTTP no backend (stateless).
 *
 * Particularidade em DEV:
 * - O token pode vir com issuer diferente dependendo do ambiente:
 *     • Browser local → http://localhost:9090
 *     • Android Emulator → http://10.0.2.2:9090
 *
 * Para suportar isso, aceitamos múltiplos issuers válidos.
 */
@Configuration
public class SecurityConfiguration {

	/**
	 * URL do JWK Set do Keycloak.
	 *
	 * Exemplo no application.yml:
	 * spring.security.oauth2.resourceserver.jwt.jwk-set-uri:
	 *   http://localhost:9090/realms/enginer/protocol/openid-connect/certs
	 *
	 * O Spring usa isso para validar a assinatura do JWT.
	 */
	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
	private String jwkSetUri;

	/**
	 * Lista de issuers aceitos (separados por vírgula).
	 *
	 * Permite múltiplos emissores válidos em ambiente DEV.
	 *
	 * Exemplo:
	 * security.oauth2.accepted-issuers:
	 *   http://localhost:9090/realms/enginer,
	 *   http://10.0.2.2:9090/realms/enginer
	 *
	 * Se não definido, usa valor padrão (localhost + 10.0.2.2).
	 */
	@Value("${security.oauth2.accepted-issuers:http://localhost:9090/realms/enginer,http://10.0.2.2:9090/realms/enginer}")
	private String acceptedIssuers;

	/**
	 * Define o pipeline principal de filtros do Spring Security.
	 *
	 * Regras:
	 * - CSRF desabilitado (API REST stateless).
	 * - CORS habilitado (usa CorsConfigurationSource).
	 * - Sem sessão HTTP (STATELESS).
	 * - OPTIONS liberado (preflight).
	 * - Todas as outras rotas exigem JWT válido.
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			/**
			 * CSRF:
			 * Desnecessário para APIs stateless com Authorization: Bearer.
			 */
			.csrf(csrf -> csrf.disable())

			/**
			 * CORS:
			 * Usa a configuração definida em SecurityCorsConfiguration.
			 */
			.cors(Customizer.withDefaults())

			/**
			 * Session Management:
			 * Garante que o backend NÃO cria sessão HTTP.
			 */
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			/**
			 * Regras de autorização:
			 */
			.authorizeHttpRequests(auth -> auth

				/**
				 * Libera preflight (OPTIONS) para evitar erro de CORS.
				 */
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				/**
				 * Todo o restante exige autenticação via JWT.
				 */
				.anyRequest().authenticated()
			)

			/**
			 * Configura o backend como OAuth2 Resource Server com validação JWT.
			 */
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

		return http.build();
	}

	/**
	 * Bean customizado de JwtDecoder.
	 *
	 * Responsável por:
	 * 1) Validar assinatura do token via JWK Set.
	 * 2) Validar claims padrão (exp, nbf, etc).
	 * 3) Validar manualmente o "iss" (issuer) contra lista permitida.
	 *
	 * Isso é necessário porque em DEV aceitamos múltiplos issuers.
	 */
	@Bean
	JwtDecoder jwtDecoder() {

		/**
		 * Cria decoder baseado no endpoint JWK do Keycloak.
		 * Ele baixa as chaves públicas automaticamente.
		 */
		NimbusJwtDecoder decoder = NimbusJwtDecoder
			.withJwkSetUri(jwkSetUri)
			.build();

		/**
		 * Converte a string CSV de issuers em lista.
		 */
		List<String> issuers = Arrays.stream(acceptedIssuers.split(","))
			.map(String::trim)
			.filter(value -> !value.isBlank())
			.collect(Collectors.toList());

		/**
		 * Validador padrão do Spring:
		 * - exp (expiração)
		 * - nbf
		 * - iat
		 */
		OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();

		/**
		 * Validador customizado para o claim "iss".
		 *
		 * Garante que o issuer do token está dentro da lista permitida.
		 */
		OAuth2TokenValidator<Jwt> issuerValidator = jwt -> {

			String issuer = jwt.getIssuer() != null
				? jwt.getIssuer().toString()
				: "";

			if (issuers.contains(issuer)) {
				return OAuth2TokenValidatorResult.success();
			}

			OAuth2Error error = new OAuth2Error(
				"invalid_token",
				"The iss claim is not valid",
				null
			);

			return OAuth2TokenValidatorResult.failure(error);
		};

		/**
		 * Combina:
		 * - Validação padrão
		 * - Validação customizada de issuer
		 */
		decoder.setJwtValidator(
			new DelegatingOAuth2TokenValidator<>(
				defaultValidator,
				issuerValidator
			)
		);

		return decoder;
	}
}