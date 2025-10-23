package br.com.enginer.domain.example.usercase;

import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.example.dto.entity.EntitySeven;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;

public class EntitySevenUserCase extends AbstractUserCase<EntitySeven> {
	
	@AutoDependencyInjector
	private EntitySixUserCase entitySixUserCase;

	@Override
	public EntitySeven salvar(EntitySeven entitySeven) throws UncheckedException {
		if(entitySeven.getId().getIdEntitySeven() == null) {
			entitySeven.getId().setIdEntitySeven(UUID.randomUUID());
		}
		return super.salvar(entitySeven);
	}
	
	public EntitySeven plus(EntitySeven entitySeven) {
		//entitySeven.setDado("Value set in user case plus");
		
		EntitySix entitySix = (EntitySix) entitySixUserCase.buscarPorId(new EntitySix(entitySeven.getId().getIdEntitySix()));
		
		if(entitySeven.getId().getIdEntitySeven() == null) {
			entitySeven.getId().setIdEntitySeven(UUID.randomUUID());
			entitySeven.getId().setEntitySix(entitySix);
		}

		return entitySeven;
	}
	
	@Override
	public PageResult<EntitySeven> buscarTodosPaginado(EntitySeven domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntitySeven> result = (PageResult<EntitySeven>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entitySeven -> {
				entitySeven.getId().setEntitySix((EntitySix) entitySixUserCase.buscarPorId(new EntitySix(entitySeven.getId().getIdEntitySix())));
			});
		}
		
		return result;
	}
	
	@Override
	public EntitySeven buscarPorId(EntitySeven domain) throws UncheckedException {
		EntitySeven entitySeven = (EntitySeven) super.buscarPorId(domain);
		entitySeven.getId().setEntitySix((EntitySix) entitySixUserCase.buscarPorId(new EntitySix(entitySeven.getId().getIdEntitySix())));
		return entitySeven;
	}
	
}
