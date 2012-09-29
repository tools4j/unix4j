package org.unix4j.variable;

/**
 * Default implementation of a {@link Variable}, a named value whose assigned
 * value can change over time.
 * 
 * @param <V>
 *            the type of the value
 */
public class DefaultVariable<V> extends AbstractNamedValue<V> implements Variable<V> {

	private V value;

	/**
	 * Constructor with name and type, initializing the variable with a null
	 * value.
	 * 
	 * @param name
	 *            the variable name
	 * @param type
	 *            the variable and value type
	 */
	public DefaultVariable(String name, Class<V> type) {
		super(name, type);
	}

	/**
	 * Constructor with name, type and initial value for the variable.
	 * 
	 * @param name
	 *            the variable name
	 * @param type
	 *            the variable and value type
	 * @param value
	 *            the initial value for the variable
	 */
	public DefaultVariable(String name, Class<V> type, V value) {
		super(name, type);
		setValue(value);
	}

	/**
	 * Returns the value currently associated with this variable.
	 * @return the variable value
	 */
	@Override
	public V getValue() {
		return value;
	}

	@Override
	public void setValue(V value) {
		this.value = value;
	}

}
