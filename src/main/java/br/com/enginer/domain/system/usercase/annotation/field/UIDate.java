package br.com.enginer.domain.system.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIDate {
	String label();
	String icon() default "calendar";
	String placeholder() default "";
	boolean showtime() default false;
	boolean disable() default false;
	TypeDateFormat format() default TypeDateFormat.DATE_FORMAT;
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.TAB, TypeTemplate.MODAL };
}