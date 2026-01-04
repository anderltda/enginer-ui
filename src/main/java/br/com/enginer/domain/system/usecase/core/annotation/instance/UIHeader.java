package br.com.enginer.domain.system.usecase.core.annotation.instance;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UIHeader {
	String title();
	UIConditional conditional() default @UIConditional;
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.FORM, TypeTemplate.TAB};
}


