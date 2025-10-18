package br.com.enginer.domain.ui.usercase;

import br.com.enginer.domain.ui.dto.logger.ActionLogger;
import br.com.enginer.domain.ui.port.inbound.SubscriberInboundPort;
import br.com.enginer.domain.ui.port.outbound.LoggerOutboundPort;
import br.com.enginer.domain.ui.port.outbound.PublisherOutboundPort;
import br.com.enginer.domain.ui.port.outbound.RepositoryOutboundPort;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;
import br.com.enginer.domain.ui.usercase.utils.ReflectionUtils;

/**
 * 
 */
public class SubscriberInboundUserCase implements SubscriberInboundPort {

	private final LoggerOutboundPort logger;
	private final RepositoryOutboundPort repositoryOutboundPort;
	private final PublisherOutboundPort publisherOutboundPort;

	/**
	 * @param logger
	 * @param repositoryOutboundPort
	 * @param publisherOutboundPort
	 */
	public SubscriberInboundUserCase(LoggerOutboundPort logger, RepositoryOutboundPort repositoryOutboundPort, PublisherOutboundPort publisherOutboundPort) {
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
		logger.info(SubscriberInboundUserCase.class, "Injected Dependency");
		return ReflectionUtils.executeInjectedDependencyUserCase(domain.getClass(), repositoryOutboundPort, publisherOutboundPort);
	}

	/**
	 *
	 */
	@Override
	public Domain<?> consumer(Domain<?> domain) throws CheckedException {
		Domain<?> domainNew = null;
		try {
			ActionLogger actionLogger = domain.getActionLogger();
			logger.info(ActionInboundUserCase.class, "Action -> " + actionLogger.getActionName());
			Object object = injectedDependency(domain);
			domainNew =  (Domain<?>) ReflectionUtils.executeMethod(object, actionLogger.getActionName(), domain);
		} catch (Exception ex) {
			logger.error(ActionInboundUserCase.class, ex);
			throw new CheckedException(ex.getCause().getMessage(), ex.getCause());
		}
		return domainNew;
	}
}
