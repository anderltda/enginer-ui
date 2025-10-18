package br.com.enginer.domain.ui.dto.logger;

import java.time.LocalDateTime;

/**
 * 
 */
public class ActionLogger {

	private String domain;
	private String actionName;
	private String id;
	private String username;
	private LocalDateTime datelocal;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalDateTime getDatelocal() {
		return datelocal;
	}

	public void setDatelocal(LocalDateTime datelocal) {
		this.datelocal = datelocal;
	}
}
