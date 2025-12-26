package br.com.enginer.domain.system.dto.entity.tag;

import br.com.enginer.domain.system.usecase.core.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIHidden;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIIdPart;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainId;

/**
 * 
 */
public class TagId extends DomainAbstract<TagId> implements DomainId {

	@UIHidden
	@UIColumn(label = "Normalized", initial = true)
	@UIIdPart
	private String normalizedName;

	@UIPosition(x = 2, y = 1)
	@UIFieldValidation(required = true)
	@UIText(label = "Domain", max = 100)
	@UIColumn(label = "Domain", initial = true)
	@UIIdPart
	private String domain;
	
	@UIPosition(x = 2, y = 2)
	@UIFieldValidation(required = true)
	@UIText(label = "Domain-Id", max = 100)
	@UIColumn(label = "Domain-Id", initial = true)
	@UIIdPart
	private String domainId;

	public TagId() {
		super();
	}

	public TagId(String normalizedName, String domain, String domainId) {
		super();
		this.normalizedName = normalizedName;
		this.domain = domain;
		this.domainId = domainId;
	}

	public String getNormalizedName() {
		return this.normalizedName;
	}

	public void setNormalizedName(String normalizedName) {
		this.normalizedName = normalizedName;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomainId() {
		return this.domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TagId that = (TagId) o;
		return java.util.Objects.equals(domainId, that.domainId) && java.util.Objects.equals(domain, that.domain)
				&& java.util.Objects.equals(normalizedName, that.normalizedName);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(domainId, domain, normalizedName);
	}

	@Override
	public String toString() {
		return "TagId [normalizedName=" + normalizedName + ", domain=" + domain + ", domainId=" + domainId + "]";
	}
}
