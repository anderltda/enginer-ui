package br.com.enginer.domain.example.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.exception.CheckedException;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;
import br.com.enginer.domain.system.usecase.upload.UploadFileUseCase;

public class EntityTenUseCase extends AbstractUseCase<EntityTen> implements br.com.enginer.domain.example.usecase.port.EntityTenUseCase {
	
	@AutoDependencyInjector
	private UploadFileUseCase uploadFileUseCase;
	
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
		
		EntityTen entityTenNew = (EntityTen) super.salvar(entityTen);
		
		entityTen.getFiles().forEach(file -> {
			uploadFileUseCase.salvarEntityId(file, entityTenNew.getId());
		});
		
		return entityTenNew;
	}
	
	@Override
	public EntityTen buscarPorId(EntityTen domain) throws CheckedException {
		
		EntityTen entityTen = (EntityTen) super.buscarPorId(domain);
		
		List<UploadFile> files = (List<UploadFile>) uploadFileUseCase.buscarPorDomainAndDomainId(EntityTen.class.getSimpleName(), entityTen.getId());
		
		entityTen.setFiles(files);
		
		return entityTen;
	}
	
}
