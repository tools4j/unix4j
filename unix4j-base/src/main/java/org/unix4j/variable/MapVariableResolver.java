package org.unix4j.variable;

import java.util.Map;

/**
 * Variable resolver based on an existing map, where the keys may or may not be
 * prefixed with the variable dollar sign '$'. This class is very similar to
 * {@link SimpleVariableResolver} but it is used when a map with name/value for
 * the variables exists already, possibly with a slightly different key (for
 * instance without the dollar prefix). Subclasses may want to override the
 * {@link #getMapKeyForVariableName(String)} method to change the default
 * assumption about the map keys.
 */
public class MapVariableResolver implements VariableResolver {

	private final Map<?, ?> nameToValue;

	/**
	 * Constructor with name-to-value map.
	 * 
	 * @param nameToValue
	 *            the map with variable names as keys
	 */
	public MapVariableResolver(Map<?, ?> nameToValue) {
		this.nameToValue = nameToValue;
	}

	/**
	 * Returns a variable resolver for the ENV variables.
	 * 
	 * @return a variable resolver for the ENV variables.
	 * @see System#getenv()
	 */
	public static MapVariableResolver getEnv() {
		return new MapVariableResolver(System.getenv());
	}

	/**
	 * Returns a variable resolver for the system properties.
	 * 
	 * @return a variable resolver for the system properties
	 * @see System#getProperties()
	 */
	public static MapVariableResolver getSystemProperties() {
		return new MapVariableResolver(System.getProperties());
	}

	/**
	 * Returns the map key for a given variable name. If the variable name
	 * starts with a dollar sign '$', the name without dollar prefix is
	 * returned. Otherwise, the variable name is returned unchanged.
	 * 
	 * @param variableName
	 *            the variable name, usually prefixed with a dollar sign '$'
	 * @return the variable name without dollar prefix
	 */
	public String getMapKeyForVariableName(String variableName) {
		if (variableName.startsWith("$")) {
			return variableName.substring(1);
		}
		return variableName;
	}

	@Override
	public Object getValue(String name) {
		final String key = getMapKeyForVariableName(name);
		return nameToValue.get(key);
	}

	@Override
	public String toString() {
		return nameToValue.toString();
	}

}
