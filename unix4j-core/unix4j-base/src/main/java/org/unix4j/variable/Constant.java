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
	 * Constructor with name and value.
	 * 
	 * @param name
	 *            the name of the constant
	 * @param value
	 *            the constant value
	 */
	public Constant(String name, V value) {
		super(name);
		this.value = value;
	}

	/**
	 * Returns a constant for the given name and value, an alternative to the
	 * constructor if the type parameter contains wildcards.
	 * 
	 * @param <V>
	 *            the type of the value
	 * @param name
	 *            the name of the constant
	 * @param value
	 *            the constant value
	 * @return a new constant for the given name and value
	 */
	public static <V> Constant<V> of(String name, V value) {
		return new Constant<V>(name, value);
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
