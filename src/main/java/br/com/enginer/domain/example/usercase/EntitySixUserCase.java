package br.com.enginer.domain.example.usercase;

import java.util.List;

import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;

public class EntitySixUserCase extends AbstractUserCase<EntitySix> implements br.com.enginer.domain.example.usercase.port.EntitySixUserCase {
	
	@Override
	public List<EntitySix> salvarLista(List<EntitySix> entities) throws UncheckedException {

		List<EntitySix> list = super.salvarLista(entities);
		return list;
	}
	
	@Override
	public void excluirLista(List<EntitySix> entities) throws UncheckedException {
		//throw new UncheckedException("Deu um erro no bagulho!!!!");
		super.excluirLista(entities);
	}
	

}
