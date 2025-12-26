package br.com.enginer.domain.example.usecase.port;

import java.util.List;

import br.com.enginer.domain.example.dto.entity.EntityOne;
import br.com.enginer.domain.system.usecase.core.action.ActionUseCase;

/**
 * 
 */
public interface EntityOneUseCase extends ActionUseCase<EntityOne> {

	/**
	 * @param entityOnes
	 */
	void rowSalvar(List<EntityOne> entityOnes);
	
}
