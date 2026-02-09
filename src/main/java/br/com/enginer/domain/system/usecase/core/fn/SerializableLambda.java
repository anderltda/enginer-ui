package br.com.enginer.domain.system.usecase.core.fn;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * Utilitário para extrair metadados de lambdas e method references
 * (ex: nome do método, classe de implementação, assinatura JVM).
 *
 * <p>
 * Este utilitário funciona apenas para lambdas/method references
 * que sejam {@link Serializable}. Isso é necessário para que o
 * compilador gere o método sintético {@code writeReplace()}, que
 * permite acessar o {@link SerializedLambda}.
 * </p>
 *
 * <p>
 * É amplamente utilizado em frameworks (Spring, JPA, MapStruct)
 * para obter informações do método sem uso de Strings hardcoded,
 * mantendo segurança de refatoração e validação em tempo de compilação.
 * </p>
 */
public final class SerializableLambda {

    /**
     * Classe utilitária: construtor privado para evitar instanciação.
     */
    private SerializableLambda() {}

    /**
     * Extrai o nome do método real associado a uma lambda ou method reference.
     *
     * <p>
     * Exemplo:
     * </p>
     * <pre>{@code
     * SerializableConsumer<ProviderIdentityVo> ref =
     *         new UserAccountUseCase()::login;
     *
     * String methodName = SerializableLambda.extractMethodName(ref);
     * // methodName == "login"
     * }</pre>
     *
     * @param lambda lambda ou method reference serializável
     * @return nome do método de implementação (ex: "login")
     */
    public static String extractMethodName(Serializable lambda) {
        return extractSerializedLambda(lambda).getImplMethodName();
    }

    /**
     * Extrai o {@link SerializedLambda} a partir de uma lambda ou method reference.
     *
     * <p>
     * O {@link SerializedLambda} contém metadados importantes como:
     * </p>
     * <ul>
     *   <li>Classe de implementação ({@code getImplClass()})</li>
     *   <li>Nome do método ({@code getImplMethodName()})</li>
     *   <li>Assinatura JVM ({@code getImplMethodSignature()})</li>
     * </ul>
     *
     * @param lambda lambda ou method reference serializável
     * @return instância de {@link SerializedLambda}
     */
    public static SerializedLambda extractSerializedLambda(Serializable lambda) {
        return extractSerializedLambdaInternal(lambda);
    }

    // =========================================================
    // Core interno reutilizável
    // =========================================================

    /**
     * Método central responsável por acessar o método sintético
     * {@code writeReplace()} gerado pelo compilador para lambdas serializáveis.
     *
     * <p>
     * Esse método converte a lambda em um {@link SerializedLambda},
     * permitindo inspeção de seus metadados em tempo de execução.
     * </p>
     *
     * @param lambda instância da lambda
     * @return {@link SerializedLambda} extraído
     * @throws RuntimeException caso não seja possível extrair os metadados
     */
    private static SerializedLambda extractSerializedLambdaInternal(Object lambda) {
        try {
            Method writeReplace = lambda.getClass().getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            return (SerializedLambda) writeReplace.invoke(lambda);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao extrair método da lambda", e);
        }
    }
}