package br.com.enginer.infrastructure.configuration;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 */
public abstract class IdAbstractMixIn {

	@JsonValue
	abstract Object getValue();
}
