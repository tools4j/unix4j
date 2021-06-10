package org.unix4j.convert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class OutputStreamConverters {
	public static final ValueConverter<OutputStream> URL_TO_STREAM = new ValueConverter<OutputStream>() {
		@Override
		public OutputStream convert(Object value) throws IllegalArgumentException {
			if (value instanceof URL) {
				try {
					return ((URL)value).openConnection().getOutputStream();
				} catch (IOException e) {
					return null;
				}
			}
			return null;
		}
	};
	public static final ValueConverter<OutputStream> URL = ConcatenatedConverter.concat(URLConverters.DEFAULT, URL_TO_STREAM);
	
	public static final ValueConverter<OutputStream> FILE_TO_STREAM = new ValueConverter<OutputStream>() {
		@Override
		public OutputStream convert(Object value) throws IllegalArgumentException {
			if (value instanceof File) {
				try {
					return new FileOutputStream((File)value);
				} catch (IOException e) {
					return null;
				}
			}
			return null;
		}
	};
	public static final ValueConverter<OutputStream> FILE = ConcatenatedConverter.concat(FileConverters.DEFAULT, FILE_TO_STREAM);
	
	public static final ValueConverter<OutputStream> DEFAULT = new CompositeValueConverter<OutputStream>().add(URL).add(FILE);
}
