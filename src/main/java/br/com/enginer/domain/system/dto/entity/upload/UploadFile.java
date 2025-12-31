package br.com.enginer.domain.system.dto.entity.upload;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

import br.com.enginer.domain.system.usecase.core.annotation.instance.UITitle;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonBack;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonClear;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterTabNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.form.UIButtonFormSave;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorDelete;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorEdit;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorSave;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

/**
 * Representa um arquivo enviado ao sistema, incluindo seus metadados e
 * informações de armazenamento.
 */
@UITitle("UploadFile")
@UIButtonAction(includes = {
	UIButtonClear.class, 
	UIButtonBack.class, 
	// FILTER
	UIButtonFilterTabNew.class, 
	UIButtonFilterFormNew.class, 
	UIButtonFilterSearch.class, 
	// FORM
	UIButtonFormDelete.class,
	UIButtonFormEdit.class, 
	UIButtonFormSave.class,
})
@UIPaginator(
	config = @UIConfig(expandable = false, multiSelectable = false),
	actions = @UIButtonAction(
	includes = { 
		UIButtonPaginatorView.class, 
		UIButtonPaginatorEdit.class, 
		UIButtonPaginatorDelete.class,
		UIButtonPaginatorSave.class,
	}
))
public class UploadFile extends DomainAbstract<Long> {

	private Long id;
	private String name;
	private String storageName;
	private String type;
	private Long size;
	private String path;
	private String storageType;
	private String checksumSha256;
	private String domain;
	private String domainId;
	private Boolean isPublic;
	private Instant createdAt;
	private transient String uid;
	private transient String status;
	private transient byte[] bytes;
	
	public UploadFile() {
		super();
	}

	public UploadFile(Long id) {
		super();
		this.id = id;
	}
	
	@Override
	public Boolean isIdNull() {
		Boolean isIdNull = false;
		try {
			isIdNull = (Boolean) ReflectionUtils.isIdNull(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return isIdNull;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getChecksumSha256() {
		return checksumSha256;
	}

	public void setChecksumSha256(String checksumSha256) {
		this.checksumSha256 = checksumSha256;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
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

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UploadFile other = (UploadFile) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "UploadFile [id=" + id + ", name=" + name + ", storageName=" + storageName + ", type=" + type + ", size="
				+ size + ", path=" + path + ", storageType=" + storageType + ", checksumSha256=" + checksumSha256
				+ ", domain=" + domain + ", domainId=" + domainId + ", isPublic=" + isPublic + ", createdAt="
				+ createdAt + ", uid=" + uid + ", status=" + status + ", bytes=" + Arrays.toString(bytes);
	}
}
