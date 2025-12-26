package br.com.enginer.domain.system.usecase.core.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.enums.TypeLayoutTarget;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface UIJoin {
	String icon() default "";
	TypeLayoutTarget layoutTarget() default TypeLayoutTarget.form;
	TypeTemplate[] template() default { TypeTemplate.MODAL, TypeTemplate.TAB, TypeTemplate.ROW, TypeTemplate.FORM };
}