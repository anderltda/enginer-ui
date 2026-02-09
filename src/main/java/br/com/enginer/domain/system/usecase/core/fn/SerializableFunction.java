package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface SerializableFunction<T, R> extends Function<T, R>, Serializable {}
