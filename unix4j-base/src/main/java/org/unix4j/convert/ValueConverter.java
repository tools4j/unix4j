package org.unix4j.convert;

/**
 * A converter for values of an arbitrary type into the target type specified by 
 * the generic parameter {@code V}. 
 *
 * @param <V>	the value target type after the conversion
 */
public interface ValueConverter<V> {
	/**
	 * Converts the given value into the target type {@code V} if it is not null
	 * and if such a conversion is supported by this converter. Returns null if
	 * the specified value is null or if the value cannot be converted.
	 * 
	 * @param value
	 *            the unconverted value
	 * @return the converted value or null if the value is null or cannot be
	 *         converted
	 */
	V convert(Object value);
}
