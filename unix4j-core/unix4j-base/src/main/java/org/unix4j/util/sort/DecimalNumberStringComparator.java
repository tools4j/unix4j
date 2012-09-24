package org.unix4j.util.sort;

import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.Locale;

/**
 * A comparator for decimal strings consisting of optional blanks, an optional
 * '-' sign, and zero or more digits possibly separated by thousands separators,
 * optionally followed by a decimal-point character and zero or more digits. An
 * empty number is treated as '0'.
 */
public class DecimalNumberStringComparator implements Comparator<CharSequence> {

	/**
	 * The instance for the default locale returned by {@link #getInstance()}.
	 */
	private static final DecimalNumberStringComparator DEFAULT_INSTANCE = new DecimalNumberStringComparator();

	/**
	 * Returns the instance for the default locale.
	 * 
	 * @see Locale#getDefault()
	 */
	public static DecimalNumberStringComparator getInstance() {
		return DEFAULT_INSTANCE;
	}

	/**
	 * Returns an instance for the specified locale.
	 */
	public static DecimalNumberStringComparator getInstance(Locale locale) {
		return new DecimalNumberStringComparator(locale);
	}

	private final DecimalFormatSymbols symbols;

	/**
	 * Private constructor used to create the {@link #DEFAULT_INSTANCE}.
	 */
	private DecimalNumberStringComparator() {
		this(DecimalFormatSymbols.getInstance());
	}

	/**
	 * Private constructor used by {@link #getInstance(Locale)}.
	 */
	private DecimalNumberStringComparator(Locale locale) {
		this(DecimalFormatSymbols.getInstance(locale));
	}

	/**
	 * Constructor with decimal symbols.
	 * 
	 * @param symbols
	 *            the decimal symbols
	 */
	public DecimalNumberStringComparator(DecimalFormatSymbols symbols) {
		this.symbols = symbols;
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {
		final int start1 = TrimBlanksStringComparator.findStartTrimBlanks(s1);
		final int start2 = TrimBlanksStringComparator.findStartTrimBlanks(s2);
		return compare(s1, start1, s1.length(), s2, start2, s2.length());
	}

	private int compare(CharSequence s1, int start1, int end1, CharSequence s2, int start2, int end2) {
		final char decimalSeparator = symbols.getDecimalSeparator();
		final char groupingSeparator = symbols.getGroupingSeparator();
		final char zeroDigit = symbols.getZeroDigit();
		final boolean isNeg1 = start1 < end1 && s1.charAt(start1) == '-';
		final boolean isNeg2 = start2 < end2 && s2.charAt(start2) == '-';
		int index1 = skipLeadingZeroChars(s1, isNeg1 ? start1 + 1 : start1, end1, zeroDigit);
		int index2 = skipLeadingZeroChars(s2, isNeg2 ? start2 + 1 : start2, end2, zeroDigit);
		int cmp = 0;
		int digits = 0;
		boolean isZero1 = true;
		boolean isZero2 = true;
		while (index1 < end1 || index2 < end2) {
			if (digits % 3 == 0 && digits > 0) {
				index1 = skipGroupingSeparatorChars(s1, index1, end1, groupingSeparator);
				index2 = skipGroupingSeparatorChars(s2, index2, end2, groupingSeparator);
			}
			final char ch1 = index1 < end1 ? s1.charAt(index1) : '\n';
			final char ch2 = index2 < end2 ? s2.charAt(index2) : '\n';
			final boolean isDigit1 = Character.isDigit(ch1);
			final boolean isDigit2 = Character.isDigit(ch2);
			if (isDigit1 && isDigit2) {
				digits++;
				isZero1 &= (isDigit1 && ch1 == zeroDigit);
				isZero2 &= (isDigit2 && ch2 == zeroDigit);
				if (cmp == 0) {
					cmp = Character.compare(ch1, ch2);
				}
				index1++;
				index2++;
			} else if (ch1 == decimalSeparator || ch2 == decimalSeparator) {
				return compareAfterDecimals(s1, index1, end1, ch1, isNeg1, isZero1, s2, index2, end2, ch2, isNeg2, isZero2, cmp);
			} else {
				if (isDigit1) {
					return applySign(1, isNeg1, isNeg2);
				} else if (isDigit2) {
					return applySign(-1, isNeg1, isNeg2);
				} else {
					if (cmp == 0) {
						cmp = Character.compare(ch1, ch2);
					}
					index1++;
					index2++;
				}
			}
		}
		return applySign(cmp, isNeg1 && !isZero1, isNeg2 && !isZero2);
	}
	private int compareAfterDecimals(CharSequence s1, int index1, int end1, char ch1, boolean isNeg1, boolean isZero1, CharSequence s2, int index2, int end2, char ch2, boolean isNeg2, boolean isZero2, int cmp) {
		final char decimalSeparator = symbols.getDecimalSeparator();
		final char groupingSeparator = symbols.getGroupingSeparator();
		final char zeroDigit = symbols.getZeroDigit();
		boolean isDigit1 = Character.isDigit(ch1); 
		boolean isDigit2 = Character.isDigit(ch2); 
		final boolean isDecimal1 = ch1 == decimalSeparator; 
		final boolean isDecimal2 = ch2 == decimalSeparator; 
		
		if (isDigit1 && !isDecimal1 && isDecimal2) {
			return applySign(1, isNeg1, isNeg2);
		} else if (isDigit2 && isDecimal1 && !isDecimal2) {
			return applySign(-1, isNeg1, isNeg2);
		}
		//both integer parts have ended, hence...
		if (cmp != 0) {
			return applySign(cmp, isNeg1 && !isZero1, isNeg2 && !isZero2);
		}
		if (isDecimal1) {
			index1++;
		}
		if (isDecimal2) {
			index2++;
		}
		int digits = 0;
		while (cmp == 0 && (index1 < end1 || index2 < end2)) {
			if (digits % 3 == 0 && digits > 0) {
				index1 = skipGroupingSeparatorChars(s1, index1, end1, groupingSeparator);
				index2 = skipGroupingSeparatorChars(s2, index2, end2, groupingSeparator);
			}
			ch1 = index1 < end1 ? s1.charAt(index1) : '\n';
			ch2 = index2 < end2 ? s2.charAt(index2) : '\n';
			isDigit1 = Character.isDigit(ch1);
			isDigit2 = Character.isDigit(ch2);
			if (isDigit1 && isDigit2) {
				digits++;
				isZero1 &= (isDigit1 && ch1 == zeroDigit);
				isZero2 &= (isDigit2 && ch2 == zeroDigit);
				cmp = Character.compare(ch1, ch2);
				index1++;
				index2++;
			} else {
				if (isDigit1) {
					if (ch1 == zeroDigit && isDecimal1) {
						index1++;
					} else {
						return applySign(1, isNeg1, isNeg2);
					}
				} else if (isDigit2) {
					if (ch2 == zeroDigit && isDecimal2) {
						index2++;
					} else {
						return applySign(-1, isNeg1, isNeg2);
					}
				} else {
					cmp = Character.compare(ch1, ch2);
					index1++;
					index2++;
				}
			}
		}
		return applySign(cmp, isNeg1 && !isZero1, isNeg2 && !isZero2);
	}

	private int applySign(int cmp, boolean isNeg1, boolean isNeg2) {
		if (isNeg1) {
			return isNeg2 ? -cmp : -1;
		} else {
			return isNeg2 ? 1 : cmp;
		}
	}

	private int skipLeadingZeroChars(CharSequence s, int index, int end, char zeroDigit) {
		while (index < end) {
			final char ch = s.charAt(index);
			if (ch == zeroDigit) {
				index++;
			} else {
				return index;
			}
		}
		return end;
	}
	private int skipGroupingSeparatorChars(CharSequence s, int index, int end, char groupingSeparator) {
		if (index < end && s.charAt(index) == groupingSeparator) {
			return index + 1;
		}
		return index;
	}
}