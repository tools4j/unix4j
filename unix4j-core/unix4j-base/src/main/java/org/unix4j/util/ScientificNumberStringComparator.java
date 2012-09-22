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
		final double dbl1 = parseDouble(s1.subSequence(start1, end1));
		final double dbl2 = parseDouble(s2.subSequence(start2, end2));
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