package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializablePentaConsumer<A, B, C, D, E> extends Serializable {

    void accept(A a, B b, C c, D d, E e);
}
