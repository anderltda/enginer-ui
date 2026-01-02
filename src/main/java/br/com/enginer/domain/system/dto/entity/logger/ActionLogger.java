package br.com.enginer.domain.system.dto.entity.logger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.enginer.domain.system.usecase.core.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionDomain;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButton;
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
import br.com.enginer.domain.system.usecase.core.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

@UITitle("Action-Logger")
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
	},
	value = {
        @UIButton(
    		label = "Visualizar", 
			template = TypeTemplate.PAGINATOR, 
			state = TypeButtonState.BTN_STATE_PRIMARY,
			icon = "link",
			action = @UIAction(
				actionObject = @UIActionDomain(object = "actionLogger", param = "$id")
			)
		),
	}
))
public class ActionLogger extends DomainAbstract<Long> {

	private Long id;
	
	@UIColumn(label = "Domain", initial = true)
	private String domain;
	
	@UIColumn(label = "Domain-Id", initial = false)
	private String domainId;
	
	@UIColumn(label = "Action", initial = true)
	private String action;
	
	@UIColumn(label = "Type", initial = true)
	private String type;
	
	@UIColumn(label = "UserId", initial = false)
	private String userId;
	
	@UIColumn(label = "Username", initial = true)
	private String username;
	
	@UIColumn(label = "Source", initial = true)
	private String source;
	
	@UIColumn(label = "Ip", initial = false)
	private String ipAddress;
	
	@UIColumn(label = "User Agent", initial = false)
	private String userAgent;
	
	@UIColumn(label = "Success", initial = true)
	private Boolean success;
	
	@UIColumn(label = "Error", initial = false)
	private String errorMessage;
	
	@UIColumn(label = "Duration", initial = false)
	private Long durationMs;
	
	@UIColumn(label = "New value", initial = false, hidden = true)
	private Object newValue;
	
	@UIColumn(label = "Url", initial = true)
	private String url;

	@UIColumn(label = "Modal", initial = false)
	private Boolean modal;
	
	@UIColumn(label = "Request-Id", initial = true)
	private UUID requestId;
	
	@UIColumn(label = "Data Local", initial = true)
	private LocalDateTime datelocal;
	
	@UIColumn(label = "Date Create", initial = true)
	private Instant createdAt;

	public ActionLogger() {
		super();
	}

	public ActionLogger(Long id) {
		super();
		this.id = id;
	}

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
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the domainId
	 */
	public String getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @return the userAgent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * @param userAgent the userAgent to set
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * @return the success
	 */
	public Boolean getSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the durationMs
	 */
	public Long getDurationMs() {
		return durationMs;
	}

	/**
	 * @param durationMs the durationMs to set
	 */
	public void setDurationMs(Long durationMs) {
		this.durationMs = durationMs;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * @param newValue the newValue to set
	 */
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the modal
	 */
	public Boolean getModal() {
		return modal;
	}

	/**
	 * @param modal the modal to set
	 */
	public void setModal(Boolean modal) {
		this.modal = modal;
	}

	/**
	 * @return the requestId
	 */
	public UUID getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(UUID requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the datelocal
	 */
	public LocalDateTime getDatelocal() {
		return datelocal;
	}

	/**
	 * @param datelocal the datelocal to set
	 */
	public void setDatelocal(LocalDateTime datelocal) {
		this.datelocal = datelocal;
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
		ActionLogger other = (ActionLogger) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ActionLogger [id=" + id + ", domain=" + domain + ", domainId=" + domainId + ", action=" + action
				+ ", type=" + type + ", userId=" + userId + ", username=" + username + ", source=" + source
				+ ", ipAddress=" + ipAddress + ", userAgent=" + userAgent + ", success=" + success + ", errorMessage="
				+ errorMessage + ", durationMs=" + durationMs + ", newValue=" + newValue
				+ ", url=" + url + ", modal=" + modal + ", requestId=" + requestId
				+ ", datelocal=" + datelocal + "]";
	}
}
