package br.com.enginer.domain.system.usercase.annotation.instance.action.button.form;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usercase.constants.Constants;
import br.com.enginer.domain.system.usercase.enums.TypeButtonState;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

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
