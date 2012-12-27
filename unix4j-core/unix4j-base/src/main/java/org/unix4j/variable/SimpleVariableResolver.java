package org.unix4j.variable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple variable resolver implementation backed by a map of variable names to
 * values.
 */
public class SimpleVariableResolver implements VariableResolver {
	
	private final Map<String, Object> nameToValue = new LinkedHashMap<String, Object>();
	
	/**
	 * Sets the specified value for the variable with the given name. If the
	 * value is null, the variable is cleared. Returns the old value held by the
	 * value before setting it, or null if no such variable existed.
	 * 
	 * @param name
	 *            the variable name
	 * @param value
	 *            the new value for the variable, null to clear the variable
	 * @return the old value held by the variable before setting it, or null if
	 *         the variable did not exist before
	 */
	public Object setValue(String name, Object value) {
		if (value != null) {
			return nameToValue.put(name, value);
		}
		return nameToValue.remove(name);
	}

	@Override
	public Object getValue(String name) {
		return nameToValue.get(name);
	}
	
	@Override
	public String toString() {
		return nameToValue.toString();
	}

}
