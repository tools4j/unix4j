package org.unix4j.convert;

/**
 * A registry defines convertes for different data types.
 * @author terz
 *
 */
public interface ConverterRegistry {
	<V> ValueConverter<V> getValueConverterFor(Class<V> type);
}
