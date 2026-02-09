package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableTriConsumer<A, B, C> extends Serializable {

    void accept(A a, B b, C c);
}
