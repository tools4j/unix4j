package org.unix4j.variable;

public interface Literal<V> {
	Class<V> getType();
	String getName();
	V getValue();
}
