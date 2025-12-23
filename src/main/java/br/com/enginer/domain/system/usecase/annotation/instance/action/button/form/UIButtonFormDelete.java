package br.com.enginer.domain.system.usecase.annotation.instance.action.button.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_DELETE,
    icon = "close_ circle",
    state = TypeButtonState.BTN_STATE_DANGER,
    confirm = true,
    needsValidation = false,
    highlight = true,
    dropdown = true,
    template = { TypeTemplate.FORM },
	conditional = @UIConditional({
		@UIConditionalOn(field = "disabled", operator = TypeOperator.EQUALS, matchs = { "false" }),
		@UIConditionalOn(field = "id", operator = TypeOperator.IS_NOT_NULL),
	}),	
    action = @UIAction(
    		method = @UIActionMethod(serverMethod = "excluir"),
            response = @UIActionResponse(
            	template = { TypeTemplate.FORM },
        		error = @UIActionResponseError(method = @UIActionMethod(clientMethod = "onAlertTestError")), 
        		success = @UIActionResponseSuccess(redirect = @UIActionRedirect(value = Constants.PATH, ui = "filter"))
            )
        )
)
public @interface UIButtonFormDelete {}
