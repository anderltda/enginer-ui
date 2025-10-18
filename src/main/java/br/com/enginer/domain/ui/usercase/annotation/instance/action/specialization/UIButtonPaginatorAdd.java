package br.com.enginer.domain.ui.usercase.annotation.instance.action.specialization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.ActionUserCase;
import br.com.enginer.domain.Constants;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIAction;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIActionTriggerMethod;
import br.com.enginer.domain.ui.usercase.annotation.instance.action.UIButton;
import br.com.enginer.domain.ui.usercase.enums.TypeButtonState;
import br.com.enginer.domain.ui.usercase.enums.TypeTemplate;

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
				trigger = @UIActionTriggerMethod(serverMethod = ActionUserCase.plus)
			),
			response = @UIActionResponse(
				template = { TypeTemplate.ROW }, 
				success = @UIActionResponseSuccess(method = @UIActionMethod(clientMethod = "setDataSetField"))
			)
	  )
)
public @interface UIButtonPaginatorAdd { }
