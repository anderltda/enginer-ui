package br.com.enginer.domain.ui.usercase.annotation.instance.paginator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButtonAction;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UIPaginator {
    UIConfig config() default @UIConfig(expandable = false, multiSelectable = false, deletable = false);
    UIButtonAction actions() default @UIButtonAction(value = {});
}
