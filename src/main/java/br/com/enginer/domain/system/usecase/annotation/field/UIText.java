package br.com.enginer.domain.system.usecase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIText {
	String label();
	String mask() default "";
	String icon() default "";
	String placeholder() default "";
	int min() default 1;
	int max() default 20;
	boolean disable() default false;
	boolean allowNegativeNumbers() default false;
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.MODAL } ;
}