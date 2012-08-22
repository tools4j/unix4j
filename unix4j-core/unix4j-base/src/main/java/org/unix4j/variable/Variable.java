package org.unix4j.variable;

public interface Variable<V> extends Literal<V> {
	void setValue(V value);
}
