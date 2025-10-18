package br.com.enginer.domain.example.usercase;

import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.example.dto.entity.EntityNineId;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

public class EntityFiveUserCase extends AbstractUserCase {
	
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
		
		Object object = buscarPorId(entityNine);
		System.out.println(object.toString());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityFive> result = (PageResult<EntityFive>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityFive -> {
				entityFive.setEntityStatus((EntityStatus) buscarPorId(entityFive.getEntityStatus()));
			});
		}
		
		return result;
	}

}
