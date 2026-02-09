package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;
import java.util.function.Supplier;

@FunctionalInterface
public interface SerializableSupplier<R> extends Supplier<R>, Serializable {}
