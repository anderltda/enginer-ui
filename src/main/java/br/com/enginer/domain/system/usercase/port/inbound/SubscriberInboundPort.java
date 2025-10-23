package br.com.enginer.domain.system.usercase.port.inbound;

import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.port.InboundPort;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;

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
