package br.com.enginer.infrastructure.configuration.security;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * SecurityCorsConfiguration
 * -----------------------------------------------------------------------------
 * Configuração global de CORS da aplicação.
 *
 * Objetivo:
 * - Definir quais origens (frontends), métodos e headers podem acessar a API.
 * - Ser utilizada pelo Spring Security via http.cors(Customizer.withDefaults()).
 *
 * Importante:
 * - Para evitar problemas em DEV (porta/host variando, WebView, emulador, etc.),
 *   usamos allowedOriginPatterns() ao invés de allowedOrigins().
 *
 * Por que?
 * - allowedOrigins exige match EXATO (protocolo + host + porta).
 * - allowedOriginPatterns permite padrões tipo: "http://localhost:*".
 *
 * Segurança:
 * - NÃO use "*" em produção.
 * - Em produção, liste as origens reais (ex.: https://app.seudominio.com).
 */
@Configuration
@EnableConfigurationProperties(SecurityCorsProperties.class)
public class SecurityCorsConfiguration {

	/**
	 * Propriedades de CORS carregadas do application.yml.
	 *
	 * Exemplo:
	 *
	 * security:
	 *   cors:
	 *     allowed-origins:
	 *       - "http://localhost:*"
	 *       - "http://192.168.68.101:*"
	 */
	private final SecurityCorsProperties props;

	public SecurityCorsConfiguration(SecurityCorsProperties props) {
		this.props = props;
	}

	/**
	 * Bean responsável por fornecer a configuração CORS para toda a aplicação.
	 *
	 * Esse bean é automaticamente utilizado pelo Spring Security quando você habilita:
	 *   http.cors(Customizer.withDefaults())
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		// Configuração base de CORS
		CorsConfiguration cors = new CorsConfiguration();

		/**
		 * Origem (Origin) permitida.
		 *
		 * Usamos allowedOriginPatterns para suportar padrões (DEV / WebView / porta variável).
		 * Exemplos válidos:
		 * - http://localhost:*
		 * - http://127.0.0.1:*
		 * - http://192.168.68.101:*
		 *
		 * ⚠ Em produção, prefira origens explícitas e sem wildcard.
		 */
		cors.setAllowedOriginPatterns(props.getAllowedOrigins());

		/**
		 * Métodos HTTP permitidos.
		 *
		 * OPTIONS é obrigatório para o preflight do browser.
		 */
		cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

		/**
		 * Headers que o frontend pode ENVIAR.
		 *
		 * "*" é aceitável aqui porque:
		 * - autenticação é Bearer token
		 * - o backend valida tudo (JWT + regras)
		 */
		cors.setAllowedHeaders(List.of("*"));

		/**
		 * Headers da RESPOSTA que o JavaScript pode LER.
		 *
		 * Por padrão, o browser restringe acesso a alguns headers.
		 */
		cors.setExposedHeaders(List.of("Authorization", "X-Request-Id"));

		/**
		 * Envio de cookies/credenciais (Authorization via cookie / withCredentials).
		 *
		 * false porque:
		 * - você usa "Authorization: Bearer <token>"
		 * - não usa cookies de sessão
		 * - não precisa de withCredentials no Angular
		 */
		cors.setAllowCredentials(false);

		/**
		 * Cache do preflight (em segundos).
		 *
		 * Ajuda performance: o browser não precisa refazer OPTIONS toda hora.
		 * (1 hora = 3600s)
		 */
		cors.setMaxAge(3600L);

		/**
		 * Registra essa configuração para todas as rotas (/**).
		 */
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cors);

		return source;
	}
}