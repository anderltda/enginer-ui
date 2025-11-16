package br.com.enginer.domain.example.usercase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.enginer.domain.example.dto.entity.EntityEight;
import br.com.enginer.domain.example.dto.entity.EntitySeven;
import br.com.enginer.domain.example.dto.entity.EntitySix;
import br.com.enginer.domain.system.usercase.AbstractUserCase;
import br.com.enginer.domain.system.usercase.annotation.AutoDependencyInjector;
import br.com.enginer.domain.system.usercase.exception.UncheckedException;
import br.com.enginer.domain.system.usercase.page.PageResult;

public class EntityEightUserCase extends AbstractUserCase<EntityEight> implements br.com.enginer.domain.example.usercase.port.EntityEightUserCase{
	
	@AutoDependencyInjector
	private EntitySevenUserCase entitySevenUserCase;
	
	@AutoDependencyInjector
	private EntitySixUserCase entitySixUserCase;
	
    /**
     * Sobrescrita de salvarLista com pós-processamento customizado.
     * Agora totalmente tipada com EntityEight.
     */
    @Override
    public List<EntityEight> salvarLista(List<EntityEight> entities) throws UncheckedException {
        List<EntityEight> persisted = super.salvarLista(entities);
        List<EntityEight> reloaded = new ArrayList<>();

        for (EntityEight entity : persisted) {
            EntityEight refreshed = buscarPorId(entity);
            reloaded.add(refreshed);
        }

        return reloaded;
    }

    /**
     * Sobrescrita de buscarTodosPaginado com montagem de entidades relacionadas.
     */
    @Override
    public PageResult<EntityEight> buscarTodosPaginado(EntityEight domain, Map<String, Object> filter) throws UncheckedException {

        PageResult<EntityEight> result = super.buscarTodosPaginado(domain, filter);

        if (result != null && result.getContent() != null) {
            result.getContent().forEach(entityEight -> {
                // busca as entidades relacionadas com tipagem forte
                EntitySeven entitySeven = entitySevenUserCase.buscarPorId(entityEight.getEntitySeven());
                EntitySix entitySix = entitySixUserCase.buscarPorId(new EntitySix(entitySeven.getId().getIdEntitySix()));

                entitySeven.getId().setEntitySix(entitySix);
                entityEight.setEntitySeven(entitySeven);
            });
        }

        return result;
    }	
}
