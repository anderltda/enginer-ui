package br.com.enginer.infrastructure.adapter.outbound.repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.dto.entity.tag.TagId;
import br.com.enginer.domain.system.dto.entity.tag.TagType;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.core.utils.ReflectionUtils;
import br.com.enginer.domain.system.usecase.core.utils.StringsUtils;
import br.com.enginer.domain.system.usecase.port.outbound.logger.LoggerOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.TagRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.storage.FileStorageOutboundPort;
import br.com.enginer.infrastructure.utils.NormalizeUtils;

/**
 * Adapter outbound específico para o domínio {@link Tag}.
 *
 * <p>
 * Esta classe apenas especializa o {@link DelegatingRepositoryOutboundAdapter}
 * para {@link Tag}, delegando todas as operações ao repositório genérico
 * {@link RepositoryOutboundPort}<{@link Domain}<?>>.
 * </p>
 *
 * <p>
 * A existência deste adapter permite:
 * <ul>
 * <li>Evitar problemas de type erasure em runtime</li>
 * <li>Injeção limpa via Spring de {@link TagRepositoryOutboundPort}</li>
 * <li>Uso seguro e tipado dentro dos UseCases</li>
 * </ul>
 * </p>
 */
@Component
public class TagRepositoryOutboundAdapterPort extends DelegatingRepositoryOutboundAdapter<Tag> implements TagRepositoryOutboundPort {

	/** 
	 * Lista de Tags para o domínio atual.
	 */
	private List<Tag> entities;

	/**
	 * @param delegate
	 * @param loggerOutboundPort
	 * @param fileStorageOutboundPort
	 */
	protected TagRepositoryOutboundAdapterPort(RepositoryOutboundPort<Domain<?>> delegate, LoggerOutboundPort loggerOutboundPort, FileStorageOutboundPort fileStorageOutboundPort) {
		super(delegate, loggerOutboundPort, fileStorageOutboundPort);
	}
	
	/**
	 *
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void pull(Domain<?> type) {
		
		try {
			
			if(type == null) return;
		
			List<String> tags = (List<String>) ReflectionUtils.execute(type, StringsUtils.getMethod("tags"));
			String domain = type.getClass().getSimpleName();
			Object domainId = ReflectionUtils.createUriIdComposedType(type);

			entities = new ArrayList<Tag>();

			Optional.ofNullable(tags)
					.orElse(List.of())
					.stream()
					.filter(Objects::nonNull)
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.forEach(name -> {
						TagId tagId = new TagId();
						tagId.setNormalizedName(ReflectionUtils.normalizeAlphaNumeric(name));
						tagId.setDomain(null);
						tagId.setDomainId(null);

						Tag tag = new Tag();
						tag.setId(tagId);
						tag.setName(name);
						tag.setType(TagType.GLOBAL);
						tag.setCreatedAt(Instant.now());

						entities.add(tag);
					});

			if (domainId != null) {
				
				Map<String, Object> filter = new HashMap<String, Object>();
				filter.put("id.domain", domain);
				filter.put("id.domainId", domainId);
				
				List<Tag> tagsFindAll = delegate.findAll(new Tag(), filter).stream()
				        .filter(Tag.class::isInstance)
				        .map(Tag.class::cast)
				        .toList();
				
				tagsFindAll.forEach(tag -> {
					filter.clear();
					filter.put("normalizedName", tag.getId().getNormalizedName());
					filter.put("domain", tag.getId().getDomain());
					filter.put("domainId", tag.getId().getDomainId());
					delegate.delete(new Tag(), filter);
				});
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			loggerOutboundPort.warn(TagRepositoryOutboundAdapterPort.class, ex.getMessage());
		}
	}
	
	/**
	 *
	 */
	@Override
	public void push(Domain<?> type) {

		try {

			if (type == null) return;
			
			String domain = type.getClass().getSimpleName();
			
			Object domainId = ReflectionUtils.createUriIdComposedType(type);
			
			entities.forEach(tag -> {
				tag.getId().setDomain(domain);
				tag.getId().setDomainId(NormalizeUtils.decodeIfNeeded(domainId.toString()));
				delegate.save(tag);
			});

		} catch (Exception ex) {
			ex.printStackTrace();
			loggerOutboundPort.warn(TagRepositoryOutboundAdapterPort.class, ex.getMessage());
		}
	}
}