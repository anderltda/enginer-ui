package br.com.enginer.domain.system.dto.entity.tag;

import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.schema.instance.DomainId;

public class TagId extends DomainAbstract<Object[]> implements DomainId {

	private String normalizedName;
	private String domain;
	private String domainId;

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
	public Object[] getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Object[] id) {
		// TODO Auto-generated method stub
		
	}
}
