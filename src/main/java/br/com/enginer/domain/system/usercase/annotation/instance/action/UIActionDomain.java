package br.com.enginer.domain.system.usercase.annotation.instance.action;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface UIActionDomain {
	String object() default "";
	String param() default "";
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.PAGINATOR, TypeTemplate.MODAL };
}
