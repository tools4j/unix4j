package org.unix4j.util.sort;

import org.unix4j.util.StringUtil;

import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * A comparator for decimal strings sorted numerically, first by numeric sign; then by SI suffix
 * (either empty, or 'k' or 'K', or one of 'MGTPEZY', in that order); and finally by numeric value.
 * For example, '1023M' sorts before '1G' because 'M' (mega) precedes 'G' (giga) as an SI suffix.
 * <p>
 * The syntax for numbers is the same as for {@link DecimalNumberStringComparator}; the SI suffix must
 * immediately follow the number.
 */
public class UnitsNumberStringComparator implements Comparator<CharSequence> {

	private static final String SI_UNITS = "KMGTPEZY";

	/**
	 * The instance for the default locale returned by {@link #getInstance()}.
	 */
	private static final UnitsNumberStringComparator DEFAULT_INSTANCE = new UnitsNumberStringComparator();

	/**
	 * Returns the instance for the default locale.
	 *
	 * @see Locale#getDefault()
	 */
	public static UnitsNumberStringComparator getInstance() {
		return DEFAULT_INSTANCE;
	}

	/**
	 * Returns an instance for the specified locale.
	 */
	public static UnitsNumberStringComparator getInstance(Locale locale) {
		return new UnitsNumberStringComparator(locale);
	}

	private final DecimalNumberStringComparator numberComparator;

	/**
	 * Private constructor used to create the {@link #DEFAULT_INSTANCE}.
	 */
	private UnitsNumberStringComparator() {
		this(DecimalNumberStringComparator.getInstance());
	}

	/**
	 * Private constructor used by {@link #getInstance(Locale)}.
	 */
	private UnitsNumberStringComparator(Locale locale) {
		this(DecimalNumberStringComparator.getInstance(locale));
	}

	/**
	 * Constructor with decimal number comparator.
	 *
	 * @param numberComparator
	 *            the decimal number comparator
	 */
	public UnitsNumberStringComparator(DecimalNumberStringComparator numberComparator) {
		this.numberComparator = Objects.requireNonNull(numberComparator);
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {
		final int start1 = StringUtil.findStartTrimWhitespace(s1);
		final int end1 = StringUtil.findEndTrimWhitespace(s1);
		final int start2 = StringUtil.findStartTrimWhitespace(s2);
		final int end2 = StringUtil.findEndTrimWhitespace(s2);
		final boolean isNeg1 = start1 < end1 && s1.charAt(start1) == '-';
		final boolean isNeg2 = start2 < end2 && s2.charAt(start2) == '-';
		final int si1 = siUnit(s1, start1, end1);
		final int si2 = siUnit(s2, start2, end2);
		final int e1 = si1 < 0 ? end1 : end1 - 1;
		final int e2 = si2 < 0 ? end2 : end2 - 1;
		final int cmp = numberComparator.compare(s1, start1, e1, s2, start2, e2);
		if (isNeg1 != isNeg2) {
			return cmp;
		}
		if (si1 < si2) {
			return isNeg1 ? 1 : -1;
		}
		if (si1 > si2) {
			return isNeg1 ? -1 : 1;
		}
		return cmp;
	}

	private static final int siUnit(CharSequence s, int start, int end) {
		if (start < end) {
			final int ch = s.charAt(end - 1);
			final int si = SI_UNITS.indexOf(ch);
			if (si >= 0) {
				return si;
			}
			if (ch == 'k') return 0;//lower case only for k
		}
		return -1;
	}
}