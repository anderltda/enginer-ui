package br.com.enginer.domain.system.usecase.port.tag;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.usecase.ActionUseCase;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;

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
