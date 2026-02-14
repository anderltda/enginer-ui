package br.com.enginer.infrastructure.adapter.outbound.repository;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.fn.SerializableConsumer;
import br.com.enginer.domain.system.usecase.core.fn.SerializableLambda;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainId;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.UserAccountRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;

/**
 * Adapter outbound específico para o domínio {@link UserAccount}.
 *
 * <p>
 * Esta classe apenas especializa o {@link DelegatingRepositoryOutboundAdapter}
 * para {@link UserAccount}, delegando todas as operações ao repositório
 * genérico {@link RepositoryOutboundPort}<{@link Domain}<?>>.
 * </p>
 *
 * <p>
 * A existência deste adapter permite:
 * <ul>
 * <li>Evitar problemas de type erasure em runtime</li>
 * <li>Injeção limpa via Spring de
 * {@link UserAccountRepositoryOutboundPort}</li>
 * <li>Uso seguro e tipado dentro dos UseCases</li>
 * </ul>
 * </p>
 */
@Component
public class UserAccountRepositoryOutboundAdapterPort extends DelegatingRepositoryOutboundAdapter<UserAccount> implements UserAccountRepositoryOutboundPort {

	/**
	 * @param delegate
	 * @param loggerOutboundPort
	 * @param fileStorageOutboundPort
	 */
	protected UserAccountRepositoryOutboundAdapterPort(RepositoryOutboundPort<Domain<?>> delegate, LoggerOutboundPort loggerOutboundPort, FileStorageOutboundPort fileStorageOutboundPort) {
		super(delegate, loggerOutboundPort, fileStorageOutboundPort);
	}

	/**
	 *
	 */
	@Override
	public void pull(Domain<?> domain) {

		try {

			if (domain == null || domain instanceof UserAccount || domain.getActionLogger() == null
					|| domain.getActionLogger().getUserAccount() == null || domain.getActionLogger().getUserAccount().getId() == null)
				return;

			String setCreatedById = SerializableLambda.extractMethodName((SerializableConsumer<Long>) domain.getActionLogger()::setCreatedById);
			String setUpdatedById = SerializableLambda.extractMethodName((SerializableConsumer<Long>) domain.getActionLogger()::setUpdatedById);

			if(domain.isIdNull() || domain.getId() instanceof DomainId) {
				ReflectionUtils.execute(domain, setCreatedById, domain.getActionLogger().getUserAccount().getId());
				ReflectionUtils.setNullViaSetter(setUpdatedById, domain);
			} else {
				ReflectionUtils.execute(domain, setUpdatedById, domain.getActionLogger().getUserAccount().getId());
				ReflectionUtils.setNullViaSetter(setCreatedById, domain);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}