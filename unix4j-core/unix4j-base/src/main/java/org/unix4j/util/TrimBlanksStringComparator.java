package org.unix4j.util;

import java.util.Comparator;

/**
 * Comparator for strings trimming leading or trailing blanks or both before
 * forwarding the actual comparison to a delegate comparator.
 */
public class TrimBlanksStringComparator implements Comparator<String> {

	/**
	 * Mode defining which end of the strings should be trimmed for blanks.
	 */
	public static enum Mode {
		/** Only leading blanks are trimmed */
		Leading, 
		/** Only trailing blanks are trimmed */
		Trailing, 
		/** Both leading and trailing blanks are trimmed */
		Both
	}

	private final Mode mode;
	private final Comparator<? super String> comparator;

	/**
	 * Constructor with mode and delegate comparator performing the actual
	 * string comparison on the trimmed strings.
	 * 
	 * @param mode
	 *            the mode defining which end of the strings should be trimmed
	 * @param comparator
	 *            delegate comparator performing the actual string comparison
	 */
	public TrimBlanksStringComparator(Mode mode, Comparator<? super String> comparator) {
		this.mode = mode;
		this.comparator = comparator;
	}

	@Override
	public int compare(String s1, String s2) {
		final int start1 = findStart(s1);
		final int end1 = findEnd(s1);
		final int start2 = findStart(s2);
		final int end2 = findEnd(s2);
		return comparator.compare(s1.substring(start1, end1), s2.substring(start2, end2));
	}

	private int findStart(String s) {
		if (mode == Mode.Trailing) return 0;
		final int len = s.length();
		for (int i = 0; i < len; i++) {
			final char ch = s.charAt(i);
			if (ch != ' ' && ch != '\t') {
				return i;
			}
		}
		return len;
	}

	private int findEnd(String s) {
		final int len = s.length();
		if (mode == Mode.Leading) return len;
		for (int i = len; i > 0; i--) {
			final char ch = s.charAt(i-1);
			if (ch != ' ' && ch != '\t') {
				return i;
			}
		}
		return 0;
	}
}