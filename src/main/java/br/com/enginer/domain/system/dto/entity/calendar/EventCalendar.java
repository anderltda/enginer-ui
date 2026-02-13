package br.com.enginer.domain.system.dto.entity.calendar;

import java.time.Instant;
import java.time.LocalDateTime;

import br.com.enginer.domain.system.dto.entity.user.UserAccount;
import br.com.enginer.domain.system.usecase.core.enums.TypeEvent;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;

/**
 * 
 */
public class EventCalendar extends DomainAbstract<Long> {

	private Long id;
	private String title;
	private String style;
	private UserAccount userAccount;
	private Long idUserAccount;
	private TypeEvent type;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private Boolean allDay;
	private Boolean readOnly;
	private String location;
	private String code;
	private String note;
	private Instant createdAt;
	private Instant updatedAt;
	private transient Other other;

	/**
	 * 
	 */
	public EventCalendar() {
		super();
	}

	/**
	 * @param id
	 */
	public EventCalendar(Long id) {
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

	/**
	 * @return the id
	 */
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * @return the userAccount
	 */
	public UserAccount getUserAccount() {
		return userAccount;
	}
	
	/**
	 * @param userAccount the userAccount to set
	 */
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	
	/**
	 * @param idUserAccount the idUserAccount to set
	 */
	public void setIdUserAccount(Long idUserAccount) {
		this.userAccount = new UserAccount(idUserAccount);
	}

	/**
	 * @return the type
	 */
	public TypeEvent getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TypeEvent type) {
		this.type = type;
	}

	/**
	 * @return the startDateTime
	 */
	public LocalDateTime getStartDateTime() {
		return startDateTime;
	}

	/**
	 * @param startDateTime the startDateTime to set
	 */
	public void setStartDateTime(LocalDateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	/**
	 * @return the endDateTime
	 */
	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	/**
	 * @param endDateTime the endDateTime to set
	 */
	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	/**
	 * @return the allDay
	 */
	public Boolean getAllDay() {
		return allDay;
	}

	/**
	 * @param allDay the allDay to set
	 */
	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}

	/**
	 * @return the readOnly
	 */
	public Boolean getReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
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
	 * @return the other
	 */
	public Other getOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(Other other) {
		this.other = other;
	}

	@Override
	public String toString() {
		return "EventCalendar [id=" + id + ", title=" + title + ", style=" + style + ", userAccount=" + userAccount
				+ ", idUserAccount=" + idUserAccount + ", type=" + type + ", startDateTime=" + startDateTime
				+ ", endDateTime=" + endDateTime + ", allDay=" + allDay + ", readOnly=" + readOnly + ", location="
				+ location + ", code=" + code + ", note=" + note + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
}
