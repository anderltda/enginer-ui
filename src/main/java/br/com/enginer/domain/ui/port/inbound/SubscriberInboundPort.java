package br.com.enginer.domain.ui.port.inbound;

import br.com.enginer.domain.InboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

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
