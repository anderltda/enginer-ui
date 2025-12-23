
package br.com.enginer.domain.system.usecase.tag;

import java.time.LocalDateTime;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.usecase.AbstractUseCase;
import br.com.enginer.domain.system.usecase.annotation.instance.action.pre.PreAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.pre.PreForm;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.schema.instance.DomainId;
import br.com.enginer.domain.system.usecase.utils.ReflectionUtils;
import br.com.enginer.infrastructure.utils.NormalizeUtils;

/**
 * 
 */
public class TagUseCase extends AbstractUseCase<Tag> implements br.com.enginer.domain.system.usecase.port.tag.TagUseCase {
	
	/**
	 *
	 */
	@Override
	@PreForm
	public void decode(Domain<?> domain) {

		if(domain == null || domain instanceof DomainId || domain.isIdNull()) return;

		Tag tag = (Tag) domain;
		
		tag.getId().setDomainId(NormalizeUtils.decodeIfNeeded(tag.getId().getDomainId()));
	}
	
	
	/**
	 * @param tag
	 */
	@PreAction
	@Override
	public void setDateCreateAt(Tag tag) {
		
		if(tag.isIdNull()) {
			tag.setCreatedAt(LocalDateTime.now());
		}
		
		tag.getId().setNormalizedName(ReflectionUtils.normalizeAlphaNumeric(tag.getName()));
	}
}
