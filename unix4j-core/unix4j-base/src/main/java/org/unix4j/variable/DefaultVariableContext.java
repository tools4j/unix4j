package org.unix4j.variable;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unix4j.convert.ValueConverter;

public class DefaultVariableContext implements VariableContext {
	
	private final Map<String, Object> variables = new LinkedHashMap<String, Object>();

	@Override
	public Object setValue(String name, Object value) {
		if (value != null) {
			return variables.put(name, value);
		}
		return variables.remove(name);
	}

	@Override
	public Object getValue(String name) {
		return variables.get(name);
	}

	@Override
	public <V> V getValue(String name, ValueConverter<V> converter) throws IllegalArgumentException {
		final Object value = getValue(name);
		if (value == null) {
			return null;
		}
		final V converted = converter.convert(value);
		if (converted == null) {
			//conversion failed, throw exception
			throw new IllegalArgumentException("value conversion not supported for variable " + name + "=" + value + " using converter=" + converter);
		}
		return converted;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + variables;
	}

}
