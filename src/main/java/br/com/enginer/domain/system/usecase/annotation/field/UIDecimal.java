package br.com.enginer.domain.system.usecase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.enums.TypeFormat;
import br.com.enginer.domain.system.usecase.enums.TypeLocale;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

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
	TypeLocale locale() default TypeLocale.PT_BR; 
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM };
}