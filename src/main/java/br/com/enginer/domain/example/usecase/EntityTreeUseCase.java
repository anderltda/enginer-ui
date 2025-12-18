package br.com.enginer.domain.example.usecase;

import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityFour;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.example.dto.entity.EntityTree;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;

public class EntityTreeUseCase extends AbstractUseCase<EntityTree> implements br.com.enginer.domain.example.usecase.port.EntityTreeUseCase {
	
	@AutoDependencyInjector
	private EntityStatusUseCase entityStatusUseCase;
	
	@AutoDependencyInjector
	private EntityFourUseCase entityFourUseCase;
	
	@AutoDependencyInjector
	private EntityFiveUseCase entityFiveUseCase;
	
	@Override
	public PageResult<EntityTree> buscarTodosPaginado(EntityTree domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityTree> result = (PageResult<EntityTree>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityTree -> {
				
				EntityFour entityFour = (EntityFour) entityFourUseCase.buscarPorId(entityTree.getEntityFour());
				EntityFive entityFive = (EntityFive) entityFiveUseCase.buscarPorId(entityFour.getEntityFive());
				
				EntityStatus entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityTree.getEntityStatus());
				entityTree.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityFour.getEntityStatus());
				entityFour.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityFive.getEntityStatus());
				entityFive.setEntityStatus(entityStatus);
				
				entityTree.setEntityFour(entityFour);
				entityFour.setEntityFive(entityFive);
			});
		}
		
		return result;
	}

}
