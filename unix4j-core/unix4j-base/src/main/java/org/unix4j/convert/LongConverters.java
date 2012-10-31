package org.unix4j.convert;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LongConverters {
	public static final ValueConverter<Long> NUMBER_TO_LONG = new ValueConverter<Long>() {
		@Override
		public Long convert(Object value) throws IllegalArgumentException {
			if (value instanceof Number) {
				return ((Number)value).longValue();
			}
			return null;
		}
	};
	public static final ValueConverter<Long> NUMBER_ROUNDED_TO_LONG = new ValueConverter<Long>() {
		@Override
		public Long convert(Object value) throws IllegalArgumentException {
			if (value instanceof Long || value instanceof Integer || value instanceof Short || value instanceof Byte || value instanceof AtomicInteger || value instanceof AtomicLong) {
				return ((Number)value).longValue();
			}
			if (value instanceof Number) {
				return Math.round(((Number)value).doubleValue());
			}
			return null;
		}
	};
	public static final ValueConverter<Long> STRING_TO_LONG = new ValueConverter<Long>() {
		@Override
		public Long convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				try {
					return Long.parseLong(value.toString());
				} catch (NumberFormatException e) {
					return null;
				}
			}
			return null;
		}
	};
	public static final ValueConverter<Long> DEFAULT = new CompositeValueConverter<Long>().add(NUMBER_TO_LONG).add(STRING_TO_LONG);
	public static final ValueConverter<Long> ROUND = new CompositeValueConverter<Long>().add(NUMBER_ROUNDED_TO_LONG).add(STRING_TO_LONG);
}
