package br.com.enginer.domain.example.usercase.port;

import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.system.usercase.ActionUserCase;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;

/**
 * 
 */
public interface EntityNineUserCase extends ActionUserCase<EntityNine> {

	/**
	 * @param entityNine
	 * @throws UncheckedException
	 */
	void keyComposte(EntityNine entityNine) throws UncheckedException;

}
