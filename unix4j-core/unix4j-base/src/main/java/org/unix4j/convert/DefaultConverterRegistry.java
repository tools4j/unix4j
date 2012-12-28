package org.unix4j.convert;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.unix4j.convert.EnumConverters.StringToEnumConverter;

/**
 * Default implementation for {@code ConverterRegistry} storing the converters
 * per value type in a {@link Map}. Converters for enums are created on the fly.
 */
public class DefaultConverterRegistry implements ConverterRegistry {

	private final Map<Class<?>, ValueConverter<?>> converterByType = new HashMap<Class<?>, ValueConverter<?>>();
	
	public DefaultConverterRegistry() {
		initConverters();
	}
	
	protected void initConverters() {
		registerValueConverter(Character.class, CharacterConverters.DEFAULT);
		registerValueConverter(char.class, CharacterConverters.DEFAULT);
		registerValueConverter(Integer.class, IntegerConverters.DEFAULT);
		registerValueConverter(int.class, IntegerConverters.DEFAULT);
		registerValueConverter(Long.class, LongConverters.DEFAULT);
		registerValueConverter(long.class, LongConverters.DEFAULT);
		registerValueConverter(String.class, StringConverters.DEFAULT);
		registerValueConverter(String[].class, StringArrayConverters.FLATTEN);
		registerValueConverter(File[].class, FileArrayConverters.FLATTEN);
		registerValueConverter(Object[].class, ArrayConverters.FLATTEN);
		registerValueConverter(Date.class, DateConverters.DEFAULT);
		registerValueConverter(File.class, FileConverters.DEFAULT);
		registerValueConverter(InputStream.class, InputStreamConverters.DEFAULT);
		registerValueConverter(OutputStream.class, OutputStreamConverters.DEFAULT);
		registerValueConverter(URL.class, URLConverters.DEFAULT);
		registerValueConverter(Pattern.class, PatternConverters.DEFAULT);
	}

	@Override
	public <V> ValueConverter<V> getValueConverterFor(Class<V> type) {
		@SuppressWarnings("unchecked")
		final ValueConverter<V> converter = (ValueConverter<V>) converterByType.get(type);
		if (converter != null) {
			return converter;
		}
		return getEnumConverterFor(type);
	}
	
	private <V> ValueConverter<V> getEnumConverterFor(Class<V> type) {
		if (type.isEnum()) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			final ValueConverter<V> enumConverter = new StringToEnumConverter(type);
			return enumConverter;
		}
		return null;
	}
	
	public <V> void registerValueConverter(Class<V> type, ValueConverter<V> converter) {
		converterByType.put(type, converter);
	}

}
