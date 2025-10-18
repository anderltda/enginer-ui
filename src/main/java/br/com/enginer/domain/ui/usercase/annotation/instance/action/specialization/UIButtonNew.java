package br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_NEW,
    icon = "add_circle",
    needsValidation = false,
    template = { TypeTemplate.FILTER, TypeTemplate.MODAL },
	action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH, param = "{ disabled=false }"))
)
public @interface UIButtonNew {}
