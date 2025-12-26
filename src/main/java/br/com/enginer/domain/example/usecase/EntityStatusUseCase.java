package br.com.enginer.domain.example.usecase;

import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;

public class EntityStatusUseCase extends AbstractUseCase<EntityStatus> implements br.com.enginer.domain.example.usecase.port.EntityStatusUseCase {
	
	@Override
	public EntityStatus salvar(EntityStatus domain) throws UncheckedException {
		return super.salvar(domain);
	}

}
