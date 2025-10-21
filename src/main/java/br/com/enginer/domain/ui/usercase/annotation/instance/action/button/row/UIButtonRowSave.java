package br.com.enginer.domain.ui.usercase.annotation.instance.action.button.row;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponseError;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_SAVE,
    icon = "save",
    state = TypeButtonState.BTN_STATE_PRIMARY,
    template = TypeTemplate.ROW,
    action = @UIAction(
        method = @UIActionMethod(serverMethod = "salvar"),
        response = @UIActionResponse(
        	template = { TypeTemplate.FORM },
    		error = @UIActionResponseError(method = @UIActionMethod(clientMethod = "onAlertTestError")), 
    		success = @UIActionResponseSuccess(redirect = @UIActionRedirect(value = Constants.PATH, ui = "row", domain = "entityEleven", param = "{ disable=true, field=entityTen, value=$object }"))
        )
    )
)
public @interface UIButtonRowSave {}
