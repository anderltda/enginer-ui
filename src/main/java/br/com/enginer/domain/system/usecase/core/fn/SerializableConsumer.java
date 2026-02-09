package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;
import java.util.function.Consumer;

@FunctionalInterface
public interface SerializableConsumer<T> extends Consumer<T>, Serializable {}
