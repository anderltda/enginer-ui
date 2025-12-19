package br.com.enginer.infrastructure.adapter.outbound.repository;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.dto.entity.tag.Tag;
import br.com.enginer.domain.system.usecase.port.outbound.repository.RepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.port.outbound.repository.TagRepositoryOutboundPort;
import br.com.enginer.domain.system.usecase.schema.instance.Domain;

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
	 * @param delegate
	 */
	public TagRepositoryOutboundAdapterPort(RepositoryOutboundPort<Domain<?>> delegate) {
		super(delegate);
	}
}