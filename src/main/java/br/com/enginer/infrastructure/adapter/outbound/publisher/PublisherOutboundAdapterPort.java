package br.com.enginer.infrastructure.adapter.outbound.publisher;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.publisher.PublisherOutboundPort;

/**
 * 
 */
@Component
public class PublisherOutboundAdapterPort<T extends Domain<?>> implements PublisherOutboundPort<T> {

	@SuppressWarnings("unused")
	private final LoggerOutboundPort logger;
	
	@SuppressWarnings("unused")
	private final ObjectMapper objectMapper;

	public PublisherOutboundAdapterPort(LoggerOutboundPort logger, ObjectMapper objectMapper) {
		this.logger = logger;
		this.objectMapper = objectMapper;
	}

}
