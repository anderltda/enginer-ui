package br.com.enginer.domain.system.usercase.schema.field.type;

import br.com.enginer.domain.system.usercase.enums.TypeFormat;
import br.com.enginer.domain.system.usercase.schema.field.behavior.Base;

/**
 * 
 */
public class Decimal extends Base {

	private static final String TYPE = "decimal";

	private TypeFormat typeFormat;
	private Integer precision;

	public String getType() {
		return TYPE;
	}

	public TypeFormat getTypeFormat() {
		return typeFormat;
	}

	public void setTypeFormat(TypeFormat typeFormat) {
		this.typeFormat = typeFormat;
	}

	public Integer getPrecision() {
		return precision;
	}

	public void setPrecision(Integer precision) {
		this.precision = precision > 0 ? precision : null;
	}
	
	

}
