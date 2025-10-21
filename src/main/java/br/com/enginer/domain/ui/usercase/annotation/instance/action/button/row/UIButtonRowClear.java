package br.com.enginer.domain.ui.usercase.annotation.instance.action.button.row;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_CLEAR,
    icon = "bin_alt",
    needsValidation = false,
    template = TypeTemplate.ROW,
    state = TypeButtonState.BTN_STATE_DEFAULT,
    action = @UIAction(
        method = @UIActionMethod(clientMethod = Constants.METHOD_CLEAR_FORM)
    )
)
public @interface UIButtonRowClear {}