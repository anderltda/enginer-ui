package br.com.enginer.domain.system.dto.view;

import java.time.LocalDateTime;

import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;

public class AuditLogView extends DomainAbstract<Long> {
	
    private Long id;

    private String traceId;
    private String username;
    private String domain;
    private String actionName;
    private String source;
    private String level;
    private String message;
    private Long executionTimeMs;
    private LocalDateTime timestamp;

    public AuditLogView() {}

    public AuditLogView(String traceId, String username, String domain, String actionName, 
                          String source, String level, String message, Long executionTimeMs) {
        this.traceId = traceId;
        this.username = username;
        this.domain = domain;
        this.actionName = actionName;
        this.source = source;
        this.level = level;
        this.message = message;
        this.executionTimeMs = executionTimeMs;
        this.timestamp = LocalDateTime.now();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getExecutionTimeMs() {
		return executionTimeMs;
	}

	public void setExecutionTimeMs(Long executionTimeMs) {
		this.executionTimeMs = executionTimeMs;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
