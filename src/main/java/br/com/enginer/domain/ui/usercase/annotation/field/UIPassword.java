package br.com.enginer.domain.ui.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIPassword {
	String label();
	String icon() default "shield_lock";
	String placeholder() default "";
	int min() default 4;
	int max() default 20;
	boolean disabled() default false;
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.MODAL } ;
}