package org.unix4j.convert;

import java.io.File;
import java.net.URL;

public class FileConverters {
	public static final ValueConverter<File> URL_TO_FILE = new ValueConverter<File>() {
		@Override
		public File convert(Object value) throws IllegalArgumentException {
			if (value instanceof URL) {
				return new File(((URL)value).getFile());
			}
			return null;
		}
	};
	public static final ValueConverter<File> URL = ConcatenatedConverter.concat(URLConverters.DEFAULT, URL_TO_FILE);
	public static final ValueConverter<File> STRING = new ValueConverter<File>() {
		@Override
		public File convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				return new File(value.toString());
			}
			return null;
		}
	};
	public static final ValueConverter<File> DEFAULT = new CompositeValueConverter<File>().add(URL).add(STRING);
}
