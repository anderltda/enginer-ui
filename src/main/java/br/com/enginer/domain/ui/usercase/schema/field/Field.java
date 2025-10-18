package br.com.enginer.domain.ui.usercase.schema.field;

import br.com.enginer.domain.ui.usercase.schema.field.behavior.Blank;
import br.com.enginer.domain.ui.usercase.schema.field.type.Area;
import br.com.enginer.domain.ui.usercase.schema.field.type.Checkbox;
import br.com.enginer.domain.ui.usercase.schema.field.type.Date;
import br.com.enginer.domain.ui.usercase.schema.field.type.Decimal;
import br.com.enginer.domain.ui.usercase.schema.field.type.Email;
import br.com.enginer.domain.ui.usercase.schema.field.type.File;
import br.com.enginer.domain.ui.usercase.schema.field.type.Filter;
import br.com.enginer.domain.ui.usercase.schema.field.type.Hidden;
import br.com.enginer.domain.ui.usercase.schema.field.type.Join;
import br.com.enginer.domain.ui.usercase.schema.field.type.Number;
import br.com.enginer.domain.ui.usercase.schema.field.type.Password;
import br.com.enginer.domain.ui.usercase.schema.field.type.Radio;
import br.com.enginer.domain.ui.usercase.schema.field.type.Select;
import br.com.enginer.domain.ui.usercase.schema.field.type.Tag;
import br.com.enginer.domain.ui.usercase.schema.field.type.Text;
import br.com.enginer.domain.ui.usercase.schema.field.type.Time;
import br.com.enginer.domain.ui.usercase.schema.instance.Button;

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
	private Filter filter;
	private Join join;
	private Button button;
	private Blank blank;

	public Hidden getHidden() {
		return hidden;
	}

	public void setHidden(Hidden hidden) {
		this.hidden = hidden;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Number getNumber() {
		return number;
	}

	public void setNumber(Number number) {
		this.number = number;
	}

	public Decimal getDecimal() {
		return decimal;
	}

	public void setDecimal(Decimal decimal) {
		this.decimal = decimal;
	}

	public Password getPassword() {
		return password;
	}

	public void setPassword(Password password) {
		this.password = password;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public Radio getRadio() {
		return radio;
	}

	public void setRadio(Radio radio) {
		this.radio = radio;
	}

	public Checkbox getCheckbox() {
		return checkbox;
	}

	public void setCheckbox(Checkbox checkbox) {
		this.checkbox = checkbox;
	}

	public Select getSelect() {
		return select;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public Area getTextarea() {
		return textarea;
	}

	public void setTextarea(Area textarea) {
		this.textarea = textarea;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public Join getJoin() {
		return join;
	}

	public void setJoin(Join join) {
		this.join = join;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public Blank getBlank() {
		return blank;
	}

	public void setBlank(Blank blank) {
		this.blank = blank;
	}
}
