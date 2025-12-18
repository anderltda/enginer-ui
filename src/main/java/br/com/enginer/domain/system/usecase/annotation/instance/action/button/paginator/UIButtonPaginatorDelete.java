package br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_DELETE,
    confirm = true,
    dropdown = true,
    highlight = true,
    needsValidation = false,
    state = TypeButtonState.BTN_STATE_DANGER,
    template = TypeTemplate.PAGINATOR,
    action = @UIAction(
		method = @UIActionMethod(serverMethod = "excluirLista"),
        response = @UIActionResponse(
        	template = TypeTemplate.PAGINATOR,
    		error = @UIActionResponseError(template = TypeTemplate.PAGINATOR, method = @UIActionMethod(clientMethod = "callbackError")), 
    		success = @UIActionResponseSuccess(template = TypeTemplate.PAGINATOR, method = @UIActionMethod(clientMethod = "callbackSuccess"))
        )
    )
)
public @interface UIButtonPaginatorDelete {}
