package br.com.enginer.domain.system.usecase.core.annotation.instance.validate.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UICustom {
	UICustomOn[] value() default {};
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.TAB };
}
