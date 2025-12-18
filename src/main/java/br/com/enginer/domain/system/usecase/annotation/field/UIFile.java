package br.com.enginer.domain.system.usecase.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.enums.TypeFileUpload;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIFile {
	String label() default "";
	String title() default "";
	String action() default "http://localhost:8081/v1/enginer-ui/action/upload";
	TypeFileUpload mode();
	String listType() default "picture"; // picture-card | picture | text
	int limit() default 3;
	boolean disable() default false;
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.MODAL };
}

//text – lista simples com ícone/link.
//picture – miniaturas alinhadas em lista.
//picture-card – grade de cards com preview.