package br.com.enginer.domain.system.usercase.annotation.field.behavior;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UITag {
	String label();
	boolean disable() default false;
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.TAB };
}