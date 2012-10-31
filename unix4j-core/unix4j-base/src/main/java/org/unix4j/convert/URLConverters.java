package org.unix4j.convert;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class URLConverters {
	public static final ValueConverter<URL> URI = new ValueConverter<URL>() {
		@Override
		public URL convert(Object value) throws IllegalArgumentException {
			if (value instanceof URI) {
				try {
					return ((URI)value).toURL();
				} catch (Exception E) {
					return null;
				}
			}
			return null;
		}
	};
	public static final ValueConverter<URL> STRING = new ValueConverter<URL>() {
		@Override
		public URL convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				try {
					return new URL(value.toString());
				} catch (MalformedURLException e) {
					return null;
				}
			}
			return null;
		}
	};
	public static final ValueConverter<URL> DEFAULT = new CompositeValueConverter<URL>().add(URI).add(STRING);
}
