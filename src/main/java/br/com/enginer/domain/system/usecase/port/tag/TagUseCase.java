package br.com.enginer.domain.system.usecase.port.tag;

import java.util.List;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.usecase.ActionUseCase;

/**
 * 
 */
public interface TagUseCase extends ActionUseCase<Tag> {

	/**
	 * @param domain
	 * @param domainId
	 * @param tags
	 */
	public void pull(String domain, Object domainId, List<String> tags);
	
	/**
	 * @param domain
	 * @param domainId
	 */
	public void push(String domain, Object domainId);

}
