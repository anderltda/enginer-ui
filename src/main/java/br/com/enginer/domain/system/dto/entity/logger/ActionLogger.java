package br.com.enginer.domain.system.dto.entity.logger;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

/**
 * 
 */
public class ActionLogger extends DomainAbstract<Long> {

	private Long id;
	private String domain;
	private String entityId;
	private String action;
	private String type;
	private String userId;
	private String username;
	private String source;
	private String ipAddress;
	private String userAgent;
	private Boolean success;
	private String errorMessage;
	private Long durationMs;
	private Object oldValue;
	private Object newValue;
	private UUID requestId;
	private LocalDateTime datelocal;

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
	 * @return the entityId
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
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
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @param oldValue the oldValue to set
	 */
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
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
		return "ActionLogger [id=" + id + ", domain=" + domain + ", entityId=" + entityId + ", action=" + action
				+ ", type=" + type + ", userId=" + userId + ", username=" + username + ", source=" + source
				+ ", ipAddress=" + ipAddress + ", userAgent=" + userAgent + ", success=" + success + ", errorMessage="
				+ errorMessage + ", durationMs=" + durationMs + ", oldValue=" + oldValue + ", newValue=" + newValue
				+ ", requestId=" + requestId + ", datelocal=" + datelocal + "]";
	}
}
