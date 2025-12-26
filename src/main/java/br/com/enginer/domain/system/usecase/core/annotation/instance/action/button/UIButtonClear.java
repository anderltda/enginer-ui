package br.com.enginer.domain.system.usecase.core.annotation.instance.action.button;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditional;
import br.com.enginer.domain.system.usecase.core.annotation.instance.validate.conditional.UIConditionalOn;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.core.enums.TypeOperator;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
    label = Constants.LABEL_CLEAR,
    icon = "bin_alt",
    needsValidation = false,
    state = TypeButtonState.BTN_STATE_DEFAULT,
    template = { TypeTemplate.FILTER },
	conditional = @UIConditional({
		@UIConditionalOn(field = "disabled", operator = TypeOperator.EQUALS, matchs = { "false" })
	}),    
    action = @UIAction(
        method = @UIActionMethod(clientMethod = Constants.METHOD_CLEAR_FORM)
    )
)
public @interface UIButtonClear {}