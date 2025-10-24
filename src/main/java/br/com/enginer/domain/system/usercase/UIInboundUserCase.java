package br.com.enginer.domain.system.usercase;

import br.com.enginer.domain.system.usercase.exception.CheckedException;
import br.com.enginer.domain.system.usercase.port.inbound.api.UIInboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usercase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usercase.schema.Form;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;
import br.com.enginer.domain.system.usercase.utils.ReflectionUtils;

/**
 * 
 */
public class UIInboundUserCase<T extends Domain<?>> implements UIInboundPort<T> {

	private final LoggerOutboundPort logger;
    private final RepositoryOutboundPort<Domain<?>> repositoryOutboundPort;
    private final PublisherOutboundPort<Domain<?>> publisherOutboundPort;

	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 */
	public UIInboundUserCase(LoggerOutboundPort logger, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort) {
		this.logger = logger;
		this.repositoryOutboundPort = repositoryOutboundPort;
		this.publisherOutboundPort = publisherOutboundPort;
	}

	/**
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	private Object injectedDependency(Domain<?> domain) throws Exception {
		return ReflectionUtils.executeInjectedDependencyUserCaseCached(domain.getClass(), repositoryOutboundPort, publisherOutboundPort);
	}

	/**
	 *
	 */
	@Override
	public Form form(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUserCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUserCase, "form", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUserCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 *
	 */
	@Override
	public Form tab(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUserCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUserCase, "tab", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUserCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Form filter(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUserCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUserCase, "filter", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUserCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */	
	@Override
	public Form row(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUserCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUserCase, "row", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUserCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
}
