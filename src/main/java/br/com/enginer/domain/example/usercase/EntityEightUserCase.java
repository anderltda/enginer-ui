package br.com.enginer.domain.example.usercase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntityEight;
import br.com.enginer.domain.example.dto.entity.EntitySeven;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

public class EntityEightUserCase extends AbstractUserCase {
	
	
	@Override
	public List<Domain<?>> salvarLista(List<Domain<?>> entities) throws UncheckedException {

		List<Domain<?>> entitys = new ArrayList<>();
		List<Domain<?>> list = super.salvarLista(entities);
		
		for (Domain<?> domain : list) {
			EntityEight entityEight = (EntityEight) buscarPorId(domain);
			entitys.add(entityEight);
		}
		
		return entitys;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityEight> result = (PageResult<EntityEight>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityEight -> {
				EntitySeven entitySeven = (EntitySeven) buscarPorId(entityEight.getEntitySeven());
				EntitySix entitySix = (EntitySix) buscarPorId(new EntitySix(entitySeven.getId().getIdEntitySix()));
				entitySeven.getId().setEntitySix(entitySix);
				entityEight.setEntitySeven(entitySeven);
			});
		}
		
		return result;
	}

}
