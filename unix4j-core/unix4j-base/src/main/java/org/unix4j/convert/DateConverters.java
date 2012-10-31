package org.unix4j.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverters {
	public static final ValueConverter<Date> LONG_TO_DATE = new ValueConverter<Date>() {
		@Override
		public Date convert(Object value) throws IllegalArgumentException {
			if (value instanceof Long) {
				return new Date(((Long)value).longValue());
			}
			return null;
		}
	};
	public static class StringToDateConverter implements ValueConverter<Date> {
		private final DateFormat format;
		public StringToDateConverter(String pattern) {
			this(new SimpleDateFormat(pattern));
		}
		public StringToDateConverter(DateFormat format) {
			this.format = format;
		}
		@Override
		public Date convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				try {
					return format.parse(value.toString());
				} catch (ParseException e) {
					return null;
				}
			}
			return null;
		}
	}
	public static final StringToDateConverter STRING_TO_DATE = new StringToDateConverter(DateFormat.getDateInstance());
	
	public static final ValueConverter<Date> DEFAULT = new CompositeValueConverter<Date>().add(LONG_TO_DATE).add(STRING_TO_DATE);
}
