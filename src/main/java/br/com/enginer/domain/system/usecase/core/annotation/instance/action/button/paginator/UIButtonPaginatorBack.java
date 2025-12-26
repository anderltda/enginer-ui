package br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator;

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
    label = Constants.LABEL_BACK,
    icon = "undo",
    needsValidation = false,
    template = TypeTemplate.ROW,
    state = TypeButtonState.BTN_STATE_DEFAULT,
    action = @UIAction(
        method = @UIActionMethod(clientMethod = "onBack")
    )
)
public @interface UIButtonPaginatorBack {}