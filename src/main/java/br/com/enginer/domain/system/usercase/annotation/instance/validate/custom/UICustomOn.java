package br.com.enginer.domain.system.usercase.annotation.instance.validate.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(UICustom.class)
public @interface UICustomOn {
	String function();
	String message();
	String[] fields();
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.TAB };
}