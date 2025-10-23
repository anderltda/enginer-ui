package br.com.enginer.domain.example.usercase;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;

public class EntityStatusUserCase extends AbstractUserCase<EntityStatus> {
	
	@Override
	public EntityStatus salvar(EntityStatus domain) throws UncheckedException {
		return super.salvar(domain);
	}

}
