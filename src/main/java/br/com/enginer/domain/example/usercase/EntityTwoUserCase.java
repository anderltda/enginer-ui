package br.com.enginer.domain.example.usercase;

import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityFour;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.example.dto.entity.EntityTree;
import br.com.enginer.domain.example.dto.entity.EntityTwo;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;

public class EntityTwoUserCase extends AbstractUserCase<EntityTwo> implements br.com.enginer.domain.example.usercase.port.EntityTwoUserCase {
	
	@AutoDependencyInjector
	private EntityStatusUserCase entityStatusUserCase;
	
	@AutoDependencyInjector
	private EntityTreeUserCase entityTreeUserCase;
	
	@AutoDependencyInjector
	private EntityFourUserCase entityFourUserCase;
	
	@AutoDependencyInjector
	private EntityFiveUserCase entityFiveUserCase;
	
	@Override
	public PageResult<EntityTwo> buscarTodosPaginado(EntityTwo domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityTwo> result = (PageResult<EntityTwo>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityTwo -> {
				
				EntityTree entityTree = (EntityTree) entityTreeUserCase.buscarPorId(entityTwo.getEntityTree());
				EntityFour entityFour = (EntityFour) entityFourUserCase.buscarPorId(entityTree.getEntityFour());
				EntityFive entityFive = (EntityFive) entityFiveUserCase.buscarPorId(entityFour.getEntityFive());
				
				EntityStatus entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityTwo.getEntityStatus());
				entityTwo.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityTree.getEntityStatus());
				entityTree.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityFour.getEntityStatus());
				entityFour.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityFive.getEntityStatus());
				entityFive.setEntityStatus(entityStatus);
				
				entityTwo.setEntityTree(entityTree);
				entityTree.setEntityFour(entityFour);
				entityFour.setEntityFive(entityFive);
			});
		}
		
		return result;
	}

}
