package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializablePentaPredicate<A, B, C, D, E> extends Serializable {

    boolean test(A a, B b, C c, D d, E e);
}
