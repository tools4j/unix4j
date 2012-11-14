package org.unix4j.convert;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.unix4j.convert.EnumConverters.StringToEnumConverter;
import org.unix4j.convert.OptionSetConverters.OptionSetConverter;
import org.unix4j.option.Option;
import org.unix4j.option.OptionSet;

/**
 * Default implementation for {@code ConverterRegistry} storing the converters
 * per value type in a {@link Map}. Converters for enums and {@link OptionSet} 
 * are created on the fly.
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
		registerValueConverter(String[].class, StringArrayConverters.DEFAULT);
		registerValueConverter(Date.class, DateConverters.DEFAULT);
		registerValueConverter(File.class, FileConverters.DEFAULT);
		registerValueConverter(InputStream.class, InputStreamConverters.DEFAULT);
		registerValueConverter(OutputStream.class, OutputStreamConverters.DEFAULT);
		registerValueConverter(URL.class, URLConverters.DEFAULT);
	}

	@Override
	public <V> ValueConverter<V> getValueConverterFor(Class<V> type) {
		@SuppressWarnings("unchecked")
		final ValueConverter<V> converter = (ValueConverter<V>) converterByType.get(type);
		if (converter != null) {
			return converter;
		}
		return getSpecialConverterFor(type);
	}
	
	protected <V> ValueConverter<V> getSpecialConverterFor(Class<V> type) {
		ValueConverter<V> converter;
		converter = getEnumConverterFor(type);
		if (converter != null) {
			return converter;
		}
		converter = getOptionSetConverterFor(type);
		return converter;
	}

	private <V> ValueConverter<V> getEnumConverterFor(Class<V> type) {
		if (type.isEnum()) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			final ValueConverter<V> enumConverter = new StringToEnumConverter(type);
			return enumConverter;
		}
		return null;
	}
	private <V> ValueConverter<V> getOptionSetConverterFor(Class<V> type) {
		if (OptionSet.class.isAssignableFrom(type)) {
			final Object emptyOptionSet;
			try {
				final Field field = type.getField("EMPTY");
				emptyOptionSet = field.get(null);//static field EMPTY
			} catch (Exception e) {
				//not convertible into an option set
				return null;
			}
			if (emptyOptionSet instanceof OptionSet) {
				final Class<?> optionType = ((OptionSet<?>)emptyOptionSet).optionType();
				if (optionType.isEnum() && Option.class.isAssignableFrom(optionType)) {
					@SuppressWarnings({ "rawtypes", "unchecked" })
					final ValueConverter<V> setConverter = new OptionSetConverter(optionType);
					return setConverter;
				}
			}
		}
		return null;
	}
	
	public <V> void registerValueConverter(Class<V> type, ValueConverter<V> converter) {
		converterByType.put(type, converter);
	}

}
