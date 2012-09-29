package org.unix4j.variable;

/**
 * A constant is a name associated with a value; the name/value association does
 * not change over time.
 * 
 * @param <V>
 *            the type of the value
 */
public class Constant<V> extends AbstractNamedValue<V> {

	private final V value;

	/**
	 * Constructor with name, type and value.
	 * 
	 * @param name
	 *            the name of the constant
	 * @param type
	 *            the value type
	 * @param value
	 *            the constant value
	 */
	public Constant(String name, Class<V> type, V value) {
		super(name, type);
		this.value = value;
	}

	/**
	 * Returns a constant for the given name, type and value.
	 * 
	 * @param <V>
	 *            the type of the value
	 * @param name
	 *            the name of the constant
	 * @param type
	 *            the value type
	 * @param value
	 *            the constant value
	 * @return a new constant for the given name, type and value
	 */
	public static <V> Constant<V> of(String name, Class<V> type, V value) {
		return new Constant<V>(name, type, value);
	}

	/**
	 * Returns a constant for the given name and value, deriving the type from
	 * the given value.
	 * 
	 * @param <V>
	 *            the type of the value
	 * @param name
	 *            the name of the constant
	 * @param value
	 *            the constant value
	 * @return a new constant for the given name and value
	 */
	public static <V> Constant<? extends V> of(String name, V value) {
		@SuppressWarnings("unchecked")
		final Class<? extends V> valueType = (Class<? extends V>) value.getClass();
		return ofValue(name, valueType, value);
	}

	//helper for generic type redirection
	private static <V, T extends V> Constant<T> ofValue(String name, Class<T> type, V value) {
		return of(name, type, type.cast(value));
	}

	/**
	 * Returns the value of this constant.
	 * @return the constant value
	 */
	@Override
	public V getValue() {
		return value;
	}

}
