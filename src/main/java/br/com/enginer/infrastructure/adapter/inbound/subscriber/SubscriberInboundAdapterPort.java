package br.com.enginer.infrastructure.adapter.inbound.subscriber;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.enginer.domain.example.dto.entity.EntityOne;
import br.com.enginer.domain.ui.port.inbound.SubscriberInboundPort;
import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

/**
 * 
 */
@Component
public class SubscriberInboundAdapterPort {

	private final SubscriberInboundPort subscriberInboundPort;
	private final LoggerOutboundPort logger;
	private final ObjectMapper objectMapper;
	
	public SubscriberInboundAdapterPort(SubscriberInboundPort subscriberInboundPort, LoggerOutboundPort logger, ObjectMapper objectMapper) {
		this.subscriberInboundPort = subscriberInboundPort;
		this.logger = logger;
		this.objectMapper = objectMapper;
	}

	/**
	 * @param json
	 * @throws CheckedException
	 */
	public void subscriber(String json) throws CheckedException {
		logger.info(SubscriberInboundAdapterPort.class, json);
		Domain<?> domain = objectMapper.convertValue(json, EntityOne.class);
		subscriberInboundPort.consumer(domain);
	}
}