package br.com.enginer.domain.example.usecase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityRow;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.system.usecase.core.AbstractUseCase;
import br.com.enginer.domain.system.usecase.core.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.core.page.PageResult;

public class EntityRowUseCase extends AbstractUseCase<EntityRow> implements br.com.enginer.domain.example.usecase.port.EntityRowUseCase {
	
	@AutoDependencyInjector
	private EntityTenUseCase entityTenUseCase;
	
	public List<EntityRow> rowSalvar(List<EntityRow> entities) {
		
		List<EntityRow> list = super.salvarLista(entities);
		List<EntityRow> updatedList = new ArrayList<>();

		for (EntityRow domain : list) {
			EntityRow entity = (EntityRow) super.buscarPorId(domain);
		    entity.setEntityTen((EntityTen) entityTenUseCase.buscarPorId(entity.getEntityTen()));
		    updatedList.add(entity);
		}
		
		return updatedList;
	}
	
	@Override
	public PageResult<EntityRow> buscarTodosPaginado(EntityRow domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityRow> result = (PageResult<EntityRow>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityEleven -> {
				entityEleven.setEntityTen((EntityTen) entityTenUseCase.buscarPorId(entityEleven.getEntityTen()));
			});
		}
		
		return result;
	}
}
