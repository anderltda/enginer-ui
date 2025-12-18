package br.com.enginer.domain.system.usecase.port.inbound.subscriber;

import br.com.enginer.domain.system.usecase.exception.CheckedException;
import br.com.enginer.domain.system.usecase.port.inbound.InboundPort;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;

/**
 * 
 */
public interface SubscriberInboundPort<T extends Domain<?>> extends InboundPort {
	
	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Domain<?> consumer(Domain<?> domain) throws CheckedException;

}
