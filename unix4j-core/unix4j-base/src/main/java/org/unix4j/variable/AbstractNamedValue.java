package org.unix4j.variable;

import org.unix4j.util.EqualsUtil;
import org.unix4j.util.HashUtil;

/**
 * Abstract base class for most constants and variables.
 * 
 * @param <V>
 *            the type of the value
 */
abstract public class AbstractNamedValue<V> implements NamedValue<V> {
	
	private final String name;

	/**
	 * Constructor with name.
	 * 
	 * @param name
	 *            the name of the constant or variable
	 */
	public AbstractNamedValue(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the hash code based on name, type and value.
	 * 
	 * @return the hash code based on name, value type and value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + HashUtil.hashObject(getName());
		result = prime * result + HashUtil.hashObject(getValue());
		return result;
	}

	/**
	 * Returns true if obj is an instance of the same class as {@code this}
	 * instance and if name, type and value are equal.
	 * 
	 * @return true for other named values of the same class with the same name,
	 *         type and value
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final AbstractNamedValue<?> other = (AbstractNamedValue<?>) obj;
		if (!EqualsUtil.equalObjects(getName(), other.getName()))
			return false;
		if (!EqualsUtil.equalObjects(getValue(), other.getValue()))
			return false;
		return true;
	}

	/**
	 * Returns a string representation for this named value, such as "$myVariable=786".
	 * @return a string similar to "$myVariable=786"
	 */
	@Override
	public String toString() {
		return "$" + getName() + "=" + getValue();
	}

}
