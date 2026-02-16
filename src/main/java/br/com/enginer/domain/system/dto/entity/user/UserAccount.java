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
import br.com.enginer.domain.system.usecase.core.annotation.field.UIText;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.UIPosition;
import br.com.enginer.domain.system.usecase.core.annotation.field.behavior.validation.UIFieldValidation;
import br.com.enginer.domain.system.usecase.core.annotation.instance.UIHeader;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.UIButtonAction;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.UIButtonClear;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterFormNew;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterSearch;
import br.com.enginer.domain.system.usecase.core.annotation.instance.action.button.filter.UIButtonFilterTabNew;
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
	// FILTER
	UIButtonFilterTabNew.class, 
	UIButtonFilterFormNew.class, 
	UIButtonFilterSearch.class, 
	// FORM
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

	@UIText(label = "Public ID", disable = true)
	@UIPosition(x = 1, y = 2)
	private UUID publicId;

	@UIText(label = "Username", disable = true)
	@UIPosition(x = 2, y = 2)
	private String username;

	@UIPosition(x = 1, y = 3)
	private String firstName;

	@UIPosition(x = 2, y = 3)
	private String lastName;

	@UIPosition(x = 1, y = 4)
	private String email;

	@UIHidden
	private String emailNormalized;
	
	@UIIgnore
	private UploadFile uploadFile;
	
	@UIHidden
	private Instant lastLoginAt;

	@UIIgnore
	private String lastLoginIp;

	@UIIgnore
	private String lastLoginUserAgent;

	@UIPosition(x = 2, y = 1)
	private Boolean active;
	
	@UIPosition(x = 1, y = 1)
	@UIFieldValidation(required = false)
	@UIFile(label = "", mode = TypeFileUpload.WALL_PICKER, listType = "picture-card", limit = 1)
	private transient List<UploadFile> files;

	@UIIgnore
	private transient String displayName;

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
		displayName = firstName +" "+ lastName;
		return displayName;
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
	 * @param idUploadFile the idUploadFile to set
	 */
	public void setIdUploadFile(Long idUploadFile) {
		if(idUploadFile == null) return;
		if(this.files == null) {
			this.files = new ArrayList<>();
		}
		this.uploadFile = new UploadFile();
		this.uploadFile.setId(idUploadFile);
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
	
	/**
	 * @return the lastLoginAt
	 */
	public Instant getLastLoginAt() {
		return lastLoginAt;
	}

	/**
	 * @param lastLoginAt the lastLoginAt to set
	 */
	public void setLastLoginAt(Instant lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	/**
	 * @return the lastLoginIp
	 */
	public String getLastLoginIp() {
		return lastLoginIp;
	}

	/**
	 * @param lastLoginIp the lastLoginIp to set
	 */
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	/**
	 * @return the lastLoginUserAgent
	 */
	public String getLastLoginUserAgent() {
		return lastLoginUserAgent;
	}

	/**
	 * @param lastLoginUserAgent the lastLoginUserAgent to set
	 */
	public void setLastLoginUserAgent(String lastLoginUserAgent) {
		this.lastLoginUserAgent = lastLoginUserAgent;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
		return "UserAccount [id=" + id + ", publicId=" + publicId + ", username=" + username + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", emailNormalized=" + emailNormalized
				+ ", uploadFile=" + uploadFile + ", lastLoginAt=" + lastLoginAt + ", lastLoginIp=" + lastLoginIp
				+ ", lastLoginUserAgent=" + lastLoginUserAgent + ", active=" + active + "]";
	}
	
}
