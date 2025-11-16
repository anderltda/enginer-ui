package br.com.enginer.domain.system.dto.entity.tag;

import java.time.LocalDateTime;

import br.com.enginer.domain.system.usercase.schema.instance.DomainAbstract;

/**
 * 
 */
public class Tag extends DomainAbstract<TagId> {
	
	private TagId id;

	private String name;

	private LocalDateTime createdAt;

	@Override
	public TagId getId() {
		return this.id;
	}

	public void setId(TagId id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "Tag{" + "id=" + (id != null ? id.toString() : "null") + ", " + "name="
				+ (name != null ? name.toString() : "null") + ", " + "createdAt="
				+ (createdAt != null ? createdAt.toString() : "null") + '}';
	}
}
