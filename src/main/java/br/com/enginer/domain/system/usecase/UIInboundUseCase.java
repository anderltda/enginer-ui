package br.com.enginer.domain.system.usecase;

import br.com.enginer.domain.system.usecase.annotation.instance.action.post.PostFilter;
import br.com.enginer.domain.system.usecase.annotation.instance.action.post.PostForm;
import br.com.enginer.domain.system.usecase.annotation.instance.action.post.PostRow;
import br.com.enginer.domain.system.usecase.annotation.instance.action.post.PostTab;
import br.com.enginer.domain.system.usecase.annotation.instance.action.pre.PreFilter;
import br.com.enginer.domain.system.usecase.annotation.instance.action.pre.PreForm;
import br.com.enginer.domain.system.usecase.annotation.instance.action.pre.PreRow;
import br.com.enginer.domain.system.usecase.annotation.instance.action.pre.PreTab;
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

	private final LoggerOutboundPort loggerOutboundPort;
    private final RepositoryOutboundPort<Domain<?>> repositoryOutboundPort;
    private final PublisherOutboundPort<Domain<?>> publisherOutboundPort;

	/**
	 * @param loggerOutboundPort
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 */
	public UIInboundUseCase(LoggerOutboundPort loggerOutboundPort, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort) {
		this.loggerOutboundPort = loggerOutboundPort;
		this.repositoryOutboundPort = repositoryOutboundPort;
		this.publisherOutboundPort = publisherOutboundPort;
	}

	/**
	 * @param domain
	 * @return
	 * @throws Exception
	 */
	private Object injectedDependency(Domain<?> domain) throws Exception {
		return ReflectionUtils.executeInjectedDependencyUseCaseCached(domain.getClass(), 
				loggerOutboundPort,
				repositoryOutboundPort, 
				publisherOutboundPort);
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
			loggerOutboundPort.error(UIInboundUseCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
	
	/**
	 *
	 */
	@Override
	public Form tab(Domain<?> domain) throws CheckedException {
		
		try {
		
			/** Injected Dependency */
			Object useCase = injectedDependency(domain);
			
        	/** Executa @PreTab */
        	ReflectionUtils.runAnnotatedMethods(useCase, PreTab.class, domain);
			
        	/** Executa o método real */
			Form form = (Form) ReflectionUtils.executeMethod(useCase, "tab", domain);
			
        	/** Executa @PostTab */
        	ReflectionUtils.runAnnotatedMethods(useCase, PostTab.class, form);
        	
			return form;
			
		} catch (Exception ex) {
			loggerOutboundPort.error(UIInboundUseCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */
	@Override
	public Form filter(Domain<?> domain) throws CheckedException {
		
		try {
		
			/** Injected Dependency */
			Object useCase = injectedDependency(domain);
			
        	/** Executa @PreFilter */
        	ReflectionUtils.runAnnotatedMethods(useCase, PreFilter.class, domain);
			
        	/** Executa o método real */
			Form form = (Form) ReflectionUtils.executeMethod(useCase, "filter", domain);
			
        	/** Executa @PostFilter */
        	ReflectionUtils.runAnnotatedMethods(useCase, PostFilter.class, form);
        	
			return form;
		
		} catch (Exception ex) {
			loggerOutboundPort.error(UIInboundUseCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}

	/**
	 *
	 */	
	@Override
	public Form row(Domain<?> domain) throws CheckedException {
		
		try {
		
			/** Injected Dependency */
			Object useCase = injectedDependency(domain);
			
        	/** Executa @PreRow */
        	ReflectionUtils.runAnnotatedMethods(useCase, PreRow.class, domain);
			
        	/** Executa o método real */
			Form form = (Form) ReflectionUtils.executeMethod(useCase, "row", domain);
			
        	/** Executa @PostRow */
        	ReflectionUtils.runAnnotatedMethods(useCase, PostRow.class, form);
        	
			return form;
		
		} catch (Exception ex) {
			loggerOutboundPort.error(UIInboundUseCase.class, ex.getMessage(), ex);
			throw new CheckedException(ex.getMessage(), ex);
		}
	}
}
