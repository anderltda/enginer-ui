package br.com.enginer.domain.example.usercase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntityRow;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

public class EntityRowUserCase extends AbstractUserCase {
	
	public List<EntityRow> rowSalvar(List<Domain<?>> entities) {
		
		List<Domain<?>> list = super.salvarLista(entities);
		List<EntityRow> updatedList = new ArrayList<>();

		for (Domain<?> domain : list) {
			EntityRow entity = (EntityRow) super.buscarPorId(domain);
		    entity.setEntityTen((EntityTen) super.buscarPorId(entity.getEntityTen()));
		    updatedList.add(entity);
		}
		
		return updatedList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityRow> result = (PageResult<EntityRow>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityEleven -> {
				entityEleven.setEntityTen((EntityTen)super.buscarPorId(entityEleven.getEntityTen()));
			});
		}
		
		return result;
	}
}
