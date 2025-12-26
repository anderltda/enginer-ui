package br.com.enginer.domain.example.dto.entity;

import java.util.UUID;

import br.com.enginer.domain.system.usecase.core.annotation.field.UIColumn;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIFilter;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIHidden;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIIdPart;
import br.com.enginer.domain.system.usecase.core.annotation.field.UIRow;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainAbstract;
import br.com.enginer.domain.system.usecase.core.schema.instance.DomainId;

public class EntityNineId extends DomainAbstract<EntityNineId> implements DomainId {

	@UIHidden
	@UIIdPart
	private Long idEntityEight;

	@UIHidden
	@UIIdPart
	private UUID idEntitySeven;

	@UIHidden
	@UIIdPart
	private Long idEntitySix;
	
	@UIFilter(label = "Entity Seven", field = "dado", readonly = false)
	@UIRow(visible = true, fields = { "id", "dado" })
	@UIColumn(label = "Entity Seven", fields = { "id", "dado" }, initial = false)
	private EntitySeven entitySeven;

	@UIFilter(label = "Entity Eight", field = "properties", readonly = false)
	@UIRow(visible = true, fields = { "position" })
	@UIColumn(label = "Entity Eight", fields = { "position" }, initial = false)
	private EntityEight entityEight;
	
	public EntityNineId() {
		super();
	}

	public EntityNineId(Long idEntityEight, UUID idEntitySeven, Long idEntitySix) {
		super();
		this.idEntityEight = idEntityEight;
		this.idEntitySeven = idEntitySeven;
		this.idEntitySix = idEntitySix;
	}

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
}
