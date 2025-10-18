package br.com.enginer.domain.ui.usercase.annotation.instance.action.button.filter;

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
    icon = "menu_add",
    needsValidation = false,
    template = TypeTemplate.FILTER,
	action = @UIAction(
		redirect = @UIActionRedirect(value = Constants.PATH, ui = "tab", param = "{ disabled=false }")
	)
)
public @interface UIButtonFilterTabNew {}