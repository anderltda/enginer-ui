package br.com.enginer.domain.system.usecase.core.schema.field;

import br.com.enginer.domain.system.usecase.core.schema.field.behavior.Blank;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Area;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Attachment;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Checkbox;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Date;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Decimal;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Email;
import br.com.enginer.domain.system.usecase.core.schema.field.type.File;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Filter;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Hidden;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Join;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Number;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Password;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Radio;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Select;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Tag;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Text;
import br.com.enginer.domain.system.usecase.core.schema.field.type.Time;
import br.com.enginer.domain.system.usecase.core.schema.instance.Button;

/**
 * 
 */
public class Field {

	private Hidden hidden;
	private Text text;
	private Email email;
	private Area textarea;
	private Number number;
	private Decimal decimal;
	private Password password;
	private Date date;
	private Time time;
	private Radio radio;
	private Checkbox checkbox;
	private Select select;
	private Tag tag;
	private File file;
	private Attachment attachment;
	private Filter filter;
	private Join join;
	private Button button;
	private Blank blank;

	/**
	 * @return the hidden
	 */
	public Hidden getHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(Hidden hidden) {
		this.hidden = hidden;
	}

	/**
	 * @return the text
	 */
	public Text getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(Text text) {
		this.text = text;
	}

	/**
	 * @return the email
	 */
	public Email getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(Email email) {
		this.email = email;
	}

	/**
	 * @return the textarea
	 */
	public Area getTextarea() {
		return textarea;
	}

	/**
	 * @param textarea the textarea to set
	 */
	public void setTextarea(Area textarea) {
		this.textarea = textarea;
	}

	/**
	 * @return the number
	 */
	public Number getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Number number) {
		this.number = number;
	}

	/**
	 * @return the decimal
	 */
	public Decimal getDecimal() {
		return decimal;
	}

	/**
	 * @param decimal the decimal to set
	 */
	public void setDecimal(Decimal decimal) {
		this.decimal = decimal;
	}

	/**
	 * @return the password
	 */
	public Password getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(Password password) {
		this.password = password;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the time
	 */
	public Time getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Time time) {
		this.time = time;
	}

	/**
	 * @return the radio
	 */
	public Radio getRadio() {
		return radio;
	}

	/**
	 * @param radio the radio to set
	 */
	public void setRadio(Radio radio) {
		this.radio = radio;
	}

	/**
	 * @return the checkbox
	 */
	public Checkbox getCheckbox() {
		return checkbox;
	}

	/**
	 * @param checkbox the checkbox to set
	 */
	public void setCheckbox(Checkbox checkbox) {
		this.checkbox = checkbox;
	}

	/**
	 * @return the select
	 */
	public Select getSelect() {
		return select;
	}

	/**
	 * @param select the select to set
	 */
	public void setSelect(Select select) {
		this.select = select;
	}

	/**
	 * @return the tag
	 */
	public Tag getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(Tag tag) {
		this.tag = tag;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the attachment
	 */
	public Attachment getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return the filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @param filter the filter to set
	 */
	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	/**
	 * @return the join
	 */
	public Join getJoin() {
		return join;
	}

	/**
	 * @param join the join to set
	 */
	public void setJoin(Join join) {
		this.join = join;
	}

	/**
	 * @return the button
	 */
	public Button getButton() {
		return button;
	}

	/**
	 * @param button the button to set
	 */
	public void setButton(Button button) {
		this.button = button;
	}

	/**
	 * @return the blank
	 */
	public Blank getBlank() {
		return blank;
	}

	/**
	 * @param blank the blank to set
	 */
	public void setBlank(Blank blank) {
		this.blank = blank;
	}

}
