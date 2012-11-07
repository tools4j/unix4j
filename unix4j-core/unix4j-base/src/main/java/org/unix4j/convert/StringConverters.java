package org.unix4j.convert;

public class StringConverters {
	public static final ValueConverter<String> DEFAULT = new ValueConverter<String>() {
		@Override
		public String convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return value.toString();
			}
			return null;
		}
	};
}
