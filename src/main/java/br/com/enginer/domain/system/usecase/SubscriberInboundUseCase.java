package br.com.enginer.domain.system.usecase;

import br.com.enginer.domain.system.usecase.exception.CheckedException;
import br.com.enginer.domain.system.usecase.logger.ActionLogger;
import br.com.enginer.domain.system.usecase.port.inbound.subscriber.SubscriberInboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.publisher.PublisherOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;

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
			logger.info(ActionInboundUseCase.class, "Action -> " + actionLogger.getActionName());
			Object object = injectedDependency(domain);
			domainNew =  (Domain<?>) ReflectionUtils.executeMethod(object, actionLogger.getActionName(), domain);
		} catch (Exception ex) {
			logger.error(ActionInboundUseCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}
		return domainNew;
	}
}
