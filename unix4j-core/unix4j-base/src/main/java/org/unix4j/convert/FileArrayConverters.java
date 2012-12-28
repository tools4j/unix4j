package org.unix4j.convert;

import java.io.File;

public class FileArrayConverters {
	public static final ValueConverter<File[]> FILE_ARRAY_TO_FILE_ARRAY = new ValueConverter<File[]>() {
		@Override
		public File[] convert(Object value) throws IllegalArgumentException {
			if (value instanceof File[]) {
				return (File[])value;
			}
			return null;
		}
	};
	public static final ValueConverter<File[]> OBJECT_ARRAY_TO_FILE_ARRAY = new ValueConverter<File[]>() {
		@Override
		public File[] convert(Object value) throws IllegalArgumentException {
			if (value instanceof Object[]) {
				final Object[] array = (Object[])value;
				final int len = array.length;
				final File[] result = new File[len];
				for (int i = 0; i < len; i++) {
					final Object element = array[i];
					result[i] = FileConverters.STRING.convert(element);
				}
				return result;
			}
			return null;
		}
	};
	public static final ValueConverter<File[]> OBJECT_TO_SINGLETON_FILE_ARRAY = new ValueConverter<File[]>() {
		@Override
		public File[] convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return new File[] {FileConverters.STRING.convert(value)};
			}
			return null;
		}
	};
	public static final ValueConverter<File[]> ARRAY_TO_FILE_ARRAY = new CompositeValueConverter<File[]>().add(FILE_ARRAY_TO_FILE_ARRAY).add(OBJECT_ARRAY_TO_FILE_ARRAY);
	public static final ValueConverter<File[]> COLLECTION_TO_FILE_ARRAY = new ConcatenatedConverter<File[]>(ArrayConverters.COLLECTION_TO_ARRAY, OBJECT_ARRAY_TO_FILE_ARRAY);
	
	public static final ValueConverter<File[]> COLLECTION_OR_ARRAY_TO_FILE_ARRAY = new CompositeValueConverter<File[]>().add(COLLECTION_TO_FILE_ARRAY).add(ARRAY_TO_FILE_ARRAY);
	public static final ValueConverter<File[]> COLLECTION_OR_ARRAY_TO_FLAT_FILE_ARRAY = new ConcatenatedConverter<File[]>(ListConverters.COLLECTION_OR_ARRAY_TO_FLAT_LIST, COLLECTION_TO_FILE_ARRAY);
	
	public static final ValueConverter<File[]> DEFAULT = new CompositeValueConverter<File[]>().add(COLLECTION_OR_ARRAY_TO_FILE_ARRAY).add(OBJECT_TO_SINGLETON_FILE_ARRAY);
	public static final ValueConverter<File[]> FLATTEN = new CompositeValueConverter<File[]>().add(COLLECTION_OR_ARRAY_TO_FLAT_FILE_ARRAY).add(OBJECT_TO_SINGLETON_FILE_ARRAY);
}
