package br.com.enginer.domain.system.usecase.core.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIFilter {
	String label();
	String field();
	String placeholder() default "";
	String[] filter() default {}; // cada item é "chave=valor"
	boolean select() default false;
	boolean disable() default false;
	boolean readonly() default false;
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.MODAL };
}