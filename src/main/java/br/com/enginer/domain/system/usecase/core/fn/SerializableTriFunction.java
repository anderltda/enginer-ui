package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableTriFunction<A, B, C, R> extends Serializable {

    R apply(A a, B b, C c);
}
