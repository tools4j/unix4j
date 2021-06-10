package org.unix4j.convert;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class IntegerConverters {
	public static final ValueConverter<Integer> NUMBER_TO_INTEGER = new ValueConverter<Integer>() {
		@Override
		public Integer convert(Object value) throws IllegalArgumentException {
			if (value instanceof Number) {
				return ((Number)value).intValue();
			}
			return null;
		}
	};
	public static final ValueConverter<Integer> NUMBER_ROUNDED_TO_INTEGER = new ValueConverter<Integer>() {
		@Override
		public Integer convert(Object value) throws IllegalArgumentException {
			if (value instanceof Long || value instanceof Integer || value instanceof Short || value instanceof Byte || value instanceof AtomicLong || value instanceof AtomicInteger) {
				return ((Number)value).intValue();
			}
			if (value instanceof Number) {
				return (int)Math.round(((Number)value).doubleValue());
			}
			return null;
		}
	};
	public static final ValueConverter<Integer> STRING_TO_INTEGER = new ValueConverter<Integer>() {
		@Override
		public Integer convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				try {
					return Integer.parseInt(value.toString());
				} catch (NumberFormatException e) {
					return null;
				}
			}
			return null;
		}
	};
	public static final ValueConverter<Integer> DEFAULT = new CompositeValueConverter<Integer>().add(NUMBER_TO_INTEGER).add(STRING_TO_INTEGER);
	public static final ValueConverter<Integer> ROUND = new CompositeValueConverter<Integer>().add(NUMBER_ROUNDED_TO_INTEGER).add(STRING_TO_INTEGER);
}
