package br.com.enginer.domain.example.usercase;

import java.util.List;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

public class EntitySixUserCase extends AbstractUserCase<EntitySix> {
	
	@Override
	public List<EntitySix> salvarLista(List<EntitySix> entities) throws UncheckedException {

		List<EntitySix> list = super.salvarLista(entities);
		
		for (Domain<?> domain : list) {
			EntitySix entitySix = (EntitySix) domain;
			System.out.println(entitySix);
		}
		
		return list;
	}
	
	@Override
	public void excluirLista(List<EntitySix> entities) throws UncheckedException {
		//throw new UncheckedException("Deu um erro no bagulho!!!!");
		super.excluirLista(entities);
	}
	

}
