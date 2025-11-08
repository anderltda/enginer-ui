package br.com.enginer.domain.example.usercase.port;

import br.com.enginer.domain.example.dto.entity.EntityFive;
import br.com.enginer.domain.system.usercase.ActionUserCase;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;

/**
 * 
 */
public interface EntityFiveUserCase extends ActionUserCase<EntityFive> {
	
	/**
	 * @param domain
	 * @return
	 * @throws UncheckedException
	 */
	void atireiopaunogato(EntityFive entityFive) throws UncheckedException;
}
