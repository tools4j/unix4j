package org.unix4j.util.sort;

import org.unix4j.util.StringUtil;

import java.text.DateFormatSymbols;
import java.util.Comparator;
import java.util.Locale;

/**
 * A comparator Months: (unknown) < 'JAN' < ... < 'DEC'. The current locale
 * determines the month spellings.
 */
public class MonthStringComparator implements Comparator<CharSequence> {

	/**
	 * The instance for the default locale returned by {@link #getInstance()}.
	 */
	private static final MonthStringComparator DEFAULT_INSTANCE = new MonthStringComparator();

	/**
	 * Returns the instance for the default locale.
	 *
	 * @see Locale#getDefault()
	 */
	public static MonthStringComparator getInstance() {
		return DEFAULT_INSTANCE;
	}

	/**
	 * Returns an instance for the specified locale.
	 */
	public static MonthStringComparator getInstance(Locale locale) {
		return new MonthStringComparator(locale);
	}

	private final String[] months;
	private final String[] shortMonths;

	/**
	 * Private constructor used to create the {@link #DEFAULT_INSTANCE}.
	 */
	private MonthStringComparator() {
		this(DateFormatSymbols.getInstance());
	}

	/**
	 * Private constructor used by {@link #getInstance(Locale)}.
	 */
	private MonthStringComparator(Locale locale) {
		this(DateFormatSymbols.getInstance(locale));
	}

	/**
	 * Constructor with date symbols.
	 *
	 * @param symbols
	 *            the date symbols
	 */
	public MonthStringComparator(DateFormatSymbols symbols) {
		this.months = symbols.getMonths();
		this.shortMonths = symbols.getShortMonths();
	}

	@Override
	public int compare(CharSequence s1, CharSequence s2) {
		final int start1 = StringUtil.findStartTrimWhitespace(s1);
		final int end1 = StringUtil.findEndTrimWhitespace(s1);
		final int start2 = StringUtil.findStartTrimWhitespace(s2);
		final int end2 = StringUtil.findEndTrimWhitespace(s2);
		final int month1 = month(s1, start1, end1);
		final int month2 = month(s2, start2, end2);
		return Integer.compare(month1, month2);
	}

	private final int month(CharSequence s, int start, int end) {
		int m = month(months, s, start, end);
		return m >= 0 ? m : month(shortMonths, s, start, end);
	}

	private static int month(String[] months, CharSequence s, int start, int end) {
		for (int i = 0; i < months.length; i++) {
			final String month = months[i];
			if (!equalsIgnoreCase(month, s, start, end)) {
				continue;
			}
			return i;
		}
		return -1;
	}

	private static final boolean equalsIgnoreCase(String s1, CharSequence s2, int start, int end) {
		if (end - start != s1.length()) {
			return false;
		}
		for (int i = start; i < end; i++) {
			if (!equalsIgnoreCase(s1.charAt(i - start), s2.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	private static final boolean equalsIgnoreCase(char ch1, char ch2) {
		return Character.toUpperCase(ch1) == Character.toUpperCase(ch2);
	}
}