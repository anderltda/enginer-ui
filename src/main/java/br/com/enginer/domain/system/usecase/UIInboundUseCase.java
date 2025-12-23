package br.com.enginer.domain.system.usecase;

import br.com.enginer.domain.system.usecase.annotation.instance.action.post.PostForm;
import br.com.enginer.domain.system.usecase.annotation.instance.action.pre.PreForm;
import br.com.enginer.domain.system.usecase.exception.CheckedException;
import br.com.enginer.domain.system.usecase.port.inbound.api.UIInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.schema.Form;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;

/**
 * 
 */
public class UIInboundUseCase<T extends Domain<?>> implements UIInboundPort<T> {

	private final LoggerOutboundPort logger;
    private final RepositoryOutboundPort<Domain<?>> repositoryOutboundPort;
    private final PublisherOutboundPort<Domain<?>> publisherOutboundPort;

	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 */
	public UIInboundUseCase(LoggerOutboundPort logger, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort) {
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
		return ReflectionUtils.executeInjectedDependencyUseCaseCached(domain.getClass(), repositoryOutboundPort, publisherOutboundPort);
	}

	/**
	 *
	 */
	@Override
	public Form form(Domain<?> domain) throws CheckedException {
		
		try {
		
			/** Injected Dependency */
			Object useCase = injectedDependency(domain);
			
        	/** Executa @PreForm */
        	ReflectionUtils.runAnnotatedMethods(useCase, PreForm.class, domain);
			
        	/** Executa o método real */
			Form form = (Form) ReflectionUtils.executeMethod(useCase, "form", domain);
			
        	/** Executa @PostForm */
        	ReflectionUtils.runAnnotatedMethods(useCase, PostForm.class, form);
			
			return form;
			
		} catch (Exception ex) {
			logger.error(UIInboundUseCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 *
	 */
	@Override
	public Form tab(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUseCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUseCase, "tab", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUseCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Form filter(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUseCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUseCase, "filter", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUseCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */	
	@Override
	public Form row(Domain<?> domain) throws CheckedException {
		try {
			Object newInstanceUseCase = injectedDependency(domain);
			Form form = (Form) ReflectionUtils.executeMethod(newInstanceUseCase, "row", domain);
			return form;
		} catch (Exception ex) {
			logger.error(UIInboundUseCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
}
