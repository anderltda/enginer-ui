package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableQuadFunction<A, B, C, D, R> extends Serializable {

    R apply(A a, B b, C c, D d);
}
