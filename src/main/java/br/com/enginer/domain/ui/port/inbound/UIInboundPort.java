package br.com.enginer.domain.ui.port.inbound;

import br.com.enginer.domain.InboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.Form;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

/**
 * 
 */
public interface UIInboundPort extends InboundPort {

	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Form form(Domain<?> domain) throws CheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Form filter(Domain<?> domain) throws CheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Form tab(Domain<?> domain) throws CheckedException;

	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Form row(Domain<?> domain) throws CheckedException;	

}
