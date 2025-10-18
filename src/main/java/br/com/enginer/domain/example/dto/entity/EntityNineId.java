package br.com.enginer.domain.example.dto.entity;

import java.util.UUID;

import br.com.enginer.domain.ui.usercase.annotation.field.UIColumn;
import br.com.enginer.domain.ui.usercase.annotation.field.UIFilter;
import br.com.enginer.domain.ui.usercase.annotation.field.UIIgnore;
import br.com.enginer.domain.ui.usercase.annotation.field.UIRow;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainAbstract;
import br.com.enginer.domain.ui.usercase.schema.instance.DomainId;

public class EntityNineId extends DomainAbstract<Object[]> implements DomainId {

	@UIIgnore
	private Long idEntityEight;

	@UIIgnore
	private UUID idEntitySeven;

	@UIIgnore
	private Long idEntitySix;
	
	@UIFilter(label = "Entity Seven", field = "dado", readonly = false)
	@UIRow(visible = true, fields = { "id", "dado" })
	@UIColumn(label = "Entity Seven", fields = { "id", "dado" }, initial = false)
	private EntitySeven entitySeven;

	@UIFilter(label = "Entity Eight", field = "properties", readonly = false)
	@UIRow(visible = true, fields = { "position" })
	@UIColumn(label = "Entity Eight", fields = { "position" }, initial = false)
	private EntityEight entityEight;
	
	public Long getIdEntityEight() {
		return this.idEntityEight;
	}

	public void setIdEntityEight(Long idEntityEight) {
		this.idEntityEight = idEntityEight;
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
	
	public EntityEight getEntityEight() {
		return entityEight;
	}

	public void setEntityEight(EntityEight entityEight) {
		this.entityEight = entityEight;
	}

	public EntitySeven getEntitySeven() {
		return entitySeven;
	}

	public void setEntitySeven(EntitySeven entitySeven) {
		this.entitySeven = entitySeven;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EntityNineId that = (EntityNineId) o;
		return java.util.Objects.equals(idEntitySeven, that.idEntitySeven)
				&& java.util.Objects.equals(idEntitySix, that.idEntitySix)
				&& java.util.Objects.equals(idEntityEight, that.idEntityEight);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idEntitySeven, idEntitySix, idEntityEight);
	}

	@Override
	public Object[] getId() {
		return null;
	}

	@Override
	public void setId(Object[] id) {
		// TODO Auto-generated method stub
	}
}
