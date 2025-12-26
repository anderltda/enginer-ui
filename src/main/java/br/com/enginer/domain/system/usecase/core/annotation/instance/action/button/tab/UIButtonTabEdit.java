package br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.tab;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.core.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
	label = Constants.LABEL_EDIT, 
	icon = "edit",
	needsValidation = false,
	state = TypeButtonState.BTN_STATE_PRIMARY,
	template = { TypeTemplate.TAB },
	conditional = @UIConditional({
		@UIConditionalOn(field = "disabled", operator = TypeOperator.EQUALS, matchs = { "true" }),
		@UIConditionalOn(field = "id", operator = TypeOperator.IS_NOT_NULL),
	}),
	action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH_FIND_BY_ID, ui = "tab", param = "{ disable=false, field=id }"))
)
public @interface UIButtonTabEdit {}



