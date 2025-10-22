package br.com.enginer.domain.example.dto.entity;

import java.time.LocalDateTime;

import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;

public class File extends DomainAbstract<Long> {
	
	private Long id;

	private String fileName;

	private String fileType;

	private Long fileSize;

	private String filePath;

	private String storageType;

	private String checksumSha256;

	private String domain;

	private String entityId;

	private Long ownerId;

	private Boolean isPublic;

	private LocalDateTime createdAt;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getEntityId() {
		return this.entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		File that = (File) o;
		return java.util.Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id);
	}

	@Override
	public String toString() {
		return "File{" + "id=" + (id != null ? id.toString() : "null") + ", " + "fileName="
				+ (fileName != null ? fileName.toString() : "null") + ", " + "fileType="
				+ (fileType != null ? fileType.toString() : "null") + ", " + "fileSize="
				+ (fileSize != null ? fileSize.toString() : "null") + ", " + "filePath="
				+ (filePath != null ? filePath.toString() : "null") + ", " + "storageType="
				+ (storageType != null ? storageType.toString() : "null") + ", " + "checksumSha256="
				+ (checksumSha256 != null ? checksumSha256.toString() : "null") + ", " + "domain="
				+ (domain != null ? domain.toString() : "null") + ", " + "entityId="
				+ (entityId != null ? entityId.toString() : "null") + ", " + "ownerId="
				+ (ownerId != null ? ownerId.toString() : "null") + ", " + "isPublic="
				+ (isPublic != null ? isPublic.toString() : "null") + ", " + "createdAt="
				+ (createdAt != null ? createdAt.toString() : "null") + '}';
	}
}
