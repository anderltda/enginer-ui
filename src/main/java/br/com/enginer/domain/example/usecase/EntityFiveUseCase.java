package br.com.enginer.domain.example.usecase;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.example.dto.entity.EntityNineId;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.annotation.PostAction;
import br.com.enginer.domain.system.usecase.annotation.PreAction;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;
import br.com.enginer.domain.system.usecase.tag.TagUseCase;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.utils.StringsUtils;

public class EntityFiveUseCase extends AbstractUseCase<EntityFive> implements br.com.enginer.domain.example.usecase.port.EntityFiveUseCase {
	
	@AutoDependencyInjector
	private EntityNineUseCase entityNineUseCase;
	
	@AutoDependencyInjector
	private EntityStatusUseCase entityStatusUseCase;
	
	@AutoDependencyInjector
	private TagUseCase tagUseCase;
	
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
		
		entityNineUseCase.buscarPorId(entityNine);
	}
	
	@Override
	public PageResult<EntityFive> buscarTodosPaginado(EntityFive domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityFive> result = (PageResult<EntityFive>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityFive -> {
				entityFive.setEntityStatus((EntityStatus) entityStatusUseCase.buscarPorId(entityFive.getEntityStatus()));
			});
		}
		
		return result;
	}
	
	/**
	 * @param tags
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@PreAction
	public void pull(EntityFive entityFive) throws Exception {
		if(entityFive != null) {
			List<String> tags = (List<String>) ReflectionUtils.executeMethod(entityFive, StringsUtils.getMethod("tags"));
			String domain = entityFive.getClass().getSimpleName();
			Object domainId = ReflectionUtils.createUriIdComposedType(entityFive) ;
			tagUseCase.pull(domain, domainId, tags);
		}
	}
	
	/**
	 * @param tags
	 * @throws Exception 
	 */
	@PostAction
	public void push(EntityFive entityFive) throws Exception {
		if(entityFive != null) {
			String domain = entityFive.getClass().getSimpleName();
			Object domainId = ReflectionUtils.createUriIdComposedType(entityFive) ;
			tagUseCase.push(domain, domainId);
		}
	}
}
