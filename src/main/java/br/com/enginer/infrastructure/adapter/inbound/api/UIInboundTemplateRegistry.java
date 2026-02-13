package br.com.enginer.infrastructure.adapter.inbound.api;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import br.com.enginer.domain.system.usecase.core.enums.TypeTemplate;
import br.com.enginer.domain.system.usecase.core.schema.Form;
import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;
import br.com.enginer.domain.system.usecase.port.inbound.api.UIInboundPort;

/**
 * Registry de templates -> handler. Centraliza o roteamento TAB/ROW/FORM/FILTER
 * para o método correspondente no UIInboundPort.
 */
@Component
public class UIInboundTemplateRegistry {

	private final Map<TypeTemplate, Function<Domain<?>, Form>> registry;

	/**
	 * @param uiInboundPort
	 */
	public UIInboundTemplateRegistry(UIInboundPort<Domain<?>> uiInboundPort) {
		
		// EnumMap é mais performático e seguro para enums
		EnumMap<TypeTemplate, Function<Domain<?>, Form>> map = new EnumMap<>(TypeTemplate.class);
		map.put(TypeTemplate.TAB, uiInboundPort::tab);
		map.put(TypeTemplate.ROW, uiInboundPort::row);
		map.put(TypeTemplate.FORM, uiInboundPort::form);
		map.put(TypeTemplate.FILTER, uiInboundPort::filter);

		this.registry = Map.copyOf(map); // imutável
	}

	/**
	 * Retorna o handler para o template ou null se não existir.
	 */
	public Function<Domain<?>, Form> get(TypeTemplate type) {
		return registry.get(type);
	}
}