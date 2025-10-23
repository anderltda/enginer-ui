package br.com.enginer.domain.system.usercase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica que um campo de um {@link AbstractUserCase}
 * deve receber automaticamente as dependências herdadas
 * (RepositoryOutboundPort, PublisherOutboundPort, etc.).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoDependencyInjector {}
