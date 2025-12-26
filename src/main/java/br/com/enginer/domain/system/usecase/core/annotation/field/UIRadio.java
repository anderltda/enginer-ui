package br.com.enginer.domain.system.usecase.core.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIRadio {
	String label();
	Class<?> provider();
	String method();
	boolean disable() default false;
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.MODAL } ;
}