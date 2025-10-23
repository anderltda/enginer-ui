package br.com.enginer.domain.example.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityEight;
import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityFour;
import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.example.dto.entity.EntityOne;
import br.com.enginer.domain.example.dto.entity.EntitySeven;
import br.com.enginer.domain.example.dto.entity.EntitySevenId;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.example.dto.entity.EntityTree;
import br.com.enginer.domain.example.dto.entity.EntityTwo;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;

public class EntityOneUserCase extends AbstractUserCase<EntityOne> {
	
	@AutoDependencyInjector
	private EntityStatusUserCase entityStatusUserCase;
	
	@AutoDependencyInjector
	private EntityTwoUserCase entityTwoUserCase;
	
	@AutoDependencyInjector
	private EntityTreeUserCase entityTreeUserCase;
	
	@AutoDependencyInjector
	private EntityFourUserCase entityFourUserCase;
	
	@AutoDependencyInjector
	private EntityFiveUserCase entityFiveUserCase;
	
	@AutoDependencyInjector
	private EntityNineUserCase entityNineUserCase;
	
	@AutoDependencyInjector
	private EntityEightUserCase entityEightUserCase;
	
	@AutoDependencyInjector
	private EntitySevenUserCase entitySevenUserCase;
	
	@AutoDependencyInjector
	private EntitySixUserCase entitySixUserCase;
	

	@Override
	public PageResult<EntityOne> buscarTodosPaginado(EntityOne domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityOne> result = (PageResult<EntityOne>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityOne -> {
				
				EntityTwo   entityTwo = (EntityTwo)  entityTwoUserCase.buscarPorId(entityOne.getEntityTwo());
				EntityTree entityTree = (EntityTree) entityTreeUserCase.buscarPorId(entityTwo.getEntityTree());
				EntityFour entityFour = (EntityFour) entityFourUserCase.buscarPorId(entityTree.getEntityFour());
				EntityFive entityFive = (EntityFive) entityFiveUserCase.buscarPorId(entityFour.getEntityFive());
				
				EntityNine entityNine = (EntityNine) entityNineUserCase.buscarPorId(entityOne.getEntityNine());
				EntityEight entityEight = (EntityEight) entityEightUserCase.buscarPorId(new EntityEight(entityNine.getId().getIdEntityEight()));
				
				EntitySeven entitySeven = (EntitySeven) entitySevenUserCase.buscarPorId(new EntitySeven(new EntitySevenId(entityNine.getId().getIdEntitySeven(), entityNine.getId().getIdEntitySix())));
				EntitySix entitySix = (EntitySix) entitySixUserCase.buscarPorId(new EntitySix(entityNine.getId().getIdEntitySix()));
				entitySeven.getId().setEntitySix(entitySix);
				
				entityNine.getId().setEntityEight(entityEight);
				entityNine.getId().setEntitySeven(entitySeven);
				
				entityOne.setEntityNine(entityNine);

				EntityStatus entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityOne.getEntityStatus());
				entityOne.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityTwo.getEntityStatus());
				entityTwo.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityTree.getEntityStatus());
				entityTree.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityFour.getEntityStatus());
				entityFour.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUserCase.buscarPorId(entityFive.getEntityStatus());
				entityFive.setEntityStatus(entityStatus);
				
				entityOne.setEntityTwo(entityTwo);
				entityTwo.setEntityTree(entityTree);
				entityTree.setEntityFour(entityFour);
				entityFour.setEntityFive(entityFive);
			});
		}
		
		return result;
	}
	
	public void rowSalvar(List<EntityOne> entityOnes) {
		super.salvarLista(entityOnes);
	}
}
