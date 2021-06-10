package org.unix4j.util.sort;

import org.unix4j.util.StringUtil;

import java.util.Comparator;

/** 
 * Comparison based on {@link Double} floating point numbers. This 
 * allows floating point numbers to be specified in scientific notation, 
 * like 1.0e-34 and 10e100 using {@link Double#parseDouble(String)}. Leading
 * and trailing strings are ignored. If the string is not a valid number, it is
 * treated as {@link Double#NaN} during the comparison.
 */
public class ScientificNumberStringComparator implements Comparator<CharSequence> {
	
	/**
	 * The singleton instance.
	 */
	public static final ScientificNumberStringComparator INSTANCE = new ScientificNumberStringComparator();
	
	/**
	 * Private constructor for the singleton {@link #INSTANCE}.
	 */
	private ScientificNumberStringComparator () {
		super();
	}
	
	@Override
	public int compare(CharSequence s1, CharSequence s2) {
		final int start1 = StringUtil.findStartTrimWhitespace(s1);
		final int end1 = StringUtil.findEndTrimWhitespace(s1);
		final int start2 = StringUtil.findStartTrimWhitespace(s2);
		final int end2 = StringUtil.findEndTrimWhitespace(s2);
		final String str1 = s1.subSequence(start1, end1).toString();
		final String str2 = s2.subSequence(start2, end2).toString();
		final double dbl1 = parseDouble(str1);
		final double dbl2 = parseDouble(str2);
		final boolean isNan1 = Double.isNaN(dbl1); 
		final boolean isNan2 = Double.isNaN(dbl2); 
		if (isNan1 || isNan2) {
			final boolean isNonDouble1 = isNan1 && !"NaN".equals(str1); 
			final boolean isNonDouble2 = isNan2 && !"NaN".equals(str2); 
			if (isNonDouble1 && isNonDouble2) {
				return str1.compareTo(str2);
			}
			if (isNonDouble1) return -1;
			if (isNonDouble2) return 1;
		}
		return Double.compare(dbl1, dbl2);
	}
	private static double parseDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}
}