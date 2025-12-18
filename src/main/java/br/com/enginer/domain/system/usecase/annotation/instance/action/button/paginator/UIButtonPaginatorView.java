package br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionRedirect;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(
	label = Constants.LABEL_ACTION_VIEW, 
	template = { TypeTemplate.PAGINATOR },
	highlight = false,
	dropdown = true,
	action = @UIAction(redirect = @UIActionRedirect(value = Constants.PATH_FIND_BY_ID, param = "{ disableAll=true }"))
)
public @interface UIButtonPaginatorView {}
