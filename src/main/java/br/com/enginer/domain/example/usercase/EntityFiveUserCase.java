package br.com.enginer.domain.example.usercase;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.example.dto.entity.EntityNineId;
import br.com.enginer.domain.example.dto.entity.EntityStatus;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.annotation.PostAction;
import br.com.enginer.domain.system.usercase.annotation.PreAction;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;
import br.com.enginer.domain.system.usercase.tag.TagUserCase;
import br.com.enginer.domain.system.usercase.utils.ReflectionUtils;
import br.com.enginer.domain.system.usercase.utils.StringsUtils;

public class EntityFiveUserCase extends AbstractUserCase<EntityFive> implements br.com.enginer.domain.example.usercase.port.EntityFiveUserCase {
	
	@AutoDependencyInjector
	private EntityNineUserCase entityNineUserCase;
	
	@AutoDependencyInjector
	private EntityStatusUserCase entityStatusUserCase;
	
	@AutoDependencyInjector
	private TagUserCase tagUserCase;
	
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
		
		entityNineUserCase.buscarPorId(entityNine);
	}
	
	@Override
	public PageResult<EntityFive> buscarTodosPaginado(EntityFive domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityFive> result = (PageResult<EntityFive>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityFive -> {
				entityFive.setEntityStatus((EntityStatus) entityStatusUserCase.buscarPorId(entityFive.getEntityStatus()));
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
			Object domainId = entityFive.getId();
			tagUserCase.pull(domain, domainId, tags);
		}
	}
	
	/**
	 * @param tags
	 */
	@PostAction
	public void push(EntityFive entityFive) {
		if(entityFive != null) {
			String domain = entityFive.getClass().getSimpleName();
			Object domainId = entityFive.getId();
			tagUserCase.push(domain, domainId);
		}
	}
}
