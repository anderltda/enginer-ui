package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializableQuadConsumer<A, B, C, D> extends Serializable {

    void accept(A a, B b, C c, D d);
}
