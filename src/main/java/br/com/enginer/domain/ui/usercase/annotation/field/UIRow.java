package br.com.enginer.domain.ui.usercase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIRow {
	boolean visible(); // colunas visiveis no paginator
	String[] fields() default {}; // fields do domain - Apenas utilizado em Objects Domain
	String calculation() default ""; // valor de um calculo ex: field1 = field2 * field3
	int order() default 999; // ordem de visualizacao da coluna na horizontal
	boolean editable() default false; // coluna editavel no paginator
	boolean unique() default false; // valor da coluna é unica no paginator
	boolean totalizer() default false; // totalizado de uma coluna, visualizado no footer do paginator
	String label() default ""; // valor da label que será mostrada quando totalizer for true
	TypeTemplate[] template() default { TypeTemplate.ROW };
}