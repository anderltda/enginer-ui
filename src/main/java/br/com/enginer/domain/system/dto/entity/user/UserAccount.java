package br.com.enginer.domain.system.dto.entity.user;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import br.com.enginer.domain.system.dto.entity.upload.UploadFile;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIFile;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIHidden;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIIgnore;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UIHeader;
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
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.paginator.UIButtonPaginatorView;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIConfig;
import br.com.enginer.domain.system.usecase.core.annotation.instance.paginator.UIPaginator;
import br.com.enginer.domain.system.usecase.core.enums.TypeFileUpload;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

@UIHeader(title = "User-Account")
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
	}
))
public class UserAccount extends DomainAbstract<Long> {

	@UIHidden
	private Long id;

	@UIPosition(x = 1, y = 2)
	private UUID publicId;

	@UIPosition(x = 1, y = 3)
	private String username;

	@UIPosition(x = 2, y = 3)
	private String displayName;

	@UIPosition(x = 1, y = 4)
	private String email;

	@UIPosition(x = 2, y = 4)
	private String emailNormalized;

	@UIHidden
	private Long avatarFileId;

	@UIPosition(x = 2, y = 1)
	private Boolean active;

	private Instant createdAt;

	private Instant updatedAt;

	@UIIgnore
	private UploadFile uploadFile;

	@UIPosition(x = 1, y = 1)
	@UIFieldValidation(required = false)
	@UIFile(label = "", mode = TypeFileUpload.WALL_PICKER, listType = "picture-card", limit = 1)
	private transient List<UploadFile> files;

	/**
	 * 
	 */
	public UserAccount() {
		super();
	}

	/**
	 * @param id
	 */
	public UserAccount(Long id) {
		super();
		this.id = id;
	}

	/**
	 *
	 */
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

	/**
	 * @return the id
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the publicId
	 */
	public UUID getPublicId() {
		return publicId;
	}

	/**
	 * @param publicId the publicId to set
	 */
	public void setPublicId(UUID publicId) {
		this.publicId = publicId;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the emailNormalized
	 */
	public String getEmailNormalized() {
		return emailNormalized;
	}

	/**
	 * @param emailNormalized the emailNormalized to set
	 */
	public void setEmailNormalized(String emailNormalized) {
		this.emailNormalized = emailNormalized;
	}

	/**
	 * @return the avatarFileId
	 */
	public Long getAvatarFileId() {
		return avatarFileId;
	}

	/**
	 * @param avatarFileId the avatarFileId to set
	 */
	public void setAvatarFileId(Long avatarFileId) {
		if(this.files == null) {
			this.files = new ArrayList<>();
		}
		this.uploadFile = new UploadFile();
		this.uploadFile.setId(avatarFileId);
		this.files.add(uploadFile);
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the createdAt
	 */
	public Instant getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public Instant getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the uploadFile
	 */
	public UploadFile getUploadFile() {
		return uploadFile;
	}

	/**
	 * @param uploadFile the uploadFile to set
	 */
	public void setUploadFile(UploadFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	/**
	 * @return the files
	 */
	public List<UploadFile> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(List<UploadFile> files) {
		this.files = files;
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
		UserAccount other = (UserAccount) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "UserAccount [id=" + id + ", publicId=" + publicId + ", displayName=" + displayName + ", username="
				+ username + ", email=" + email + ", emailNormalized=" + emailNormalized + ", avatarFileId="
				+ avatarFileId + ", active=" + active + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
				+ ", uploadFile=" + uploadFile + "]";
	}

}
