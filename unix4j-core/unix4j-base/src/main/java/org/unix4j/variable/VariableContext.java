package org.unix4j.variable;

public interface VariableContext {
	/**
	 * Null context returning null for every {@code getValue(..)} call and
	 * throwing an exception when {@code setValue(..)} is called.
	 */
	VariableContext NULL_CONTEXT = new VariableContext() {
		@Override
		public Object setValue(String name, Object value) {
			throw new IllegalArgumentException("null context: no values can be set");
		}
		@Override
		public <V> V getAndConvertValue(String name, Class<V> type) {
			return null;
		}
		@Override
		public Object getValue(String name) {
			return null;
		}
	};
	
	Object setValue(String name, Object value);
	Object getValue(String name);
	<V> V getAndConvertValue(String name, Class<V> type) throws IllegalArgumentException;
}
