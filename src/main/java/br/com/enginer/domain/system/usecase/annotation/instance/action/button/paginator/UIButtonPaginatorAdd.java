package br.com.enginer.domain.system.usecase.annotation.instance.action.button.paginator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.ActionUseCase;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIActionTriggerMethod;
import br.com.enginer.domain.system.usecase.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.constants.Constants;
import br.com.enginer.domain.system.usecase.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(	  
	  label = Constants.LABEL_ADD, 
	  icon = "plus", 
	  state = TypeButtonState.BTN_STATE_PRIMARY, 
	  template = { TypeTemplate.ROW },
	  needsValidation = true, 
	  action = @UIAction(
			method = @UIActionMethod(
				clientMethod = "triggerMethod", 
				trigger = @UIActionTriggerMethod(serverMethod = ActionUseCase.plus)
			),
			response = @UIActionResponse(
				template = { TypeTemplate.ROW }, 
				success = @UIActionResponseSuccess(method = @UIActionMethod(clientMethod = "setDataSetField"))
			)
	  )
)
public @interface UIButtonPaginatorAdd { }
