package br.com.enginer.domain.example.usecase.port;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.example.dto.entity.EntityNine;
import br.com.enginer.domain.system.usecase.core.action.ActionUseCase;
import br.com.enginer.domain.system.usecase.core.exception.UncheckedException;

/**
 * 
 */
public interface EntityFiveUseCase extends ActionUseCase<EntityFive> {
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	EntityNine atireiopaunogato(EntityFive entityFive) throws UncheckedException;
}
