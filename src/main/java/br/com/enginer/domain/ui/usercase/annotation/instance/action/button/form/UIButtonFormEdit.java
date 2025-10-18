package br.com.enginer.domain.ui.usercase.annotation.instance.action.button.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
	label = Constants.LABEL_EDIT, 
	icon = "edit",
	needsValidation = false,
	state = TypeButtonState.BTN_STATE_PRIMARY,
	template = TypeTemplate.FORM,
	action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH_FIND_BY_ID, param = "{ disable=false, field=id }"))
)
public @interface UIButtonFormEdit {}



