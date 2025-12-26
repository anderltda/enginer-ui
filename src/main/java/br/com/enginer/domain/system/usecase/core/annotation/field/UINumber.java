package br.com.enginer.domain.system.usecase.core.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UINumber {
	String label();
	String icon() default "";
	String placeholder() default "";
	int min() default 1;
	int max() default 10;
	boolean disable() default false;
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.TAB, TypeTemplate.MODAL } ;
}