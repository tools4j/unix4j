package org.unix4j.variable;

public class Constant<V> extends AbstractLiteral<V> {
	
	private final V value;

	public Constant(String name, Class<V> type, V value) {
		super(name, type);
		this.value = value;
	}
	public static <V> Constant<V> of(String name, Class<V> type, V value) {
		return new Constant<V>(name, type, value);
	}
	public static <V> Constant<? extends V> of(String name, V value) {
		@SuppressWarnings("unchecked")
		final Class<? extends V> valueType = (Class<? extends V>)value.getClass();
		return ofValue(name, valueType, value);
	}
	private static <T extends V, V> Constant<T> ofValue(String name, Class<T> type, V value) {
		return of(name, type, type.cast(value));
	}
	
	@Override
	public V getValue() {
		return value;
	}

}
