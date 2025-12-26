package br.com.enginer.infrastructure.adapter.inbound.subscriber;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.enginer.domain.example.dto.entity.EntityOne;
import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.port.inbound.subscriber.SubscriberInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;

/**
 * 
 */
@Component
public class SubscriberInboundAdapterPort {

	private final SubscriberInboundPort<Domain<?>> subscriberInboundPort;
	private final LoggerOutboundPort logger;
	private final ObjectMapper objectMapper;
	
	public SubscriberInboundAdapterPort(SubscriberInboundPort<Domain<?>> subscriberInboundPort, LoggerOutboundPort logger, ObjectMapper objectMapper) {
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