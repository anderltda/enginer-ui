package br.com.enginer.domain.ui.usercase.annotation.instance.validate.conditional;

import java.lang.annotation.*;

import br.com.enginer.domain.ui.usercase.enums.TypeOperator;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(UIConditional.class)
public @interface UIConditionalOn {
	String label() default "";
	String field();
	TypeOperator operator();
	String[] matchs() default{};
	String value() default "";
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.TAB };
}