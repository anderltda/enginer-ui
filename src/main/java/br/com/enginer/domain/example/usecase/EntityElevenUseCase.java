package br.com.enginer.domain.example.usecase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityEleven;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.example.dto.entity.EntityTen;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;
import br.com.enginer.domain.system.usecase.page.PageResult;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;

public class EntityElevenUseCase extends AbstractUseCase<EntityEleven> implements br.com.enginer.domain.example.usecase.port.EntityElevenUseCase {
	
	@AutoDependencyInjector
	private EntityTenUseCase entityTenUseCase;
	
	@AutoDependencyInjector
	private EntitySixUseCase entitySixUseCase;
	
	@Override
	public PageResult<EntityEleven> buscarTodosPaginado(EntityEleven domain, Map<String, Object> filter) throws UncheckedException {
		PageResult<EntityEleven> result = (PageResult<EntityEleven>) super.buscarTodosPaginado(domain, filter);
		if(result != null) {
			result.getContent().forEach(entityEleven -> {
				entityEleven.setEntityTen((EntityTen) entityTenUseCase.buscarPorId(entityEleven.getEntityTen()));
				entityEleven.setEntitySix((EntitySix) entitySixUseCase.buscarPorId(entityEleven.getEntitySix()));
			});
		}
		return result;
	}

	@Override
	public List<EntityEleven> salvarLista(List<EntityEleven> entities) throws UncheckedException {
		Integer totalAmount = 0;
		BigDecimal totalValue = BigDecimal.ZERO;
		for (Domain<?> domain : entities) {
		    EntityEleven entityEleven = (EntityEleven) domain;
		    entityEleven.setDateCreate(LocalDateTime.now());
		    // Soma a quantidade total
		    totalAmount += entityEleven.getAmount();
		    // Multiplica o valor unitário pela quantidade e soma no total
		    if (entityEleven.getValue() != null && entityEleven.getAmount() != null) {
		        BigDecimal amount = BigDecimal.valueOf(entityEleven.getAmount());
		        BigDecimal lineValue = amount.multiply(entityEleven.getValue());
		        totalValue = totalValue.add(lineValue);
		    }
		    if (entityEleven.getId() != null) {
		        entityEleven.setDateUpdate(LocalDateTime.now());
		    }
		}
		List<EntityEleven> list = super.salvarLista(entities);
		EntityEleven entityEleven = (EntityEleven) super.buscarPorId(((EntityEleven)list.get(0)));
		EntityTen entityTen = (EntityTen) entityTenUseCase.buscarPorId(entityEleven.getEntityTen());
		entityTen.setTotalAmount(totalAmount);
		entityTen.setTotalValue(totalValue);
		entityTenUseCase.salvar(entityTen);

		List<EntityEleven> updatedList = new ArrayList<>();

		for (EntityEleven domain : list) {
			entityEleven = (EntityEleven) super.buscarPorId(domain);
			entityEleven.setEntitySix((EntitySix) entitySixUseCase.buscarPorId(entityEleven.getEntitySix()));
			entityEleven.setEntityTen(entityTen);
		    updatedList.add(entityEleven);
		}
		
		return updatedList;
	}
}
