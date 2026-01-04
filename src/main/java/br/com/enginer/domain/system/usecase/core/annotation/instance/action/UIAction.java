package br.com.enginer.domain.system.usecase.core.annotation.instance.action;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface UIAction {
    UIActionMethod method() default @UIActionMethod();
    UIActionRedirect redirect() default @UIActionRedirect();
	UIActionDomain actionObject() default @UIActionDomain;
	UIActionResponse response() default @UIActionResponse();
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.PAGINATOR, TypeTemplate.MODAL };
}
