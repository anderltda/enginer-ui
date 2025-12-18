package br.com.enginer.domain.example.usecase.port;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.system.usecase.ActionUseCase;
import br.com.enginer.domain.system.usecase.exception.UncheckedException;

/**
 * 
 */
public interface EntityFiveUseCase extends ActionUseCase<EntityFive> {
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	void atireiopaunogato(EntityFive entityFive) throws UncheckedException;
}
