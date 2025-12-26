package br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.tab;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_BEFORE,
    icon = "chevron_left",
    state = TypeButtonState.BTN_STATE_PRIMARY,
    needsValidation = false,
    template = { TypeTemplate.TAB, TypeTemplate.MODAL },
    action = @UIAction(
        method = @UIActionMethod(clientMethod = "onPrevious")
    )
)
public @interface UIButtonTabBefore {}
