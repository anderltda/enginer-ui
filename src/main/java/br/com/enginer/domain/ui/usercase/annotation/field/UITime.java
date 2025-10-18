package br.com.enginer.domain.ui.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UITime {
	String label();
	String icon() default "time";
	String placeholder() default "";
	boolean disabled() default false;
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.MODAL } ;
}