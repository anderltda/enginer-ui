package br.com.enginer.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.enginer.domain.bre.port.BreOutboundPort;
import br.com.enginer.domain.comissao.port.ComissaoOutboundPort;
import br.com.enginer.domain.logger.port.LoggerOutboundPort;
import br.com.enginer.domain.rule.port.RuleInboundPort;
import br.com.enginer.domain.rule.usercase.RuleUserCase;
import br.com.enginer.infrastructure.tracking.TrackingProvider;

@Configuration
public class BeanConfiguration {

	@Bean
	TrackingProvider trackingProvider() {
		return TrackingProvider.getInstance();
	}

	@Bean
	WebClient webClient(WebClient.Builder builder) {
		return builder.build();
	}

	@Bean
	RuleInboundPort ruleInboundPort(LoggerOutboundPort logger, BreOutboundPort bre, ComissaoOutboundPort comissao) {
		return new RuleUserCase(logger, bre, comissao);
	}

}
