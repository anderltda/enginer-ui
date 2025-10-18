package br.com.enginer.domain.example.usercase;

import java.util.List;
import java.util.Map;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntityEight;
import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.example.dto.entity.EntitySeven;
import br.com.enginer.domain.example.dto.entity.EntitySevenId;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.ui.dto.PageResult;
import br.com.enginer.domain.ui.usercase.exception.CheckedException;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.Form;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

/**
 * 
 */
public class EntityNineUserCase extends AbstractUserCase {

	public void keyComposte(EntityNine entityNine) throws UncheckedException {

		if (entityNine.getId() == null) {
			throw new UncheckedException("Faltou o id");
		}

		Object object = buscarPorId(entityNine);
		System.out.println(object.toString());
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<?> buscarTodosPaginado(Domain<?> domain, Map<String, Object> filter) throws UncheckedException {
		System.out.println(filter);

		PageResult<EntityNine> result = (PageResult<EntityNine>) super.buscarTodosPaginado(domain, filter);

		if (result != null) {
			result.getContent().forEach(nine -> {
				EntitySeven seven = (EntitySeven) super.buscarPorId(new EntitySeven(new EntitySevenId(nine.getId().getIdEntitySeven(), nine.getId().getIdEntitySix())));
				seven.getId().setEntitySix((EntitySix) super.buscarPorId(new EntitySix(nine.getId().getIdEntitySix())));
				nine.getId().setEntityEight((EntityEight) super.buscarPorId(new EntityEight(nine.getId().getIdEntityEight())));
				nine.getId().setEntitySeven(seven);
			});
		}

		return result;

	}

	@Override
	public Domain<?> buscarPorId(Domain<?> domain) throws CheckedException {
		System.out.println(domain.getId());
		return super.buscarPorId(domain);
	}

	@Override
	public Form form(Domain<?> domain) throws UncheckedException {
		// TODO Auto-generated method stub
		return super.form(domain);
	}
	
	@Override
	public Domain<?> plus(Domain<?> domain) {
		EntityNine nine = (EntityNine) domain;
		EntitySeven seven = (EntitySeven) super.buscarPorId(new EntitySeven(new EntitySevenId(nine.getId().getIdEntitySeven(), nine.getId().getIdEntitySix())));
		seven.getId().setEntitySix((EntitySix) super.buscarPorId(new EntitySix(nine.getId().getIdEntitySix())));
		nine.getId().setEntityEight((EntityEight) super.buscarPorId(new EntityEight(nine.getId().getIdEntityEight())));
		nine.getId().setEntitySeven(seven);
		return nine;
	}
	
	@Override
	public List<Domain<?>> salvarLista(List<Domain<?>> entities) throws UncheckedException {
		
		List<Domain<?>> list = super.salvarLista(entities);
		list.forEach(domain -> {
			EntityNine nine = (EntityNine) domain;
			EntitySeven seven = (EntitySeven) super.buscarPorId(new EntitySeven(new EntitySevenId(nine.getId().getIdEntitySeven(), nine.getId().getIdEntitySix())));
			seven.getId().setEntitySix((EntitySix) super.buscarPorId(new EntitySix(nine.getId().getIdEntitySix())));
			nine.getId().setEntityEight((EntityEight) super.buscarPorId(new EntityEight(nine.getId().getIdEntityEight())));
			nine.getId().setEntitySeven(seven);
		});
			
		return list;
	}
	
}
