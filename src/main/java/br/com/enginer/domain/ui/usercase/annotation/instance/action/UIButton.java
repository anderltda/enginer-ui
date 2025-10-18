package br.com.enginer.domain.ui.usercase.annotation.instance.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(UIButtonAction.class)
public @interface UIButton {
	String label();
	String icon() default "";
	boolean disabled() default false;
	boolean highlight() default false;
	boolean dropdown() default false;
	boolean confirm() default false;
	boolean needsValidation() default true;
	String[] notDomain() default {};
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL };
	TypeButtonState state() default TypeButtonState.BTN_STATE_DEFAULT; 
	UIAction action() default @UIAction;
}
