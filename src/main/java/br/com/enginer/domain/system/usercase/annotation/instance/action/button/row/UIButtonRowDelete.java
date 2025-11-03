package br.com.enginer.domain.system.usercase.annotation.instance.action.button.row;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usercase.constants.Constants;
import br.com.enginer.domain.system.usercase.enums.TypeButtonState;
import br.com.enginer.domain.system.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_DELETE,
    icon = "close_ circle",
    rowInlineButton = true,
    template = TypeTemplate.ROW,
    state = TypeButtonState.BTN_ICON_DANGER,
    action = @UIAction(method = @UIActionMethod(clientMethod = "onDeleteRow"))
)
public @interface UIButtonRowDelete {}
