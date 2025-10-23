package br.com.enginer.domain.system.dto.entity;

import java.time.LocalDateTime;

import br.com.enginer.domain.system.usercase.schema.instance.DomainAbstract;

public class UploadFile extends DomainAbstract<Long> {

	private Long id;

	private String uid;

	private String status;

	private String name;

	private String type;

	private Long size;

	private String path;

	private String storageType;

	private String checksumSha256;

	private String domain;

	private String domainId;

	private Boolean isPublic;

	private LocalDateTime createdAt;

	private UploadResponse response;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStorageType() {
		return this.storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getChecksumSha256() {
		return this.checksumSha256;
	}

	public void setChecksumSha256(String checksumSha256) {
		this.checksumSha256 = checksumSha256;
	}

	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomainId() {
		return this.domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public Boolean getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public UploadResponse getResponse() {
		return response;
	}

	public void setResponse(UploadResponse response) {
		this.response = response;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
