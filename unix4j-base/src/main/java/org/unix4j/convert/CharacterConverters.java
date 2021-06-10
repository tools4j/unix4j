package org.unix4j.convert;

public class CharacterConverters {
	public static final ValueConverter<Character> INTEGER_TO_CHARACTER = new ValueConverter<Character>() {
		@Override
		public Character convert(Object value) throws IllegalArgumentException {
			if (value instanceof Integer) {
				return (char)((Integer)value).intValue();
			}
			return null;
		}
	};
	public static final ValueConverter<Character> STRING_TO_CHARACTER = new ValueConverter<Character>() {
		@Override
		public Character convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				final String s = value.toString();
				if (s.length() > 0) {
					return s.charAt(0);
				}
			}
			return null;
		}
	};
	public static final ValueConverter<Character> DEFAULT = new CompositeValueConverter<Character>().add(INTEGER_TO_CHARACTER).add(STRING_TO_CHARACTER);
}
