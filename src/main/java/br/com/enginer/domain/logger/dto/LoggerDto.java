package br.com.enginer.domain.logger.dto;

public class LoggerDto {

	private Class<?> clazz;
	private String message;
	private Throwable throwable;

	public LoggerDto(Class<?> clazz, String message) {
		this.clazz = clazz;
		this.message = message;
	}

	public LoggerDto(Class<?> clazz, Throwable throwable) {
		this.clazz = clazz;
		this.throwable = throwable;
	}

	public LoggerDto(Class<?> clazz, String message, Throwable throwable) {
		this.clazz = clazz;
		this.message = message;
		this.throwable = throwable;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String getMessage() {
		return message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

}
