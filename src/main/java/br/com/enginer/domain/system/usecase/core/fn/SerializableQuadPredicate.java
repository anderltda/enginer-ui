package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableQuadPredicate<A, B, C, D> extends Serializable {

    boolean test(A a, B b, C c, D d);
}
