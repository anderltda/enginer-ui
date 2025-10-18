package br.com.enginer.domain.ui.usercase.annotation.instance.action;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface UIActionResponseError {
    UIActionMethod method() default @UIActionMethod();
    UIActionRedirect redirect() default @UIActionRedirect();
    TypeTemplate[] template() default { TypeTemplate.FORM, TypeTemplate.ROW, TypeTemplate.FILTER, TypeTemplate.TAB, TypeTemplate.MODAL };
}
