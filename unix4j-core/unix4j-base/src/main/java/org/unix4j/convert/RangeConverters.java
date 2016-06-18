package org.unix4j.convert;

import org.unix4j.util.Range;

import java.util.ArrayList;
import java.util.List;

public class RangeConverters {
	public static final ValueConverter<Range> STRING = new ValueConverter<Range>() {
		@Override
		public Range convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				final String[] values = value.toString().split("\\s*,\\s*");
				final List<Integer> indices = new ArrayList<Integer>();
				for (final String s : values) {
					final int dashIndex = s.indexOf('-');
					try {
						if (dashIndex < 0) {
							indices.add(Integer.parseInt(s));
						} else {
							if (dashIndex == 0) {
								//e.g.: -4
								indices.add(Integer.parseInt(s));
							} else if (dashIndex == s.length() - 1) {
								//e.g.: 4-
								indices.add(Integer.parseInt(s.substring(0, dashIndex)));
								indices.add(-Integer.MAX_VALUE);//FIXME not very nice, but works
							} else {
								//e.g. 4-8
								indices.add(Integer.parseInt(s.substring(0, dashIndex)));
								indices.add(Integer.parseInt(s.substring(dashIndex)));//include minus
							}
						}
					} catch (Exception e) {
						//we cannot parse range value, return null
						return null;
					}
				}
				if (!indices.isEmpty()) {
					return toRange(value, indices);
				}
			}
			return null;
		}
		private Range toRange(final Object value, final List<Integer> indices) {
			int lastIndex = 0;
			Range range = null;
			for (final int index : indices) {
				if (index == 0) {
					throw new IllegalArgumentException("invalid index 0 in range expression: " + value);
				}
				if (index > 0) {
					if (lastIndex > 0) {
						range = range == null ? Range.of(lastIndex) : range.andOf(lastIndex);
					}
					lastIndex = index;
				} else {
					final int start = lastIndex == 0 ? 1 : lastIndex;
					range = range == null ? Range.between(start, -index) : range.andBetween(start, -index);
					lastIndex = -1;
				}
			}
			if (lastIndex > 0) {
				return range == null ? Range.of(lastIndex) : range.andOf(lastIndex);
			}
			return range;
		}

	};
	public static final ValueConverter<Range> DEFAULT = STRING;
}
