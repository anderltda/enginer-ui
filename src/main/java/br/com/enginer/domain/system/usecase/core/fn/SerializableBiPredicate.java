package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface SerializableBiPredicate<T, U> extends BiPredicate<T, U>, Serializable {}
