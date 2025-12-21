package br.com.enginer.domain.example.usecase;

import java.time.LocalDateTime;
import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;

public class EntityTenUseCase extends AbstractUseCase<EntityTen> implements br.com.enginer.domain.example.usecase.port.EntityTenUseCase {
	
	@AutoDependencyInjector
	private EntityStatusUseCase entityStatusUseCase;
	
	@Override
	public PageResult<EntityTen> buscarTodosPaginado(EntityTen domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityTen> result = (PageResult<EntityTen>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityTen -> {
				entityTen.setEntityStatus((EntityStatus) entityStatusUseCase.buscarPorId(entityTen.getEntityStatus()));
			});
		}
		
		return result;
	}
	
	@Override
	public EntityTen salvar(EntityTen domain) throws UncheckedException {

		EntityTen entityTen = (EntityTen) domain;
		
		if(entityTen.getId() == null) {
			entityTen.setDateCreate(LocalDateTime.now());
		} else {
			entityTen.setDateUpdate(LocalDateTime.now());
		}
		
		return super.salvar(entityTen);
	}
}
