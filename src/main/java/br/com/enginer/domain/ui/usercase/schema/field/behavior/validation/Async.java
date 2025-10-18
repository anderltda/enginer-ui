package br.com.enginer.domain.ui.usercase.schema.field.behavior.validation;

/**
 * 
 */
public class Async {

	private String function;
	private String message;
	private String method;

	/**
	 * 
	 */
	public Async() {
		super();
	}

	/**
	 * @param function
	 * @param message
	 * @param method
	 */
	public Async(String function, String message, String method) {
		super();
		this.function = function;
		this.message = message;
		this.method = method;
	}

	public String getFunction() {
		return getMessage() != null && getMethod() != null ? function: null;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getMessage() {
		return !message.isEmpty() ? message : null;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMethod() {
		return !method.isEmpty() ? method : null;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
