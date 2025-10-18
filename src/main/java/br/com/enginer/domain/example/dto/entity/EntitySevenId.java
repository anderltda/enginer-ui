package br.com.enginer.domain.example.dto.entity;

import java.util.UUID;

import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIHidden;
import br.com.enginer.domain.ui.usercase.annotation.field.UIIgnore;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainId;

public class EntitySevenId extends DomainAbstract<Object[]> implements DomainId {
	
	@UIHidden
	@UIColumn(label = "Id EntitySeven", initial = false)
	private UUID idEntitySeven;

	@UIIgnore
	private Long idEntitySix;
	
	@UIFilter(label = "Entity Six", field = "packageName", readonly = false)
	@UIColumn(label = "Six", fields = { "id", "packageName", "startDate", "stopDate" }, initial = false)
	@UIRow(visible = true, fields = { "packageName" })
	private EntitySix entitySix;
	
	public EntitySevenId() {
		super();
	}

	public EntitySevenId(UUID idEntitySeven, Long idEntitySix) {
		super();
		this.idEntitySeven = idEntitySeven;
		this.idEntitySix = idEntitySix;
	}

	public EntitySix getEntitySix() {
		return entitySix;
	}

	public void setEntitySix(EntitySix entitySix) {
		this.entitySix = entitySix;
	}

	public UUID getIdEntitySeven() {
		return this.idEntitySeven;
	}

	public void setIdEntitySeven(UUID idEntitySeven) {
		this.idEntitySeven = idEntitySeven;
	}

	public Long getIdEntitySix() {
		return this.idEntitySix;
	}

	public void setIdEntitySix(Long idEntitySix) {
		this.idEntitySix = idEntitySix;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntitySevenId that = (EntitySevenId) o;
		return java.util.Objects.equals(idEntitySeven, that.idEntitySeven)
				&& java.util.Objects.equals(idEntitySix, that.idEntitySix);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntitySeven, idEntitySix);
	}
	
	@Override
	public Object[] getId() {
		//return new Object[] { idEntitySeven, idEntitySix };
		return null;
	}

	@Override
	public void setId(Object[] id) {
		// TODO Auto-generated method stub
	}
}
