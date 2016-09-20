package org.unix4j.util.sort;

import java.util.Comparator;
import java.util.Objects;

/**
 * Comparator for strings considering only blanks and alphanumeric characters.
 */
public class DictionaryStringComparator implements Comparator<String> {


	private final Comparator<? super String> comparator;

	/**
	 * Constructor with delegate comparator comparing strings containing only
	 * blanks and alphanumeric characters.
	 *
	 * @param comparator the delegate comparator
     */
	public DictionaryStringComparator(final Comparator<? super String> comparator) {
		this.comparator = Objects.requireNonNull(comparator);
	}

	@Override
	public int compare(String s1, String s2) {
		return comparator.compare(dictionaryString(s1), dictionaryString(s2));
	}

	private static String dictionaryString(String s) {
		final int len = s.length();
		for (int i = 0; i < len; i++) {
			if (!isBlankOrAlphaNumeric(s.charAt(i))) {
				return convertToDictionaryString(s);
			}
		}
		return s;
	}

	private static String convertToDictionaryString(String s) {
		final int len = s.length();
		final StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			final char ch = s.charAt(i);
			if (isBlankOrAlphaNumeric(ch)) {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	private static boolean isBlankOrAlphaNumeric(char ch) {
		return Character.isWhitespace(ch) || Character.isDigit(ch) || Character.isLetter(ch);
	}
}