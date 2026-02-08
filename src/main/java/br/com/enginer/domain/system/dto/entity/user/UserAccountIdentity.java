package br.com.enginer.domain.system.dto.entity.user;

import java.time.Instant;
import java.util.Objects;

import br.com.enginer.domain.system.usecase.core.annotation.instance.UIHeader;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonBack;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonClear;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormSave;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

@UIHeader(title = "User-Account-Identity")
@UIButtonAction(includes = {
	UIButtonClear.class, 
	UIButtonBack.class, 
	// FILTER
	UIButtonFilterTabNew.class, 
	UIButtonFilterFormNew.class, 
	UIButtonFilterSearch.class, 
	// FORM
	UIButtonFormDelete.class,
	UIButtonFormEdit.class, 
	UIButtonFormSave.class,
})
@UIPaginator(
	config = @UIConfig(expandable = false, multiSelectable = false),
	actions = @UIButtonAction(
	includes = { 
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,
	}
))
public class UserAccountIdentity extends DomainAbstract<Long> {

	private Long id;
	private String provider;
	private String providerSubject;
	private String providerTenant;
	private Instant createdAt;
	private UserAccount userAccount;
	private Long idSystemUserAccount;

	/**
	 * 
	 */
	public UserAccountIdentity() {
		super();
	}

	/**
	 * @param id
	 */
	public UserAccountIdentity(Long id) {
		super();
		this.id = id;
	}

	/**
	 *
	 */
	@Override
	public Boolean isIdNull() {
		Boolean isIdNull = false;
		try {
			isIdNull = (Boolean) ReflectionUtils.isIdNull(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isIdNull;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the provider
	 */
	public String getProvider() {
		return provider;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	}

	/**
	 * @return the providerSubject
	 */
	public String getProviderSubject() {
		return providerSubject;
	}

	/**
	 * @param providerSubject the providerSubject to set
	 */
	public void setProviderSubject(String providerSubject) {
		this.providerSubject = providerSubject;
	}

	/**
	 * @return the providerTenant
	 */
	public String getProviderTenant() {
		return providerTenant;
	}

	/**
	 * @param providerTenant the providerTenant to set
	 */
	public void setProviderTenant(String providerTenant) {
		this.providerTenant = providerTenant;
	}

	/**
	 * @return the createdAt
	 */
	public Instant getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the userAccount
	 */
	public UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	 * @param userAccount the userAccount to set
	 */
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	
	/**
	 * @param idSystemUserAccount the idSystemUserAccount to set
	 */
	public void setIdSystemUserAccount(Long idSystemUserAccount) {
		this.userAccount = new UserAccount(idSystemUserAccount);
		this.idSystemUserAccount = idSystemUserAccount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccountIdentity other = (UserAccountIdentity) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "UserAccountIdentity [id=" + id + ", provider=" + provider + ", providerSubject=" + providerSubject
				+ ", providerTenant=" + providerTenant + ", createdAt=" + createdAt + ", userAccount=" + userAccount
				+ ", idSystemUserAccount=" + idSystemUserAccount + "]";
	}

	
}
