package br.com.enginer.domain.example.usercase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.example.dto.entity.File;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

public class EntityTenUserCase extends AbstractUserCase {
	
	@AutoDependencyInjector
	private FileUserCase fileUserCase;
	
	@SuppressWarnings("unchecked")
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityTen> result = (PageResult<EntityTen>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityTen -> {
				entityTen.setEntityStatus((EntityStatus) buscarPorId(entityTen.getEntityStatus()));
			});
		}
		
		return result;
	}
	
	@Override
	public Domain<?> salvar(Domain<?> domain) throws UncheckedException {

		EntityTen entityTen = (EntityTen) domain;
		
		if(entityTen.getId() == null) {
			entityTen.setDateCreate(LocalDateTime.now());
		} else {
			entityTen.setDateUpdate(LocalDateTime.now());
		}
		
		EntityTen entityTenNew = (EntityTen) super.salvar(entityTen);
		
		entityTen.getWallPickers().forEach(file -> {
			fileUserCase.salvarEntityId(file, entityTenNew.getId());
		});
		
		return entityTenNew;
	}
	
	@Override
	public Domain<?> buscarPorId(Domain<?> domain) throws CheckedException {
		
		EntityTen entityTen = (EntityTen) super.buscarPorId(domain);
		
		List<File> wallPickers = (List<File>) fileUserCase.buscarPorEntityIdAndDomain(
			entityTen.getId(), 
			EntityTen.class.getSimpleName()
		);
		
		entityTen.setWallPickers(wallPickers);
		
		return entityTen;
	}
}
