package org.unix4j.convert;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternConverters {
	public static final ValueConverter<Pattern> STRING = new ValueConverter<Pattern>() {
		@Override
		public Pattern convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				try {
					return Pattern.compile(value.toString());
				} catch (PatternSyntaxException e) {
					//ignore, we just can't convert this string
				}
			}
			return null;
		}
	};
	public static final ValueConverter<Pattern> DEFAULT = STRING;
}
