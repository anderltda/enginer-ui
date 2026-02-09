package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;

@FunctionalInterface
public interface SerializablePentaFunction<A, B, C, D, E, R> extends Serializable {

    R apply(A a, B b, C c, D d, E e);
}
