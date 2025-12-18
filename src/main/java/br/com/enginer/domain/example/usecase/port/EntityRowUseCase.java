package br.com.enginer.domain.example.usecase.port;

import java.util.List;

import br.com.enginer.domain.example.dto.entity.EntityRow;
import br.com.enginer.domain.system.usecase.ActionUseCase;

/**
 * 
 */
public interface EntityRowUseCase extends ActionUseCase<EntityRow> {
	
	/**
	 * @param entities
	 * @return
	 */
	List<EntityRow> rowSalvar(List<EntityRow> entities);
	
}
