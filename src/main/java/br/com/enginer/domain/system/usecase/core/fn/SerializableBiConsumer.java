package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface SerializableBiConsumer<T, U> extends BiConsumer<T, U>, Serializable {}
