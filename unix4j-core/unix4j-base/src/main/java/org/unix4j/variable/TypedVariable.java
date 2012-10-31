package org.unix4j.variable;

import org.unix4j.convert.ValueConverter;

/**
 * A typed variable with the type specified as generic parameter {@code T}. The
 * typed variable supports conversion of arbitrary values into the variable's
 * type using the converter returned by {@link #getValueConverter()}. Note that
 * the converter returns null and throws no exception if value conversion is not
 * possible.
 *
 * @param <T>	the variable type
 */
public interface TypedVariable<T> extends Variable {
	/**
	 * Returns the converter used to convert arbitrary values into this 
	 * variable's data type. Note that converters return null and do not throw 
	 * an exception if value conversion is not possible.
	 * 
	 * @return the converter used to convert values into this variable's type
	 */
	ValueConverter<T> getValueConverter();
}
