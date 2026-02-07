package br.com.enginer.infrastructure.configuration.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Classe responsável por mapear as propriedades de CORS
 * definidas no application.yml para um objeto Java tipado.
 *
 * Exemplo de configuração esperada:
 *
 * security:
 *   cors:
 *     allowed-origins:
 *       - http://localhost:4200
 *       - http://192.168.68.101:4200
 *
 * ✔ Evita uso de @Value espalhado pelo código
 * ✔ Facilita manutenção e validação
 * ✔ Permite múltiplas origens
 */
@ConfigurationProperties(prefix = "security.cors")
public class SecurityCorsProperties {

	/**
	 * Lista de origens (frontends) autorizadas a acessar a API.
	 *
	 * Cada item representa o valor exato do header:
	 *   Origin: <valor>
	 *
	 * ⚠ Deve bater exatamente com a origem do browser
	 * (protocolo + host + porta).
	 */
	private List<String> allowedOrigins = new ArrayList<>();

	/**
	 * Retorna a lista de origens permitidas.
	 *
	 * Usado pela SecurityCorsConfiguration para
	 * configurar o CorsConfiguration do Spring.
	 */
	public List<String> getAllowedOrigins() {
		return allowedOrigins;
	}

	/**
	 * Define a lista de origens permitidas.
	 *
	 * Esse método é chamado automaticamente pelo Spring
	 * durante o bootstrap da aplicação, com base no
	 * application.yml.
	 */
	public void setAllowedOrigins(List<String> allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}
}