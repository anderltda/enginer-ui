package br.com.enginer.infrastructure.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 
 */
@Configuration
public class SecurityCorsConfiguration {

	/**
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration cors = new CorsConfiguration();
		// seu front
		cors.setAllowedOrigins(List.of("http://localhost:4200"
		// se você abrir o Angular pelo IP, adicione também:
		// "http://192.168.68.101:4200"
		));

		cors.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		cors.setAllowedHeaders(List.of("*"));

		// se você quiser ler headers customizados no front
		cors.setExposedHeaders(List.of("Authorization", "X-Request-Id"));

		// importante:
		// como você usa Bearer token (Authorization header), NÃO precisa cookies.
		cors.setAllowCredentials(false);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", cors);
		return source;
	}
}