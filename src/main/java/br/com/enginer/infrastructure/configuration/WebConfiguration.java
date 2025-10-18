package br.com.enginer.infrastructure.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	private final DomainResolver domainResolver;

	public WebConfiguration(DomainResolver domainResolver) {
		this.domainResolver = domainResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(domainResolver);
	}
	
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIdConverter());
    }

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Define as rotas que serão liberadas
				.allowedOrigins("http://localhost:4200") // Define a origem permitida (ex: Angular rodando localmente)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Define os métodos HTTP permitidos
				.allowedHeaders("*") // Define os cabeçalhos permitidos
				.allowCredentials(true); // Permite envio de credenciais (cookies, autenticação, etc.)
	}
}
