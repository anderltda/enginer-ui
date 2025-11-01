package br.com.enginer.domain.system.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.enums.TypeFormat;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIDecimal {
	String label();
	String icon() default "";
	String placeholder() default "";
	boolean disable() default false;
	int precision() default 0;
	int min() default 0;
	int max() default 0;
	TypeFormat typeFormat() default TypeFormat.decimal;
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM };
}