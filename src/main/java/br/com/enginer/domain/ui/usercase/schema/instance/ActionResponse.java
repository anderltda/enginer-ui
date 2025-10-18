package br.com.enginer.domain.ui.usercase.schema.instance;

/**
 * 
 */
public class ActionResponse {

	private ActionResponseSuccess success;
	private ActionResponseError error;

	public ActionResponseSuccess getSuccess() {
		return success;
	}

	public void setSuccess(ActionResponseSuccess success) {
		this.success = success;
	}

	public ActionResponseError getError() {
		return error;
	}

	public void setError(ActionResponseError error) {
		this.error = error;
	}
}
