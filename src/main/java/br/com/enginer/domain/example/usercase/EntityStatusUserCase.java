package br.com.enginer.domain.example.usercase;

import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;

public class EntityStatusUserCase extends AbstractUserCase<EntityStatus> implements br.com.enginer.domain.example.usercase.port.EntityStatusUserCase {
	
	@Override
	public EntityStatus salvar(EntityStatus domain) throws UncheckedException {
		return super.salvar(domain);
	}

}
