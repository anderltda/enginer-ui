package br.com.enginer.domain.system.usecase.core.utils;

import java.util.UUID;

/**
 * 
 */
public class UUIDGeneratorUtils {

    /**
     * Gera um UUID aleatório no formato padrão.
     * @return UUID como String.
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }

    /**
     * Gera um UUID diretamente como objeto.
     * @return UUID.
     */
    public static UUID generateUUID() {
        return UUID.randomUUID();
    }
}