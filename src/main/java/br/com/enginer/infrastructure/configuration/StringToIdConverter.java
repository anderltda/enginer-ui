package br.com.enginer.infrastructure.configuration;

import org.springframework.core.convert.converter.Converter;

import br.com.enginer.domain.ui.usercase.schema.field.type.Id;

/**
 * 
 */
public class StringToIdConverter implements Converter<String, Id<?>> {

	@Override
	public Id<?> convert(String source) {
		return Id.of(source);
	}
}
