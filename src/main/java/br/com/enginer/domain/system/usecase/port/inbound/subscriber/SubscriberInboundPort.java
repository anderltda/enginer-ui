package br.com.enginer.domain.system.usecase.port.inbound.subscriber;

import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.port.inbound.InboundPort;

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
