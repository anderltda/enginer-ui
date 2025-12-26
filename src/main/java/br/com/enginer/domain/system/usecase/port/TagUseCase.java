package br.com.enginer.domain.system.usecase.port;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.usecase.core.action.ActionUseCase;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;

/**
 * 
 */
public interface TagUseCase extends ActionUseCase<Tag> {
	
	/**
	 * @param domain
	 */
	void decode(Domain<?> domain);
	
	/**
	 * @param tag
	 */
	void setDateCreateAt(Tag tag);

}
