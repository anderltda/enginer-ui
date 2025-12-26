package br.com.enginer.domain.system.usecase.core.subscriber;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.usecase.core.action.ActionInboundUseCase;
import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.port.inbound.subscriber.SubscriberInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;

/**
 * 
 */
public class SubscriberInboundUseCase<T extends Domain<?>> implements SubscriberInboundPort<T> {

	private final LoggerOutboundPort logger;
    private final RepositoryOutboundPort<Domain<?>> repositoryOutboundPort;
    private final PublisherOutboundPort<Domain<?>> publisherOutboundPort;

	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 */
	public SubscriberInboundUseCase(LoggerOutboundPort logger, RepositoryOutboundPort<Domain<?>> repositoryOutboundPort, PublisherOutboundPort<Domain<?>> publisherOutboundPort) {
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
		logger.info(SubscriberInboundUseCase.class, "Injected Dependency");
		return ReflectionUtils.executeInjectedDependencyUseCaseCached(domain.getClass(), repositoryOutboundPort, publisherOutboundPort);
	}

	/**
	 *
	 */
	@Override
	public Domain<?> consumer(Domain<?> domain) throws CheckedException {
		
		Domain<?> domainNew = null;
		
		try {
			
			ActionLogger actionLogger = domain.getActionLogger();
			
			logger.info(ActionInboundUseCase.class, "Action -> " + actionLogger.getAction());
			
			Object object = injectedDependency(domain);
			
			domainNew =  (Domain<?>) ReflectionUtils.executeMethod(object, actionLogger.getAction(), domain);
			
		} catch (Exception ex) {
			logger.error(ActionInboundUseCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}
		
		return domainNew;
	}
}
