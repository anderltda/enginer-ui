package br.com.enginer.domain.ui.usercase.schema.instance;

/**
 * 
 */
public class ActionResponseSuccess {

	private String ui;
	private String domain;
	private String param;
	private String redirect;
	private String clientMethod;
	private String serverMethod;

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

}
