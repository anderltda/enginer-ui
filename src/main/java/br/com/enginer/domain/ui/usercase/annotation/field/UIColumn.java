package br.com.enginer.domain.ui.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIColumn {
	String label(); // nome da coluna
	boolean initial(); // aparece logo na tela
	String[] fields() default {}; // fields do domain - Apenas utilizado em Objects Domain
	boolean hidden() default false; // nunca exibida
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.MODAL };
}