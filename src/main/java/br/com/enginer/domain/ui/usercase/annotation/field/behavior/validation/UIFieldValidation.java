package br.com.enginer.domain.ui.usercase.annotation.field.behavior.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIFieldValidation {
	boolean required();
    UIPattern pattern() default @UIPattern;
    UIAsync async() default @UIAsync;
    UISync sync() default @UISync;
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL };
}