package br.com.enginer.infrastructure.adapter.outbound.publisher;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;
import br.com.enginer.domain.ui.port.outbound.PublisherOutboundPort;

/**
 * 
 */
@Component
public class PublisherOutboundAdapterPort implements PublisherOutboundPort {

	@SuppressWarnings("unused")
	private final LoggerOutboundPort logger;
	
	@SuppressWarnings("unused")
	private final ObjectMapper objectMapper;

	public PublisherOutboundAdapterPort(LoggerOutboundPort logger, ObjectMapper objectMapper) {
		this.logger = logger;
		this.objectMapper = objectMapper;
	}

}
