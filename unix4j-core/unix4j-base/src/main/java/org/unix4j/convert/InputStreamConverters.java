package org.unix4j.convert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class InputStreamConverters {
	public static final ValueConverter<InputStream> URL_TO_STREAM = new ValueConverter<InputStream>() {
		@Override
		public InputStream convert(Object value) throws IllegalArgumentException {
			if (value instanceof URL) {
				try {
					return ((URL)value).openStream();
				} catch (IOException e) {
					return null;
				}
			}
			return null;
		}
	};
	public static final ValueConverter<InputStream> URL = ConcatenatedConverter.concat(URLConverters.DEFAULT, URL_TO_STREAM);
	
	public static final ValueConverter<InputStream> FILE_TO_STREAM = new ValueConverter<InputStream>() {
		@Override
		public InputStream convert(Object value) throws IllegalArgumentException {
			if (value instanceof File) {
				try {
					return new FileInputStream((File)value);
				} catch (IOException e) {
					return null;
				}
			}
			return null;
		}
	};
	public static final ValueConverter<InputStream> FILE = ConcatenatedConverter.concat(FileConverters.DEFAULT, FILE_TO_STREAM);
	
	public static final ValueConverter<InputStream> DEFAULT = new CompositeValueConverter<InputStream>().add(URL).add(FILE);
}
