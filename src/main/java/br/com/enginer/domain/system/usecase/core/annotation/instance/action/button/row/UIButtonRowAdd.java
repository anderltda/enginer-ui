package br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.row;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.enginer.domain.system.usecase.core.action.ActionUseCase;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionMethod;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponse;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIActionResponseSuccess;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButton;
import br.com.enginer.domain.system.usecase.core.constants.Constants;
import br.com.enginer.domain.system.usecase.core.enums.TypeButtonState;
import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@UIButton(	  
	  label = Constants.LABEL_ADD, 
	  icon = "plus", 
	  state = TypeButtonState.BTN_STATE_PRIMARY, 
	  template = { TypeTemplate.ROW }, 
	  needsValidation = true, 
	  action = @UIAction(
			method = @UIActionMethod(serverMethod = ActionUseCase.plus), 
			response = @UIActionResponse(template = { TypeTemplate.ROW }, success = @UIActionResponseSuccess(method = @UIActionMethod(clientMethod = "setDataSetField")))
	  )
)
public @interface UIButtonRowAdd { }
