package br.com.enginer.domain.example.usercase;

import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityFour;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.example.dto.entity.EntityTree;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;

public class EntityTreeUserCase extends AbstractUserCase<EntityTree> {
	
	@AutoDependencyInjector
	private EntityStatusUserCase entityStatusUserCase;
	
	@AutoDependencyInjector
	private EntityFourUserCase entityFourUserCase;
	
	@AutoDependencyInjector
	private EntityFiveUserCase entityFiveUserCase;
	
	@Override
	public PageResult<EntityTree> buscarTodosPaginado(EntityTree domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityTree> result = (PageResult<EntityTree>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityTree -> {
				
				EntityFour entityFour = (EntityFour) entityFourUserCase.buscarPorId(entityTree.getEntityFour());
				EntityFive entityFive = (EntityFive) entityFiveUserCase.buscarPorId(entityFour.getEntityFive());
				
				EntityStatus entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityTree.getEntityStatus());
				entityTree.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityFour.getEntityStatus());
				entityFour.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityFive.getEntityStatus());
				entityFive.setEntityStatus(entityStatus);
				
				entityTree.setEntityFour(entityFour);
				entityFour.setEntityFive(entityFive);
			});
		}
		
		return result;
	}

}
