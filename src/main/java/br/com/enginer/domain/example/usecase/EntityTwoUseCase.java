package br.com.enginer.domain.example.usecase;

import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityFour;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.example.dto.entity.EntityTree;
import br.com.enginer.domain.example.dto.entity.EntityTwo;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.core.page.PageResult;

public class EntityTwoUseCase extends AbstractUseCase<EntityTwo> implements br.com.enginer.domain.example.usecase.port.EntityTwoUseCase {
	
	@AutoDependencyInjector
	private EntityStatusUseCase entityStatusUseCase;
	
	@AutoDependencyInjector
	private EntityTreeUseCase entityTreeUseCase;
	
	@AutoDependencyInjector
	private EntityFourUseCase entityFourUseCase;
	
	@AutoDependencyInjector
	private EntityFiveUseCase entityFiveUseCase;
	
	@Override
	public PageResult<EntityTwo> buscarTodosPaginado(EntityTwo domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityTwo> result = (PageResult<EntityTwo>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityTwo -> {
				
				EntityTree entityTree = (EntityTree) entityTreeUseCase.buscarPorId(entityTwo.getEntityTree());
				EntityFour entityFour = (EntityFour) entityFourUseCase.buscarPorId(entityTree.getEntityFour());
				EntityFive entityFive = (EntityFive) entityFiveUseCase.buscarPorId(entityFour.getEntityFive());
				
				EntityStatus entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityTwo.getEntityStatus());
				entityTwo.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityTree.getEntityStatus());
				entityTree.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityFour.getEntityStatus());
				entityFour.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityFive.getEntityStatus());
				entityFive.setEntityStatus(entityStatus);
				
				entityTwo.setEntityTree(entityTree);
				entityTree.setEntityFour(entityFour);
				entityFour.setEntityFive(entityFive);
			});
		}
		
		return result;
	}

}
