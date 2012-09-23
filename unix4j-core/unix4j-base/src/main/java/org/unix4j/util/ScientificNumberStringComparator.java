package org.unix4j.util;

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
		final int start1 = TrimBlanksStringComparator.findStartTrimBlanks(s1);
		final int end1 = TrimBlanksStringComparator.findEndTrimBlanks(s1);
		final int start2 = TrimBlanksStringComparator.findStartTrimBlanks(s2);
		final int end2 = TrimBlanksStringComparator.findEndTrimBlanks(s2);
		final CharSequence sub1 = s1.subSequence(start1, end1);
		final CharSequence sub2 = s2.subSequence(start2, end2);
		final double dbl1 = parseDouble(sub1);
		final double dbl2 = parseDouble(sub2);
		final boolean isNan1 = Double.isNaN(dbl1); 
		final boolean isNan2 = Double.isNaN(dbl2); 
		final boolean isNonDouble1 = isNan1 && !"NaN".equalsIgnoreCase(sub1.toString()); 
		final boolean isNonDouble2 = isNan2 && !"NaN".equalsIgnoreCase(sub2.toString()); 
		if (isNonDouble1 || isNonDouble2) {
			if (isNonDouble1 && isNonDouble2) {
				return sub1.toString().compareTo(sub2.toString());
			}
			if (isNonDouble1) return -1;
			if (isNonDouble2) return 1;
			throw new RuntimeException("code shoudl never get here: " + s1 + " / " + s2);
		}
		return Double.compare(dbl1, dbl2);
	}
	private static double parseDouble(CharSequence s) {
		try {
			return Double.parseDouble(s.toString());
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}
}