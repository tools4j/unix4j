package org.unix4j.variable;

/**
 * Default implementation of a {@link Variable}, a named value whose assigned
 * value can change over time. This variable implementation is not thread safe.
 * 
 * @param <V>
 *            the type of the value
 */
public class DefaultVariable<V> extends AbstractNamedValue<V> implements Variable<V> {

	private V value;

	/**
	 * Constructor with name initializing the variable with a null value.
	 * 
	 * @param name
	 *            the variable name
	 */
	public DefaultVariable(String name) {
		super(name);
	}

	/**
	 * Constructor with name and initial value for the variable.
	 * 
	 * @param name
	 *            the variable name
	 * @param value
	 *            the initial value for the variable
	 */
	public DefaultVariable(String name, V value) {
		super(name);
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
