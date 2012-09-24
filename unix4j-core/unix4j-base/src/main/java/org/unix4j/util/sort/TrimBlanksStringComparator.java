package org.unix4j.util.sort;

import java.util.Comparator;

import org.unix4j.util.StringUtil;

/**
 * Comparator for strings or character sequences trimming leading or trailing
 * blanks or both before forwarding the actual comparison to a delegate
 * comparator.
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
	private final Comparator<? super CharSequence> comparator;

	/**
	 * Constructor with mode and delegate comparator performing the actual
	 * string comparison on the trimmed strings or character sequences.
	 * 
	 * @param mode
	 *            the mode defining which end of the character sequence should
	 *            be trimmed
	 * @param comparator
	 *            delegate comparator performing the actual comparison after
	 *            trimming
	 */
	public TrimBlanksStringComparator(Mode mode, Comparator<? super CharSequence> comparator) {
		this.mode = mode;
		this.comparator = comparator;
	}

	@Override
	public int compare(String s1, String s2) {
		final int start1 = findStart(s1);
		final int end1 = findEnd(s1);
		final int start2 = findStart(s2);
		final int end2 = findEnd(s2);
		return comparator.compare(s1.subSequence(start1, end1), s2.subSequence(start2, end2));
	}

	private int findStart(String s) {
		if (mode == Mode.Trailing)
			return 0;
		return findStartTrimBlanks(s);
	}

	/**
	 * Finds and returns the start of the given character sequence after
	 * trimming spaces and tabs.
	 * 
	 * @param s
	 *            the character sequence
	 * @return the index containing the first non-blank character, or the length
	 *         of the character sequence if all characters are blank
	 * @see StringUtil#findStartTrimWhitespace(CharSequence)
	 */
	static int findStartTrimBlanks(CharSequence s) {
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
		if (mode == Mode.Leading)
			return s.length();
		return findEndTrimBlanks(s);
	}

	/**
	 * Finds and returns the end of the given character sequence after trimming
	 * spaces and tabs.
	 * 
	 * @param s
	 *            the character sequence
	 * @return the index after the last non-blank character, or zero if all
	 *         characters are blank
	 * @see StringUtil#findEndTrimWhitespace(CharSequence)
	 */
	static int findEndTrimBlanks(CharSequence s) {
		for (int i = s.length(); i > 0; i--) {
			final char ch = s.charAt(i - 1);
			if (ch != ' ' && ch != '\t') {
				return i;
			}
		}
		return 0;
	}
}