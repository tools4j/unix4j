package org.unix4j.convert;

import java.util.Collection;

public class ArrayConverters {
	public static final ValueConverter<Object[]> COLLECTION_TO_ARRAY = new ValueConverter<Object[]>() {
		@Override
		public Object[] convert(Object value) throws IllegalArgumentException {
			if (value instanceof Collection) {
				return ((Collection<?>)value).toArray();
			}
			return null;
		}
	};
	public static final ValueConverter<Object[]> OBJECT_TO_SINGLETON_ARRAY = new ValueConverter<Object[]>() {
		@Override
		public Object[] convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return new Object[] {value};
			}
			return null;
		}
	};
	
	public static final ValueConverter<Object[]> COLLECTION_OR_ARRAY_TO_FLAT_ARRAY = new ConcatenatedConverter<Object[]>(ListConverters.COLLECTION_OR_ARRAY_TO_FLAT_LIST, COLLECTION_TO_ARRAY);
	public static final ValueConverter<Object[]> DEFAULT = new CompositeValueConverter<Object[]>().add(COLLECTION_TO_ARRAY).add(OBJECT_TO_SINGLETON_ARRAY);
	public static final ValueConverter<Object[]> FLATTEN = new CompositeValueConverter<Object[]>().add(COLLECTION_OR_ARRAY_TO_FLAT_ARRAY).add(OBJECT_TO_SINGLETON_ARRAY);
}
