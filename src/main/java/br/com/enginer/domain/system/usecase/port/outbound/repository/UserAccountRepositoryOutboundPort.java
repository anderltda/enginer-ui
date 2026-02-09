package br.com.enginer.domain.system.usecase.port.outbound.repository;

import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;

/**
 * Port outbound responsável por acesso a dados (camada repositório apenas de UserAccount).
 * Tipado com UserAccount para garantir segurança de tipo em toda a camada de casos de uso.
 */
public interface UserAccountRepositoryOutboundPort extends RepositoryOutboundPort<UserAccount> {
	
	/**
	 * @param domain
	 */
	void pull(Domain<?> domain);
}
