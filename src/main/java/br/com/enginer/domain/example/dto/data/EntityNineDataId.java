package br.com.enginer.domain.example.dto.data;

import br.com.enginer.domain.system.usecase.core.annotation.field.UIIdPart;

public class EntityNineDataId {

	@UIIdPart
	private Long idEntityEight;

	@UIIdPart
	private Long idEntitySeven;

	@UIIdPart
	private Long idEntitySix;

	public Long getIdEntityEight() {
		return this.idEntityEight;
	}

	public void setIdEntityEight(Long idEntityEight) {
		this.idEntityEight = idEntityEight;
	}

	public Long getIdEntitySeven() {
		return this.idEntitySeven;
	}

	public void setIdEntitySeven(Long idEntitySeven) {
		this.idEntitySeven = idEntitySeven;
	}

	public Long getIdEntitySix() {
		return this.idEntitySix;
	}

	public void setIdEntitySix(Long idEntitySix) {
		this.idEntitySix = idEntitySix;
	}
}
