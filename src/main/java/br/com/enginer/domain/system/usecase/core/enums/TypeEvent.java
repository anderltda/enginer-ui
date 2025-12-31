package br.com.enginer.domain.system.usecase.core.enums;


/**
 * Enum que representa os tipos de eventos do calendário.
 *
 * Cada tipo possui um estilo visual associado,
 * utilizado pela UI para renderização.
 */
public enum TypeEvent {

    MEETING(TypeEventSyle.PRIMARY),
    TASK(TypeEventSyle.SUCCESS),
    ALERT(TypeEventSyle.WARNING),
    REMINDER(TypeEventSyle.COMPLETE),
    SYSTEM(TypeEventSyle.DANGER);

    private final TypeEventSyle style;

    TypeEvent(TypeEventSyle style) {
        this.style = style;
    }

    /**
     * Retorna o estilo visual associado ao tipo de evento.
     */
    public TypeEventSyle getStyle() {
        return style;
    }

    /**
     * Retorna a classe CSS associada ao tipo de evento.
     */
    public String getCssClass() {
        return style.getCssClass();
    }

    @Override
    public String toString() {
        return name();
    }

    /**
     * Obtém o enum a partir do nome do tipo.
     */
    public static TypeEvent fromValue(String value) {
        return TypeEvent.valueOf(value.toUpperCase());
    }
}