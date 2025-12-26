package br.com.enginer.domain.example.usecase.port;

import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.system.usecase.core.action.ActionUseCase;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;

/**
 * 
 */
public interface EntityNineUseCase extends ActionUseCase<EntityNine> {

	/**
	 * @param entityNine
	 * @throws UncheckedException
	 */
	void keyComposte(EntityNine entityNine) throws UncheckedException;

}
