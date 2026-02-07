package br.com.enginer.infrastructure.configuration.security;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuração global de CORS da aplicação.
 *
 * Essa classe define quais origens (frontends), métodos HTTP e headers podem
 * acessar a API.
 *
 * ✔ Usada em conjunto com Spring Security (http.cors()). ✔ Centraliza regras de
 * CORS fora dos controllers. ✔ Lê configurações do application.yml de forma
 * tipada.
 */
@Configuration
@EnableConfigurationProperties(SecurityCorsProperties.class)
public class SecurityCorsConfiguration {

	/**
	 * Propriedades de CORS carregadas do application.yml.
	 *
	 * Exemplo:
	 *
	 * security: cors: allowed-origins: - http://localhost:4200 -
	 * http://192.168.68.101:4200
	 */
	private final SecurityCorsProperties props;

	/**
	 * Injeção via construtor (boa prática).
	 *
	 * Garante que a aplicação só sobe se as propriedades de CORS estiverem
	 * corretamente configuradas.
	 */
	public SecurityCorsConfiguration(SecurityCorsProperties props) {
		this.props = props;
	}

	/**
	 * Bean responsável por fornecer a configuração de CORS para toda a aplicação.
	 *
	 * Esse bean é automaticamente utilizado pelo Spring Security quando você
	 * habilita:
	 *
	 * http.cors(cors -> {})
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {

		// Cria a configuração base de CORS
		CorsConfiguration cors = new CorsConfiguration();

		/**
		 * Define explicitamente quais ORIGENS (frontends) podem acessar a API.
		 *
		 * ⚠ Nunca use "*" em produção quando há autenticação. ✔ Aqui usamos lista vinda
		 * do application.yml.
		 */
		cors.setAllowedOrigins(props.getAllowedOrigins());

		/**
		 * Define os métodos HTTP permitidos.
		 *
		 * OPTIONS é obrigatório para o preflight do browser.
		 */
		cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

		/**
		 * Define quais headers o frontend pode enviar.
		 *
		 * "*" é aceitável aqui porque: ✔ a autenticação é feita por Bearer Token ✔ o
		 * backend valida tudo
		 */
		cors.setAllowedHeaders(List.of("*"));

		/**
		 * Define quais headers da RESPOSTA podem ser lidos pelo JavaScript no frontend.
		 *
		 * Por padrão, o browser bloqueia o acesso a headers.
		 */
		cors.setExposedHeaders(List.of("Authorization", "X-Request-Id"));

		/**
		 * Indica se cookies / credenciais devem ser enviados.
		 *
		 * ✔ false porque: - você usa Authorization: Bearer <token> - não usa cookies de
		 * sessão - não usa withCredentials no Angular
		 */
		cors.setAllowCredentials(false);

		/**
		 * Registra a configuração de CORS para TODAS as rotas (/**).
		 *
		 * Isso garante comportamento consistente em toda a API, inclusive endpoints
		 * protegidos pelo Spring Security.
		 */
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", cors);

		return source;
	}
}