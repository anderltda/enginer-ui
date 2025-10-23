package br.com.enginer.domain.system.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UISelect {
	String label();
	Class<?> provider();
	String method();
	boolean multi() default false;
	boolean disable() default false;
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.MODAL } ;
}