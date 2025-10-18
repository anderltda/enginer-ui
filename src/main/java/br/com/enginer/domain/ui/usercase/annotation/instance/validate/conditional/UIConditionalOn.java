package br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional;

import java.lang.annotation.*;

import br.com.enginer.domain.ui.usercase.enums.TypeOperator;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(UIConditional.class)
public @interface UIConditionalOn {
	String label();
	String field();
	TypeOperator operator();
	String[] matchs();
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.TAB };
}