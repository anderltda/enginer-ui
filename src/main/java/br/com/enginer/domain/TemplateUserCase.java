package br.com.enginer.domain;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.ui.port.outbound.PublisherOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.Form;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

/**
 * 
 */
public interface TemplateUserCase {
	
	public static final String buscarFormPorId = "buscarFormPorId";
	public static final String buscarFormTodos = "buscarFormTodos";
	
	/**
	 * @param repositoryOutboundPort
	 */
	void setRepositoryOutboundPort(RepositoryOutboundPort repositoryOutboundPort);
	
	/**
	 * @param publisherOutboundPort
	 */
	void setPublisherOutboundPort(PublisherOutboundPort publisherOutboundPort);
	
	/**
	 * @return
	 */
	RepositoryOutboundPort getRepositoryOutboundPort();	
	
	/**
	 * @return
	 */
	PublisherOutboundPort getPublisherOutboundPort();	

	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	Form form(Domain<?> domain) throws UncheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	Form tab(Domain<?> domain) throws UncheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	Form filter(Domain<?> domain) throws UncheckedException;

	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	Form row(Domain<?> domain) throws UncheckedException;
	
	/**
	 * @param domain
	 * @return
	 * @throws CheckedException
	 */
	Domain<?> buscarFormPorId(Domain<?> domain) throws CheckedException;
	
	/**
	 * @param domain
	 * @param filter
	 * @return
	 * @throws UncheckedException
	 */
	List<Domain<?>> buscarFormTodos(Domain<?> domain, Map<String, Object> filter) throws UncheckedException;

}
