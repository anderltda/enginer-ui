package br.com.enginer.infrastructure.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração principal de segurança (Spring Security).
 *
 * Objetivo: - API stateless (sem sessão) - Autenticação via JWT (Keycloak) como
 * Resource Server - CORS habilitado usando o bean CorsConfigurationSource
 * (SecurityCorsConfiguration) - Libera endpoints públicos (login/refresh) e
 * exige token no restante
 */
@Configuration
public class SecurityConfiguration {

	/**
	 * Define a SecurityFilterChain (o pipeline de filtros do Spring Security).
	 *
	 * @param http builder principal do Spring Security para configurar filtros e
	 *             regras
	 * @return SecurityFilterChain pronto
	 * @throws Exception caso ocorra falha de configuração
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
				 * Sessão: - STATELESS garante que o Spring Security não cria nem usa sessão
				 * HTTP. - Cada request precisa vir com Authorization: Bearer <token>.
				 */
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				/**
				 * Regras de autorização (quem pode acessar o quê).
				 */
				.authorizeHttpRequests(auth -> auth

						/**
						 * Endpoint público de login. (No cenário "correto" com Keycloak-JS, esse
						 * endpoint tende a deixar de existir, mas enquanto existir, ele precisa ser
						 * liberado.)
						 */
						.requestMatchers(HttpMethod.POST, "/v1/enginer-ui/action/login").permitAll()

						/**
						 * Endpoint público de refresh. (Mesma observação: com Keycloak-JS, quem renova
						 * é o próprio frontend via Keycloak.)
						 */
						.requestMatchers(HttpMethod.POST, "/v1/enginer-ui/action/refresh").permitAll()

						/**
						 * Preflight do browser (CORS). - O Angular/browser dispara OPTIONS
						 * automaticamente em alguns cenários. - Se não liberar, você toma erro de CORS
						 * antes de chegar no controller.
						 */
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

						/**
						 * Qualquer outra rota exige autenticação (token JWT válido).
						 */
						.anyRequest().authenticated())

				/**
				 * Resource Server com JWT: - Configura o Spring para validar tokens JWT
				 * recebidos no header Authorization. - Vai usar as configs do application.yml
				 * em: spring.security.oauth2.resourceserver.jwt.issuer-uri
				 */
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

		return http.build();
	}
}