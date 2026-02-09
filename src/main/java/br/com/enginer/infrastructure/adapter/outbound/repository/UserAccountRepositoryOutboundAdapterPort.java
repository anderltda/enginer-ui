package br.com.enginer.infrastructure.adapter.outbound.repository;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
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

			if (domain == null || domain instanceof UserAccount || domain.getActionLogger() == null)
				return;

			ReflectionUtils.execute(domain, "setIdSystemUserAccount", Long.valueOf(domain.getActionLogger().getUserId()));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}