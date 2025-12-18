package br.com.enginer.domain.example.usecase;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.example.dto.entity.EntitySeven;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.annotation.PostAction;
import br.com.enginer.domain.system.usecase.annotation.PreAction;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;
import br.com.enginer.domain.system.usecase.tag.TagUseCase;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.utils.StringsUtils;

public class EntitySevenUseCase extends AbstractUseCase<EntitySeven> implements br.com.enginer.domain.example.usecase.port.EntitySevenUseCase {
	
	@AutoDependencyInjector
	private EntitySixUseCase entitySixUseCase;
	
	@AutoDependencyInjector
	private TagUseCase tagUseCase;

	@Override
	public EntitySeven salvar(EntitySeven entitySeven) throws UncheckedException {
		if(entitySeven.getId().getIdEntitySeven() == null) {
			entitySeven.getId().setIdEntitySeven(UUID.randomUUID());
		}
		return super.salvar(entitySeven);
	}
	
	public EntitySeven plus(EntitySeven entitySeven) {
		//entitySeven.setDado("Value set in user case plus");
		
		EntitySix entitySix = (EntitySix) entitySixUseCase.buscarPorId(new EntitySix(entitySeven.getId().getIdEntitySix()));
		
		if(entitySeven.getId().getIdEntitySeven() == null) {
			entitySeven.getId().setIdEntitySeven(UUID.randomUUID());
			entitySeven.getId().setEntitySix(entitySix);
		}

		return entitySeven;
	}
	
	@Override
	public PageResult<EntitySeven> buscarTodosPaginado(EntitySeven domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntitySeven> result = (PageResult<EntitySeven>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entitySeven -> {
				entitySeven.getId().setEntitySix((EntitySix) entitySixUseCase.buscarPorId(new EntitySix(entitySeven.getId().getIdEntitySix())));
			});
		}
		
		return result;
	}
	
	@Override
	public EntitySeven buscarPorId(EntitySeven domain) throws UncheckedException {
		EntitySeven entitySeven = (EntitySeven) super.buscarPorId(domain);
		if(entitySeven != null) {
			entitySeven.getId().setEntitySix((EntitySix) entitySixUseCase.buscarPorId(new EntitySix(entitySeven.getId().getIdEntitySix())));
		}
		return entitySeven;
	}
	
	/**
	 * @param tags
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@PreAction
	public void pull(EntitySeven entitySeven) throws Exception {
		if(entitySeven != null) {
			List<String> tags = (List<String>) ReflectionUtils.executeMethod(entitySeven, StringsUtils.getMethod("tags"));
			String domain = entitySeven.getClass().getSimpleName();
			Object domainId = ReflectionUtils.createUriIdComposedType(entitySeven);
			tagUseCase.pull(domain, domainId, tags);
		}
	}
	
	/**
	 * @param tags
	 * @throws Exception 
	 */
	@PostAction
	public void push(EntitySeven entitySeven) throws Exception {
		if(entitySeven != null) {
			String domain = entitySeven.getClass().getSimpleName();
			Object domainId = ReflectionUtils.createUriIdComposedType(entitySeven);
			tagUseCase.push(domain, domainId);
		}
	}
	
}
