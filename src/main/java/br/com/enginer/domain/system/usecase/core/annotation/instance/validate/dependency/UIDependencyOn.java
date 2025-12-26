package br.com.enginer.domain.system.usecase.core.annotation.instance.validate.dependency;

import java.lang.annotation.*;

import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(UIDependency.class)
public @interface UIDependencyOn {
	String label();
	String field();
	String[] depends();
	TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.TAB };
}