package br.com.enginer.domain.example.usecase;

import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.example.dto.entity.EntityNineId;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;

/**
 * 
 */
public class EntityFiveUseCase extends AbstractUseCase<EntityFive> implements br.com.enginer.domain.example.usecase.port.EntityFiveUseCase {
	
	@AutoDependencyInjector
	private EntityNineUseCase entityNineUseCase;
	
	@AutoDependencyInjector
	private EntityStatusUseCase entityStatusUseCase;
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	public void atireiopaunogato(EntityFive entityFive) throws UncheckedException {
		
		if(entityFive.getId() == null) {
			throw new UncheckedException("Faltou o id");
		}
		
		EntityNine entityNine = new EntityNine();
		EntityNineId entityNineId = new EntityNineId();
		entityNineId.setIdEntityEight(1l);
		entityNineId.setIdEntitySeven(UUID.randomUUID());
		entityNineId.setIdEntitySix(1l);
		entityNine.setId(entityNineId);
		
		entityNineUseCase.buscarPorId(entityNine);
	}
	
	/**
	 *
	 */
	@Override
	public PageResult<EntityFive> buscarTodosPaginado(EntityFive domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityFive> result = (PageResult<EntityFive>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityFive -> {
				entityFive.setEntityStatus((EntityStatus) entityStatusUseCase.buscarPorId(entityFive.getEntityStatus()));
			});
		}
		
		return result;
	}
}
