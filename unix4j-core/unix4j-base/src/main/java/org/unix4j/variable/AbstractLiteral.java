package org.unix4j.variable;

import org.unix4j.util.EqualsUtil;
import org.unix4j.util.HashUtil;

abstract public class AbstractLiteral<V> implements Literal<V> {
	private final String name;
	private final Class<V> type;
	public AbstractLiteral(String name, Class<V> type) {
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Class<V> getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + HashUtil.hashObject(getName());
		result = prime * result + HashUtil.hashObject(getType());
		result = prime * result + HashUtil.hashObject(getValue());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final AbstractLiteral<?> other = (AbstractLiteral<?>) obj;
		if (!EqualsUtil.equalObjects(getName(), other.getName())) return false;
		if (!EqualsUtil.equalObjects(getType(), other.getType())) return false;
		if (!EqualsUtil.equalObjects(getValue(), other.getValue())) return false;
		return true;
	}
	@Override
	public String toString() {
		return "$" + getName() + "[" + getType() + "]=" + getValue();
	}
	
	
}
