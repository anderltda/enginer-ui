package br.com.enginer.domain.ui.usercase.utils;

import java.util.UUID;

/**
 * 
 */
public class UUIDGenerator {

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