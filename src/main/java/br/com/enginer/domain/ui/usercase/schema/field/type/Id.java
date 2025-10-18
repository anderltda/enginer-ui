package br.com.enginer.domain.ui.usercase.schema.field.type;

import java.util.Objects;

/**
 * @param <T>
 */
public class Id<T> {

	private final T value;

	public Id(T value) {
		this.value = value;
	}

	public static <T> Id<T> of(T value) {
		return new Id<>(value);
	}

	public T getValue() {
		return value;
	}

	public boolean isPresent() {
		return value != null;
	}

	public boolean instanceofType(Class<?> type) {
		return type.isInstance(value);
	}

	@SuppressWarnings("unchecked")
	public <U> U cast(Class<U> type) {
		if (type.isInstance(value)) {
			return (U) value;
		}
		throw new ClassCastException("Cannot cast " + value + " to " + type.getSimpleName());
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Id<?> other))
			return false;
		return Objects.equals(this.value, other.value);
	}
}
