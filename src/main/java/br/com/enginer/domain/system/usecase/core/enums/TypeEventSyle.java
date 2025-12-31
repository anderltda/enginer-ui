package br.com.enginer.domain.system.usecase.core.enums;


/**
 * Enum que representa classes CSS de background em tons claros.
 */
public enum TypeEventSyle {

    SUCCESS("bg-success-lighter"),
    PRIMARY("bg-primary-lighter"),
    WARNING("bg-warning-lighter"),
    COMPLETE("bg-complete-lighter"),
    DANGER("bg-danger-lighter");

	private final String cssClass;

    TypeEventSyle(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }

    @Override
    public String toString() {
        return cssClass;
    }

    /**
     * Obtém o enum a partir do valor da classe CSS.
     */
    public static TypeEventSyle fromValue(String value) {
        for (TypeEventSyle bg : values()) {
            if (bg.cssClass.equalsIgnoreCase(value)) {
                return bg;
            }
        }
        throw new IllegalArgumentException("Classe CSS inválida: " + value);
    }
}