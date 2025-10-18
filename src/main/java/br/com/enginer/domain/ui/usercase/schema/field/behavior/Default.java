package br.com.enginer.domain.ui.usercase.schema.field.behavior;

import java.util.List;

import br.com.enginer.domain.ui.usercase.enums.TypeDateFormat;
import br.com.enginer.domain.ui.usercase.enums.TypeFileUpload;
import br.com.enginer.domain.ui.usercase.enums.TypeLayoutTarget;
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
import br.com.enginer.domain.ui.usercase.utils.ReflectionUtils;
import br.com.enginer.domain.ui.usercase.utils.StringsUtils;

/**
 * 
 */
public class Default {

	private String label;
	private String field;
	private Object value;
	private Position position;
	private Boolean disable;

	public Default(Integer xposition, Integer yposition, String name, Boolean disable, Object object) {
		this.position = new Position(xposition, yposition);
		this.label = StringsUtils.normalizeLabelToLowercaseCamelization(name);
		this.field = StringsUtils.normalizeToCamelCaseFromPascalCase(name);
		this.disable = disable;
		this.value = ReflectionUtils.get(StringsUtils.getMethod(name), object);
	}

	/**
	 * @param value
	 * @return
	 */
	public Hidden getHidden() {
		Hidden hidden = new Hidden();
		hidden.setField(label);
		hidden.setField(field);
		hidden.setValue(value);
		hidden.setPosition(new Position(0, 0));
		return hidden;
	}

	public Text getText() {
		Text text = new Text();
		text.setLabel(label);
		text.setField(field);
		text.setValue(value);
		text.setMin(1);
		text.setMax(20);
		text.setPosition(position);
		text.setDisable(disable);
		return text;
	}

	public Email getEmail() {
		Email email = new Email();
		email.setLabel(label);
		email.setField(field);
		email.setValue(value);
		email.setMin(1);
		email.setMax(50);
		email.setPosition(position);
		email.setDisable(disable);
		return email;
	}

	public Number getNumber() {
		Number number = new Number();
		number.setLabel(label);
		number.setField(field);
		number.setValue(value);
		number.setMin(1);
		number.setMax(50);
		number.setPosition(position);
		number.setDisable(disable);
		return number;
	}

	public Decimal getDecimal() {
		Decimal decimal = new Decimal();
		decimal.setLabel(label);
		decimal.setField(field);
		decimal.setValue(value);
		decimal.setPosition(position);
		decimal.setDisable(disable);
		return decimal;
	}

	public Password getPassword() {
		Password password = new Password();
		password.setLabel(label);
		password.setField(field);
		password.setMin(1);
		password.setMax(10);
		password.setPosition(position);
		password.setDisable(disable);
		return password;
	}

	public Date getDate(boolean showTime) {
		Date date = new Date();
		date.setLabel(label);
		date.setField(field);
		date.setValue(value);
		date.setShowtime(showTime);
		date.setFormat(showTime ? TypeDateFormat.DATE_TIME_FORMAT : TypeDateFormat.DATE_FORMAT);
		date.setPosition(position);
		date.setDisable(disable);
		return date;
	}

	public Time getTime() {
		Time time = new Time();
		time.setLabel(label);
		time.setField(field);
		time.setValue(value);
		time.setPosition(position);
		time.setDisable(disable);
		return time;
	}

	public Radio getRadio(List<Object> options) {
		Radio radio = new Radio();
		radio.setLabel(label);
		radio.setField(field);
		radio.setValue(value);
		radio.setOptions(options);
		radio.setPosition(position);
		radio.setDisable(disable);
		return radio;
	}

	public Checkbox getCheckbox() {
		Checkbox checkbox = new Checkbox();
		checkbox.setLabel(label);
		checkbox.setField(field);
		checkbox.setValue(value);
		checkbox.setPosition(position);
		checkbox.setDisable(disable);
		return checkbox;
	}

	public Select getSelect(List<Object> options) {
		Select select = new Select();
		select.setLabel(label);
		select.setField(field);
		select.setValue(value);
		select.setPosition(position);
		select.setOptions(options);
		select.setDisable(disable);
		return select;
	}

	public Tag getTag() {
		Tag tag = new Tag();
		tag.setLabel(label);
		tag.setField(field);
		tag.setValue(value);
		tag.setPosition(position);
		tag.setDisable(disable);
		return tag;
	}

	public Area getTextarea() {
		Area textarea = new Area();
		textarea.setLabel(label);
		textarea.setField(field);
		textarea.setValue(value);
		textarea.setEditor(false);
		textarea.setPosition(position);
		textarea.setDisable(disable);
		return textarea;
	}

	public File getFile(List<UploadFile> files) {
		File file = new File();
		file.setLabel(label);
		file.setField(field);
		file.setValue(value);
		file.setAction("http://localhost:8081/api/upload");
		file.setMode(TypeFileUpload.SIMPLE);
		file.setFiles(files);
		file.setPosition(position);
		file.setDisable(disable);
		return file;
	}

	public Filter getFilter(Class<?> domain) {
		Filter filter = new Filter();
		filter.setLabel(label);
		filter.setValue(value);
		filter.setDomainClass(domain);
		filter.setDomain(StringsUtils.firstLower(domain.getSimpleName()));
		filter.setPosition(position);
		filter.setDisable(disable);
		return filter;
	}

	public Join getJoin(Class<?> domain) {
		Join join = new Join();
		join.setLabel(label);
		join.setDomainClass(domain);
		join.setDomain(StringsUtils.firstLower(domain.getSimpleName()));
		join.setLayoutTarget(TypeLayoutTarget.form);
		return join;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
