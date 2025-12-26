package br.com.enginer.domain.system.dto.entity.tag;

import java.util.List;

import br.com.enginer.domain.system.usecase.core.schema.instance.Domain;

/**
 * 
 */
public record SearchOverlay(List<Domain<?>> globais, List<Domain<?>> system) {}
