package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableTriPredicate<A, B, C> extends Serializable {

    boolean test(A a, B b, C c);
}
