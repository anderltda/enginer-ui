package br.com.enginer.domain.example.usercase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntityRow;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;

public class EntityRowUserCase extends AbstractUserCase<EntityRow> {
	
	@AutoDependencyInjector
	private EntityTenUserCase entityTenUserCase;
	
	public List<EntityRow> rowSalvar(List<EntityRow> entities) {
		
		List<EntityRow> list = super.salvarLista(entities);
		List<EntityRow> updatedList = new ArrayList<>();

		for (EntityRow domain : list) {
			EntityRow entity = (EntityRow) super.buscarPorId(domain);
		    entity.setEntityTen((EntityTen) entityTenUserCase.buscarPorId(entity.getEntityTen()));
		    updatedList.add(entity);
		}
		
		return updatedList;
	}
	
	@Override
	public PageResult<EntityRow> buscarTodosPaginado(EntityRow domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityRow> result = (PageResult<EntityRow>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityEleven -> {
				entityEleven.setEntityTen((EntityTen) entityTenUserCase.buscarPorId(entityEleven.getEntityTen()));
			});
		}
		
		return result;
	}
}
