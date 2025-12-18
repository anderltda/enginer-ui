package br.com.enginer.domain.example.usecase;

import java.util.List;

import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;

public class EntitySixUseCase extends AbstractUseCase<EntitySix> implements br.com.enginer.domain.example.usecase.port.EntitySixUseCase {
	
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
