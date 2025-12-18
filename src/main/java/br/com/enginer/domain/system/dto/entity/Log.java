package br.com.enginer.domain.system.dto.entity;

import java.time.LocalDateTime;

import br.com.enginer.domain.system.usecase.schema.instance.DomainAbstract;

public class Log extends DomainAbstract<Long> {

	private Long id;

	private String level;
	private String source;
	private String message;
	private String traceId;
	private String username;
	private LocalDateTime timestamp;

	public Log() {}

	public Log(String level, String source, String message, String traceId, String username) {
		this.level = level;
		this.source = source;
		this.message = message;
		this.traceId = traceId;
		this.username = username;
		this.timestamp = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}
