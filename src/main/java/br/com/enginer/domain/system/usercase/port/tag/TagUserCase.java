package br.com.enginer.domain.system.usercase.port.tag;

import java.util.List;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.usercase.ActionUserCase;

/**
 * 
 */
public interface TagUserCase extends ActionUserCase<Tag> {

	/**
	 * @param tags
	 */
	public void pull(List<String> tags);
	
	/**
	 * @param domain
	 * @param domainId
	 */
	public void push(String domain, Object domainId);

}
