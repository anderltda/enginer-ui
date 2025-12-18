package br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_CLEAR,
    icon = "bin_alt",
    needsValidation = false,
    template = { TypeTemplate.FILTER, TypeTemplate.MODAL },
    state = TypeButtonState.BTN_STATE_DEFAULT,
    action = @UIAction(
        method = @UIActionMethod(clientMethod = Constants.METHOD_CLEAR_FORM)
    )
)
public @interface UIButtonFilterClear {}