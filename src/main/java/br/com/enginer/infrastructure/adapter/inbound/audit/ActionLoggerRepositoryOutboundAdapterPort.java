package br.com.enginer.infrastructure.adapter.inbound.audit;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.dto.entity.logger.ActionLogger;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.port.outbound.logger.ActionLoggerRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.infrastructure.adapter.outbound.repository.DelegatingRepositoryOutboundAdapter;

/**
 * Adapter outbound específico para o domínio {@link ActionLogger}.
 *
 * <p>
 * Esta classe apenas especializa o {@link DelegatingRepositoryOutboundAdapter}
 * para {@link ActionLogger}, delegando todas as operações ao repositório genérico
 * {@link RepositoryOutboundPort}<{@link Domain}<?>>.
 * </p>
 *
 * <p>
 * A existência deste adapter permite:
 * <ul>
 * <li>Evitar problemas de type erasure em runtime</li>
 * <li>Injeção limpa via Spring de {@link ActionLoggerRepositoryOutboundPort}</li>
 * <li>Uso seguro e tipado dentro dos UseCases</li>
 * </ul>
 * </p>
 */
@Component
public class ActionLoggerRepositoryOutboundAdapterPort extends DelegatingRepositoryOutboundAdapter<ActionLogger> implements ActionLoggerRepositoryOutboundPort {

	/**
	 * @param delegate
	 * @param loggerOutboundPort
	 * @param fileStorageOutboundPort
	 */
	protected ActionLoggerRepositoryOutboundAdapterPort(RepositoryOutboundPort<Domain<?>> delegate, LoggerOutboundPort loggerOutboundPort, FileStorageOutboundPort fileStorageOutboundPort) {
		super(delegate, loggerOutboundPort, fileStorageOutboundPort);
	}	

}