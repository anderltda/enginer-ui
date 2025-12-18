package br.com.enginer.domain.system.usecase.annotation.instance.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(UIButtonAction.class)
public @interface UIButton {
	String label();
	String icon() default "";
	boolean disable() default false;
	boolean highlight() default false;
	boolean dropdown() default false;
	boolean rowInlineButton() default false;
	boolean confirm() default false;
	boolean needsValidation() default true;
	String[] notDomain() default {};
	TypeTemplate[] template() default { TypeTemplate.FILTER, TypeTemplate.ROW, TypeTemplate.FORM, TypeTemplate.TAB, TypeTemplate.MODAL };
	TypeButtonState state() default TypeButtonState.BTN_STATE_DEFAULT; 
	UIAction action() default @UIAction;
}
