package br.com.enginer.domain.example.usercase.port;

import java.util.List;

import br.com.enginer.domain.example.dto.entity.EntityRow;
import br.com.enginer.domain.system.usercase.ActionUserCase;

/**
 * 
 */
public interface EntityRowUserCase extends ActionUserCase<EntityRow> {
	
	/**
	 * @param entities
	 * @return
	 */
	List<EntityRow> rowSalvar(List<EntityRow> entities);
	
}
