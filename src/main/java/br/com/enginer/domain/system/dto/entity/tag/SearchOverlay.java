package br.com.enginer.domain.system.dto.entity.tag;

import java.util.List;

import br.com.enginer.domain.system.usecase.schema.instance.Domain;

/**
 * 
 */
public record SearchOverlay(List<Domain<?>> users, List<Domain<?>> tags) {}
