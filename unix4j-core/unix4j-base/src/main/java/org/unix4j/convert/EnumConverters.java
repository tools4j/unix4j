package org.unix4j.convert;

public class EnumConverters {
	public static class StringToEnumConverter<E extends Enum<E>> implements ValueConverter<E> {
		private final Class<E> enumClass;
		public StringToEnumConverter(Class<E> enumClass) {
			this.enumClass = enumClass;
		}
		@Override
		public E convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				final String name = value.toString();
				try {
					return Enum.valueOf(enumClass, name);
				} catch (IllegalArgumentException e) {
					return null;
				}
			}
			return null;
		}
	};
}
