package br.com.enginer.domain.example.usercase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityEleven;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;
import br.com.enginer.domain.system.usercase.schema.instance.Domain;

public class EntityElevenUserCase extends AbstractUserCase<EntityEleven> {
	
	@AutoDependencyInjector
	private EntityTenUserCase entityTenUserCase;
	
	@AutoDependencyInjector
	private EntitySixUserCase entitySixUserCase;
	
	@Override
	public PageResult<EntityEleven> buscarTodosPaginado(EntityEleven domain, Map<String, Object> filter) throws UncheckedException {
		PageResult<EntityEleven> result = (PageResult<EntityEleven>) super.buscarTodosPaginado(domain, filter);
		if(result != null) {
			result.getContent().forEach(entityEleven -> {
				entityEleven.setEntityTen((EntityTen) entityTenUserCase.buscarPorId(entityEleven.getEntityTen()));
				entityEleven.setEntitySix((EntitySix) entitySixUserCase.buscarPorId(entityEleven.getEntitySix()));
			});
		}
		return result;
	}

	@Override
	public List<EntityEleven> salvarLista(List<EntityEleven> entities) throws UncheckedException {
		Integer totalAmount = 0;
		Double totalValue = 0d;
		for (Domain<?> domain : entities) {
			EntityEleven entityEleven = (EntityEleven)domain;
			entityEleven.setDateCreate(LocalDateTime.now());
		    totalAmount += entityEleven.getAmount();
		    //totalValue += entityEleven.getAmount() * entityEleven.getValue();
			if(entityEleven.getId() != null) {
				entityEleven.setDateUpdate(LocalDateTime.now());
			}
		};
		List<EntityEleven> list = super.salvarLista(entities);
		EntityEleven entityEleven = (EntityEleven) super.buscarPorId(((EntityEleven)list.get(0)));
		EntityTen entityTen = (EntityTen) entityTenUserCase.buscarPorId(entityEleven.getEntityTen());
		entityTen.setTotalAmount(totalAmount);
		//entityTen.setTotalValue(totalValue);
		entityTenUserCase.salvar(entityTen);

		List<EntityEleven> updatedList = new ArrayList<>();

		for (EntityEleven domain : list) {
			entityEleven = (EntityEleven) super.buscarPorId(domain);
			entityEleven.setEntitySix((EntitySix) entitySixUserCase.buscarPorId(entityEleven.getEntitySix()));
			entityEleven.setEntityTen(entityTen);
		    updatedList.add(entityEleven);
		}
		
		return updatedList;
	}
}
