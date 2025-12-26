package br.com.enginer.domain.example.usecase;

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
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.core.page.PageResult;

public class EntityOneUseCase extends AbstractUseCase<EntityOne> implements br.com.enginer.domain.example.usecase.port.EntityOneUseCase {
	
	@AutoDependencyInjector
	private EntityStatusUseCase entityStatusUseCase;
	
	@AutoDependencyInjector
	private EntityTwoUseCase entityTwoUseCase;
	
	@AutoDependencyInjector
	private EntityTreeUseCase entityTreeUseCase;
	
	@AutoDependencyInjector
	private EntityFourUseCase entityFourUseCase;
	
	@AutoDependencyInjector
	private EntityFiveUseCase entityFiveUseCase;
	
	@AutoDependencyInjector
	private EntityNineUseCase entityNineUseCase;
	
	@AutoDependencyInjector
	private EntityEightUseCase entityEightUseCase;
	
	@AutoDependencyInjector
	private EntitySevenUseCase entitySevenUseCase;
	
	@AutoDependencyInjector
	private EntitySixUseCase entitySixUseCase;
	

	@Override
	public PageResult<EntityOne> buscarTodosPaginado(EntityOne domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityOne> result = (PageResult<EntityOne>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityOne -> {
				
				EntityTwo   entityTwo = (EntityTwo)  entityTwoUseCase.buscarPorId(entityOne.getEntityTwo());
				EntityTree entityTree = (EntityTree) entityTreeUseCase.buscarPorId(entityTwo.getEntityTree());
				EntityFour entityFour = (EntityFour) entityFourUseCase.buscarPorId(entityTree.getEntityFour());
				EntityFive entityFive = (EntityFive) entityFiveUseCase.buscarPorId(entityFour.getEntityFive());
				
				EntityNine entityNine = (EntityNine) entityNineUseCase.buscarPorId(entityOne.getEntityNine());
				
				if(entityNine != null) {
					EntityEight entityEight = (EntityEight) entityEightUseCase.buscarPorId(new EntityEight(entityNine.getId().getIdEntityEight()));
					EntitySeven entitySeven = (EntitySeven) entitySevenUseCase.buscarPorId(new EntitySeven(new EntitySevenId(entityNine.getId().getIdEntitySeven(), entityNine.getId().getIdEntitySix())));
					EntitySix entitySix = (EntitySix) entitySixUseCase.buscarPorId(new EntitySix(entityNine.getId().getIdEntitySix()));
					entitySeven.getId().setEntitySix(entitySix);
					entityNine.getId().setEntityEight(entityEight);
					entityNine.getId().setEntitySeven(entitySeven);
				}
				
				entityOne.setEntityNine(entityNine);

				EntityStatus entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityOne.getEntityStatus());
				entityOne.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityTwo.getEntityStatus());
				entityTwo.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityTree.getEntityStatus());
				entityTree.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityFour.getEntityStatus());
				entityFour.setEntityStatus(entityStatus);
				
				entityStatus = (EntityStatus) entityStatusUseCase.buscarPorId(entityFive.getEntityStatus());
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
