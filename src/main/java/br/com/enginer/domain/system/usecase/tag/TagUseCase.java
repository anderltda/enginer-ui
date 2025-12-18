package br.com.enginer.domain.system.usecase.tag;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.dto.entity.tag.TagId;
import br.com.enginer.domain.system.usecase.AbstractUseCase;

/**
 * 
 */
public class TagUseCase extends AbstractUseCase<Tag> implements br.com.enginer.domain.system.usecase.port.tag.TagUseCase {

	private List<Tag> entities;

	/**
	 * @param tags
	 */
	public void pull(String domain, Object domainId, List<String> tags) {

		entities = new ArrayList<Tag>();

		Optional.ofNullable(tags)
				.orElse(List.of())
				.stream()
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.forEach(name -> {
					TagId tagId = new TagId();
					tagId.setNormalizedName(name.toLowerCase());
					tagId.setDomain(null);
					tagId.setDomainId(null);

					Tag tag = new Tag();
					tag.setId(tagId);
					tag.setName(name);
					tag.setCreatedAt(LocalDateTime.now());

					entities.add(tag);
				});

		if (domainId != null) {
			delete(domain, domainId);
		}
	}

	/**
	 * @param domain
	 * @param domainId
	 */
	public void push(String domain, Object domainId) {

		entities.forEach(tag -> {
			tag.getId().setDomain(domain);
			tag.getId().setDomainId(domainId.toString());
			repositoryOutboundPort.save(tag);
		});
	}
	
	/**
	 * @param domain
	 * @param domainId
	 */
	private void delete(String domain, Object domainId) {

		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("id.domain", domain);
		filter.put("id.domainId", domainId);
		
		List<Tag> tags = repositoryOutboundPort.findAll(new Tag(), filter);
		
		tags.forEach(tag -> {
			filter.clear();
			filter.put("normalizedName", tag.getId().getNormalizedName());
			filter.put("domain", tag.getId().getDomain());
			filter.put("domainId", tag.getId().getDomainId());
			repositoryOutboundPort.delete(new Tag(), filter);
		});
	}
}
