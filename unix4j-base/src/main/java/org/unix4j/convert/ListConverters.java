package org.unix4j.convert;

import java.lang.reflect.Array;
import java.util.*;

public class ListConverters {
	public static final ValueConverter<List<?>> COLLECTION_TO_LIST = new ValueConverter<List<?>>() {
		@Override
		public List<?> convert(Object value) throws IllegalArgumentException {
			if (value instanceof Collection) {
				if (value instanceof List) {
					return (List<?>)value;
				}
				return new ArrayList<Object>((Collection<?>)value);
			}
			return null;
		}
	};
	public static final ValueConverter<List<Object>> OBJECT_ARRAY_TO_LIST = new ValueConverter<List<Object>>() {
		@Override
		public List<Object> convert(Object value) throws IllegalArgumentException {
			if (value instanceof Object[]) {
				return Arrays.asList((Object[])value);
			}
			return null;
		}
	};
	public static final ValueConverter<List<Object>> ANY_ARRAY_TO_LIST = new ValueConverter<List<Object>>() {
		@Override
		public List<Object> convert(Object value) throws IllegalArgumentException {
			if (value != null && value.getClass().isArray()) {
				final int len = Array.getLength(value);
				final List<Object> result = new ArrayList<Object>(len);
				for (int i = 0; i < len; i++) {
					final Object element = Array.get(value, i);
					result.add(element);
				}
				return result;
			}
			return null;
		}
	};
	public static final ValueConverter<List<Object>> ARRAY_TO_LIST = new CompositeValueConverter<List<Object>>().add(OBJECT_ARRAY_TO_LIST).add(ANY_ARRAY_TO_LIST);

	public static final ValueConverter<List<Object>> OBJECT_TO_SINGLETON_LIST = new ValueConverter<List<Object>>() {
		@Override
		public List<Object> convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return Collections.singletonList(value);
			}
			return null;
		}
	};
	
	public static final ValueConverter<List<?>> COLLECTION_TO_FLAT_LIST = new ValueConverter<List<?>>() {
		@Override
		public List<?> convert(Object value) throws IllegalArgumentException {
			if (value instanceof Collection) {
				final Collection<?> source = (Collection<?>)value;
				final List<Object> target = new ArrayList<Object>(source.size());
				for (final Object element : source) {
					final List<?> unnested = COLLECTION_OR_ARRAY_TO_FLAT_LIST.convert(element);
					if (unnested == null) {
						target.add(element);
					} else {
						target.addAll(unnested);
					}
				}
				return target;
			}
			return null;
		}
	};
	public static final ValueConverter<List<?>> ARRAY_TO_FLAT_LIST = new ConcatenatedConverter<List<?>>(ARRAY_TO_LIST, COLLECTION_TO_FLAT_LIST);
	public static final ValueConverter<List<?>> COLLECTION_OR_ARRAY_TO_FLAT_LIST = new CompositeValueConverter<List<?>>().add(COLLECTION_TO_FLAT_LIST).add(ARRAY_TO_FLAT_LIST);

	public static final ValueConverter<List<?>> DEFAULT = new CompositeValueConverter<List<?>>().add(COLLECTION_TO_LIST).add(ARRAY_TO_LIST).add(OBJECT_TO_SINGLETON_LIST);
	public static final ValueConverter<List<?>> FLATTEN = new CompositeValueConverter<List<?>>().add(COLLECTION_OR_ARRAY_TO_FLAT_LIST).add(OBJECT_TO_SINGLETON_LIST);
}
