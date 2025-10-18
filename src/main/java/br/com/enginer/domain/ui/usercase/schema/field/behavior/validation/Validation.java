package br.com.enginer.domain.ui.usercase.schema.field.behavior.validation;

import br.com.enginer.domain.ui.usercase.schema.field.behavior.Pattern;

/**
 * 
 */
public class Validation {

	private Pattern pattern;
	private Async async;
	private Sync sync;
	
	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Async getAsync() {
		return async;
	}

	public void setAsync(Async async) {
		this.async = async;
	}

	public Sync getSync() {
		return sync;
	}

	public void setSync(Sync sync) {
		this.sync = sync;
	}
}
