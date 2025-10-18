package br.com.enginer.domain.example.usercase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntityEleven;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

public class EntityElevenUserCase extends AbstractUserCase {
	
	@SuppressWarnings("unchecked")
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		
		PageResult<EntityEleven> result = (PageResult<EntityEleven>) super.buscarTodosPaginado(domain, filter);
		
		if(result != null) {
			result.getContent().forEach(entityEleven -> {
				entityEleven.setEntityTen((EntityTen)super.buscarPorId(entityEleven.getEntityTen()));
				entityEleven.setEntitySix((EntitySix)super.buscarPorId(entityEleven.getEntitySix()));
			});
		}
		
		return result;
	}

	@Override
	public List<Domain<?>> salvarLista(List<Domain<?>> entities) throws UncheckedException {
		
		Integer totalAmount = 0;
		Double totalValue = 0d;
		
		for (Domain<?> domain : entities) {
			EntityEleven entityEleven = (EntityEleven)domain;
			entityEleven.setDateCreate(LocalDateTime.now());
		    totalAmount += entityEleven.getAmount();
		    totalValue += entityEleven.getAmount() * entityEleven.getValue();
			if(entityEleven.getId() != null) {
				entityEleven.setDateUpdate(LocalDateTime.now());
			}
		};
			
		List<Domain<?>> list = super.salvarLista(entities);
		EntityEleven entityEleven = (EntityEleven) super.buscarPorId(((EntityEleven)list.get(0)));
		EntityTen entityTen = (EntityTen) super.buscarPorId(entityEleven.getEntityTen());
		entityTen.setTotalAmount(totalAmount);
		entityTen.setTotalValue(totalValue);
		super.salvar(entityTen);

		List<Domain<?>> updatedList = new ArrayList<>();

		for (Domain<?> domain : list) {
			entityEleven = (EntityEleven) super.buscarPorId(domain);
			entityEleven.setEntitySix((EntitySix) super.buscarPorId(entityEleven.getEntitySix()));
			entityEleven.setEntityTen(entityTen);
		    updatedList.add(entityEleven);
		}
		
		return updatedList;
	}
}
