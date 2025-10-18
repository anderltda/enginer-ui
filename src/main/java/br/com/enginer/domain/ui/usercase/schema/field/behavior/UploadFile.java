package br.com.enginer.domain.ui.usercase.schema.field.behavior;

/**
 * 
 */
public class UploadFile {

	private String uid;
	private String name;
	private String status;
	private String url;
	private UploadResponse response;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UploadResponse getResponse() {
		return response;
	}

	public void setResponse(UploadResponse response) {
		this.response = response;
	}

}
