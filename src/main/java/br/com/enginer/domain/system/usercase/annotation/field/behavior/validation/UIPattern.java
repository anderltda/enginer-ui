package br.com.enginer.domain.system.usercase.annotation.field.behavior.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface UIPattern {
	String pattern() default "";
	String patternError() default "";
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.FORM, TypeTemplate.MODAL };
}
