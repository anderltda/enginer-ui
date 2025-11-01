package br.com.enginer.domain.system.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UITime {
	String label();
	String icon() default "time";
	String placeholder() default "";
	boolean disable() default false;
	TypeDateFormat format() default TypeDateFormat.TIME_HHMM_FORMAT;
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.MODAL } ;
}