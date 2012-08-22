package org.unix4j.variable;

public class DefaultVariable<V> extends AbstractLiteral<V> implements Variable<V> {
	
	private V value;
	
	public DefaultVariable(String name, Class<V> type) {
		super(name, type);
	}
	public DefaultVariable(String name, Class<V> type, V value) {
		super(name, type);
		setValue(value);
	}
	
	@Override
	public V getValue() {
		return value;
	}
	@Override
	public void setValue(V value) {
		this.value = value;
	}

}
