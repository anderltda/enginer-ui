package br.com.enginer.domain.example.usercase.port;

import java.util.List;

import br.com.enginer.domain.example.dto.entity.EntityOne;
import br.com.enginer.domain.system.usercase.ActionUserCase;

/**
 * 
 */
public interface EntityOneUserCase extends ActionUserCase<EntityOne> {

	/**
	 * @param entityOnes
	 */
	void rowSalvar(List<EntityOne> entityOnes);
	
}
