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
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_SAVE,
    icon = "save",
    state = TypeButtonState.BTN_STATE_PRIMARY,
    template = { TypeTemplate.FORM, TypeTemplate.MODAL },
    action = @UIAction(
        method = @UIActionMethod(serverMethod = "salvar"),
        response = @UIActionResponse(
        	template = { TypeTemplate.FORM },
    		error = @UIActionResponseError(method = @UIActionMethod(clientMethod = "onAlertTestError")), 
    		success = @UIActionResponseSuccess(redirect = @UIActionRedirect(Constants.PATH_FIND_BY_ID))
        )
    )
)
public @interface UIButtonFormSave {}
