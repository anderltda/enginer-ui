package br.com.enginer.domain.example.usecase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityEight;
import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.example.dto.entity.EntitySeven;
import br.com.enginer.domain.example.dto.entity.EntitySevenId;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.core.exception.CheckedException;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.core.page.PageResult;

/**
 * 
 */
public class EntityNineUseCase extends AbstractUseCase<EntityNine> implements br.com.enginer.domain.example.usecase.port.EntityNineUseCase {
	
	@AutoDependencyInjector
	private EntitySixUseCase entitySixUseCase;
	
	@AutoDependencyInjector
	private EntitySevenUseCase entitySevenUseCase;
	
	@AutoDependencyInjector
	private EntityEightUseCase entityEightUseCase;

	public void keyComposte(EntityNine entityNine) throws UncheckedException {

		if (entityNine.getId() == null) {
			throw new UncheckedException("Faltou o id");
		}

		buscarPorId(entityNine);
	}

	@Override
	public PageResult<EntityNine> buscarTodosPaginado(EntityNine domain, Map<String, Object> filter) throws UncheckedException {

		PageResult<EntityNine> result = (PageResult<EntityNine>) super.buscarTodosPaginado(domain, filter);

		if (result != null) {
			result.getContent().forEach(nine -> {
				EntitySeven seven = (EntitySeven) entitySevenUseCase.buscarPorId(new EntitySeven(new EntitySevenId(nine.getId().getIdEntitySeven(), nine.getId().getIdEntitySix())));
				seven.getId().setEntitySix((EntitySix) entitySixUseCase.buscarPorId(new EntitySix(nine.getId().getIdEntitySix())));
				nine.getId().setEntityEight((EntityEight) entityEightUseCase.buscarPorId(new EntityEight(nine.getId().getIdEntityEight())));
				nine.getId().setEntitySeven(seven);
			});
		}

		return result;

	}

	@Override
	public EntityNine buscarPorId(EntityNine domain) throws CheckedException {
		return super.buscarPorId(domain);
	}

	@Override
	public EntityNine plus(EntityNine domain) {
		EntityNine nine = (EntityNine) domain;
		EntitySeven seven = (EntitySeven) entitySevenUseCase.buscarPorId(new EntitySeven(new EntitySevenId(nine.getId().getIdEntitySeven(), nine.getId().getIdEntitySix())));
		seven.getId().setEntitySix((EntitySix) entitySixUseCase.buscarPorId(new EntitySix(nine.getId().getIdEntitySix())));
		nine.getId().setEntityEight((EntityEight) entityEightUseCase.buscarPorId(new EntityEight(nine.getId().getIdEntityEight())));
		nine.getId().setEntitySeven(seven);
		return nine;
	}
	
	@Override
	public List<EntityNine> salvarLista(List<EntityNine> entities) throws UncheckedException {
		
		List<EntityNine> list = super.salvarLista(entities);
		list.forEach(domain -> {
			EntityNine nine = (EntityNine) domain;
			EntitySeven seven = (EntitySeven) entitySevenUseCase.buscarPorId(new EntitySeven(new EntitySevenId(nine.getId().getIdEntitySeven(), nine.getId().getIdEntitySix())));
			seven.getId().setEntitySix((EntitySix) entitySixUseCase.buscarPorId(new EntitySix(nine.getId().getIdEntitySix())));
			nine.getId().setEntityEight((EntityEight) entityEightUseCase.buscarPorId(new EntityEight(nine.getId().getIdEntityEight())));
			nine.getId().setEntitySeven(seven);
		});
			
		return list;
	}
	
}
