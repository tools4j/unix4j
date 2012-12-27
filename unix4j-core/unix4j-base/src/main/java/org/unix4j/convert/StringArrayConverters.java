package org.unix4j.convert;

import java.lang.reflect.Array;

public class StringArrayConverters {
	public static final ValueConverter<String[]> STRING_ARRAY_TO_STRING_ARRAY = new ValueConverter<String[]>() {
		@Override
		public String[] convert(Object value) throws IllegalArgumentException {
			if (value instanceof String[]) {
				return (String[])value;
			}
			return null;
		}
	};
	public static final ValueConverter<String[]> OBJECT_ARRAY_TO_STRING_ARRAY = new ValueConverter<String[]>() {
		@Override
		public String[] convert(Object value) throws IllegalArgumentException {
			if (value instanceof Object[]) {
				final Object[] array = (Object[])value;
				final int len = array.length;
				final String[] result = new String[len];
				for (int i = 0; i < len; i++) {
					final Object element = array[i];
					result[i] = element == null ? null : element.toString(); 
				}
				return result;
			}
			return null;
		}
	};
	public static final ValueConverter<String[]> ANY_ARRAY_TO_STRING_ARRAY = new ValueConverter<String[]>() {
		@Override
		public String[] convert(Object value) throws IllegalArgumentException {
			if (value != null && value.getClass().isArray()) {
				final int len = Array.getLength(value);
				final String[] result = new String[len];
				for (int i = 0; i < len; i++) {
					final Object element = Array.get(value, i);
					result[i] = element == null ? null : element.toString();
				}
				return result;
			}
			return null;
		}
	};
	public static final ValueConverter<String[]> WHITESPACE_DELIMITED = new ValueConverter<String[]>() {
		@Override
		public String[] convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return value.toString().split("\\s+");
			}
			return null;
		}
	};
	public static final ValueConverter<String[]> SPACE_DELIMITED = new ValueConverter<String[]>() {
		@Override
		public String[] convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return value.toString().split(" ");
			}
			return null;
		}
	};
	public static final ValueConverter<String[]> COMMA_DELIMITED = new ValueConverter<String[]>() {
		@Override
		public String[] convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return value.toString().split(",");
			}
			return null;
		}
	};
	public static final ValueConverter<String[]> TAB_DELIMITED = new ValueConverter<String[]>() {
		@Override
		public String[] convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return value.toString().split(",");
			}
			return null;
		}
	};
	public static final ValueConverter<String[]> OBJECT_TO_SINGLETON_STRING_ARRAY = new ValueConverter<String[]>() {
		@Override
		public String[] convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return new String[] {value.toString()};
			}
			return null;
		}
	};
	public static final ValueConverter<String[]> ARRAY_TO_STRING_ARRAY = new CompositeValueConverter<String[]>().add(STRING_ARRAY_TO_STRING_ARRAY).add(OBJECT_ARRAY_TO_STRING_ARRAY).add(ANY_ARRAY_TO_STRING_ARRAY);
	public static final ValueConverter<String[]> COLLECTION_TO_STRING_ARRAY = new ConcatenatedConverter<String[]>(ArrayConverters.COLLECTION_TO_ARRAY, OBJECT_ARRAY_TO_STRING_ARRAY);
	
	public static final ValueConverter<String[]> COLLECTION_OR_ARRAY_TO_STRING_ARRAY = new CompositeValueConverter<String[]>().add(COLLECTION_TO_STRING_ARRAY).add(ARRAY_TO_STRING_ARRAY);
	public static final ValueConverter<String[]> COLLECTION_OR_ARRAY_TO_FLAT_STRING_ARRAY = new ConcatenatedConverter<String[]>(ListConverters.COLLECTION_OR_ARRAY_TO_FLAT_LIST, COLLECTION_TO_STRING_ARRAY);
	
	public static final ValueConverter<String[]> DEFAULT = new CompositeValueConverter<String[]>().add(COLLECTION_OR_ARRAY_TO_STRING_ARRAY).add(OBJECT_TO_SINGLETON_STRING_ARRAY);
	public static final ValueConverter<String[]> FLATTEN = new CompositeValueConverter<String[]>().add(COLLECTION_OR_ARRAY_TO_FLAT_STRING_ARRAY).add(OBJECT_TO_SINGLETON_STRING_ARRAY);
	public static final ValueConverter<String[]> DEFAULT_WHITESPACE_DELIMITED = new CompositeValueConverter<String[]>().add(COLLECTION_OR_ARRAY_TO_STRING_ARRAY).add(WHITESPACE_DELIMITED).add(OBJECT_TO_SINGLETON_STRING_ARRAY);
	public static final ValueConverter<String[]> DEFAULT_SPACE_DELIMITED = new CompositeValueConverter<String[]>().add(COLLECTION_OR_ARRAY_TO_STRING_ARRAY).add(SPACE_DELIMITED).add(OBJECT_TO_SINGLETON_STRING_ARRAY);
	public static final ValueConverter<String[]> DEFAULT_COMMA_DELIMITED = new CompositeValueConverter<String[]>().add(COLLECTION_OR_ARRAY_TO_STRING_ARRAY).add(COMMA_DELIMITED).add(OBJECT_TO_SINGLETON_STRING_ARRAY);
	public static final ValueConverter<String[]> DEFAULT_TAB_DELIMITED = new CompositeValueConverter<String[]>().add(COLLECTION_OR_ARRAY_TO_STRING_ARRAY).add(TAB_DELIMITED).add(OBJECT_TO_SINGLETON_STRING_ARRAY);
}
