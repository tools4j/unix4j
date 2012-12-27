package org.unix4j.variable;

import java.util.Deque;
import java.util.LinkedList;

import org.unix4j.convert.ValueConverter;

/**
 * Default implementation of {@link VariableContext}.
 */
public class DefaultVariableContext implements VariableContext {
	
	private final Deque<VariableResolver> resolvers = new LinkedList<VariableResolver>();

	@Override
	public void addVariableResolver(VariableResolver resolver) {
		resolvers.addFirst(resolver);
	}
	@Override
	public void removeVariableResolver(VariableResolver resolver) {
		resolvers.removeFirstOccurrence(resolver);
	}
	@Override
	public Object getValue(String name) {
		for (final VariableResolver resolver : resolvers) {
			final Object value = resolver.getValue(name);
			if (value != null) {
				return value;
			}
		}
		return null;
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
		return getClass().getSimpleName() + resolvers;
	}

}
