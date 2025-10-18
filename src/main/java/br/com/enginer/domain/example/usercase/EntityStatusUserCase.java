package br.com.enginer.domain.example.usercase;

import br.com.enginer.domain.AbstractUserCase;
import br.com.enginer.domain.ui.usercase.exception.UncheckedException;
import br.com.enginer.domain.ui.usercase.schema.instance.Domain;

public class EntityStatusUserCase extends AbstractUserCase {
	
	@Override
	public Domain<?> salvar(Domain<?> domain) throws UncheckedException {
		return super.salvar(domain);
	}

}
