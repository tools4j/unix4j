package org.unix4j.util;

import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.Locale;

/**
 * Comparator for strings trimming leading or trailing blanks or both before
 * forwarding the actual comparison to a delegate comparator.
 */
public class NumericComparator implements Comparator<CharSequence> {

	/**
	 * Mode defining the number type to expect when comparing two strings.
	 */
	public static enum Mode {
		/** 
		 * Decimal strings consisting of optional blanks, an optional '-' sign, 
		 * and zero or more digits possibly separated by thousands separators, 
		 * optionally followed by a decimal-point character and zero or more 
		 * digits. An empty number is treated as '0'. 
		 */
		Decimal {
			private final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
			@Override
			public int compare(CharSequence s1, int start1, int end1, CharSequence s2, int start2, int end2) {
				final char decimalSeparator = symbols.getDecimalSeparator();
				final char groupingSeparator = symbols.getGroupingSeparator();
				final boolean neg1 = start1 < end1 && s1.charAt(start1) == '-';
				final boolean neg2 = start2 < end2 && s2.charAt(start2) == '-';
				int index1 = neg1 ? start1 + 1 : start1;
				int index2 = neg2 ? start2 + 1 : start2;
				int cmp = 0;
				int digits = 0;
				boolean isDecimal = false;
				while (cmp == 0) {
					digits++;
					final char ch1 = getNextChar(s1, index1, end1, digits, groupingSeparator);
					final char ch2 = getNextChar(s2, index2, end2, digits, groupingSeparator);
					if (Character.isDigit(ch1) && Character.isDigit(ch2)) {
						cmp = Character.compare(ch1, ch2);
						index1++;
						index2++;
					} else {
						//FIXME wrong below
						if (Character.isDigit(ch1)) {
							cmp = 1;
						} else if (Character.isDefined(ch2)) {
							cmp = -1;
						} else {
							return 0;
						}
					}
				}
				if (neg1) {
					return neg2 ? -cmp : -1;
				} else {
					return neg2 ? 1 : cmp;
				}
			}
			private char getNextChar(CharSequence s1, int index1, int end1, int digits, char groupingSeparator) {
				if (index1 < end1) {
					final char ch = s1.charAt(index1);
					if (Character.isDigit(ch)) return ch;
					if (ch == groupingSeparator) {
						if (digits % 3 == 0) {
							return getNextChar(s1, index1 + 1, end1, 1, groupingSeparator);
						}
					}
					return ch;
				}
				return '\n';
			}
		}, 
		/** 
		 * Comparison based on {@link Double} floating point numbers. This 
		 * allows floating point numbers to be specified in scientific notation, 
		 * like 1.0e-34 and 10e100. The default {@link Locale} determines the 
		 * decimal-point character.
		 * */
		Scientific {
			@Override
			public int compare(CharSequence s1, int start1, int end1, CharSequence s2, int start2, int end2) {
				final double dbl1 = parseDouble(s1.subSequence(start1, end1));
				final double dbl2 = parseDouble(s2.subSequence(start2, end2));
				return Double.compare(dbl1, dbl2);
			}
		};
		abstract public int compare(CharSequence s1, int start1, int end1, CharSequence s2, int start2, int end2);
	}

	private final Mode mode;

	/**
	 * Constructor with mode and delegate comparator performing the actual
	 * string comparison on the trimmed strings.
	 * 
	 * @param mode
	 *            the mode defining which end of the strings should be trimmed
	 * @param comparator
	 *            delegate comparator performing the actual string comparison
	 */
	public NumericComparator(Mode mode, Comparator<? super String> comparator) {
		this.mode = mode;
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {
		final int start1 = TrimBlanksStringComparator.findStartTrimBlanks(s1);
		final int end1 = TrimBlanksStringComparator.findEndTrimBlanks(s1);
		final int start2 = TrimBlanksStringComparator.findStartTrimBlanks(s2);
		final int end2 = TrimBlanksStringComparator.findEndTrimBlanks(s2);
		return mode.compare(s1, start1, end1, s2, start2, end2);
	}

	private static double parseDouble(CharSequence s) {
		try {
			return Double.parseDouble(s.toString());
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}
}