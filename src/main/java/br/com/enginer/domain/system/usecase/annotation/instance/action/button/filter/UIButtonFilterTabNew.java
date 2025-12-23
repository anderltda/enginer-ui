package br.com.enginer.domain.system.usecase.annotation.instance.action.button.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_NEW,
    icon = "menu_add",
    needsValidation = false,
    template = { TypeTemplate.FILTER },
	conditional = @UIConditional({
		@UIConditionalOn(field = "modal", operator = TypeOperator.EQUALS, matchs = { "false" }),
	}),    
	action = @UIAction(
		redirect = @UIActionRedirect(value = Constants.PATH, ui = "tab", param = "{ disabled=false }")
	)
)
public @interface UIButtonFilterTabNew {}