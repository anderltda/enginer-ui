package br.com.enginer.domain.ui.usercase.schema.instance;

/**
 * 
 */
public class Action {

	private String ui;
	private String domain;
	private String param;
	private String redirect;
	private String clientMethod;
	private String serverMethod;
	private ActionTrigger triggerMethod;
	private ActionObject actionObject;
	private ActionResponse response;
	private Boolean needsValidation;

	public String getUi() {
		return ui;
	}

	public void setUi(String ui) {
		this.ui = ui;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public String getClientMethod() {
		return clientMethod;
	}

	public void setClientMethod(String clientMethod) {
		this.clientMethod = clientMethod;
	}

	public String getServerMethod() {
		return serverMethod;
	}

	public void setServerMethod(String serverMethod) {
		this.serverMethod = serverMethod;
	}
	
	public ActionTrigger getTriggerMethod() {
		return triggerMethod;
	}

	public void setTriggerMethod(ActionTrigger triggerMethod) {
		this.triggerMethod = triggerMethod;
	}

	public ActionObject getActionObject() {
		return actionObject;
	}

	public void setActionObject(ActionObject actionObject) {
		this.actionObject = actionObject;
	}

	public ActionResponse getResponse() {
		return response;
	}

	public void setResponse(ActionResponse response) {
		this.response = response;
	}

	public Boolean getNeedsValidation() {
		return needsValidation;
	}

	public void setNeedsValidation(Boolean needsValidation) {
		this.needsValidation = needsValidation;
	}
}
