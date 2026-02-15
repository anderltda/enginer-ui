package br.com.enginer.infrastructure.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração principal de segurança (Spring Security)
 *
 * Cenário (Opção A):
 * - Backend é apenas Resource Server (valida JWT do Keycloak).
 * - Refresh token é feito DIRETO no Keycloak pelo frontend.
 * - Portanto, NÃO existem endpoints internos de /login ou /refresh no backend.
 *
 * Resultado:
 * - API stateless (sem sessão HTTP)
 * - CORS habilitado
 * - Preflight (OPTIONS) liberado
 * - Todo o resto exige Bearer token válido
 */
@Configuration
public class SecurityConfiguration {

	/**
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			/**
			 * CSRF: - Para APIs REST stateless (JWT no header Authorization), CSRF
			 * geralmente é desnecessário. - CSRF é mais relevante quando você usa
			 * sessão/cookies no browser.
			 */
			.csrf(csrf -> csrf.disable())
	
			/**
			 * CORS: - Habilita suporte a CORS dentro do Spring Security. - Importante: isso
			 * faz o Spring Security usar o bean CorsConfigurationSource() (ex.: o seu
			 * SecurityCorsConfiguration) para responder preflight e liberar origens.
			 */
			.cors(Customizer.withDefaults())

			/**
			 * Regras de autorização (quem pode acessar o quê).
			 */			
			.authorizeHttpRequests(auth -> auth

				/**
				 * Preflight do browser (CORS). - O Angular/browser dispara OPTIONS
				 * automaticamente em alguns cenários. - Se não liberar, você toma erro de CORS
				 * antes de chegar no controller.
				 */
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				/**
				 * Regras de autorização (quem pode acessar o quê).
				 */
				.anyRequest().authenticated()
			)

			/**
			 * Resource Server com JWT: - Configura o Spring para validar tokens JWT
			 * recebidos no header Authorization. - Vai usar as configs do application.yml
			 * em: spring.security.oauth2.resourceserver.jwt.issuer-uri
			 */
			.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

		return http.build();
	}
}