package br.com.enginer.domain.ui.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

/**
	style="label"
	style="label label-success"
	style="label label-warning"
	style="label label-important"
	style="label label-info"
	style="label label-inverse"
	style="badge"
	style="badge badge-success"
	style="badge badge-warning"
	style="badge badge-important"
	style="badge badge-info"
	style="badge badge-inverse"
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIColumn {
	String label(); // nome da coluna
	boolean initial(); // aparece logo na tela
	String style() default ""; // class css para o valor da coluna
	UIConditional conditional() default @UIConditional;
	String[] fields() default {}; // fields do domain - Apenas utilizado em Objects Domain
	boolean hidden() default false; // nunca exibida
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.MODAL };
}
