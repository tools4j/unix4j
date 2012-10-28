package org.unix4j.variable;

public interface ValueConverter<V> {
	V convert(Object value) throws IllegalArgumentException;
}
