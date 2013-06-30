package org.unix4j.util;

import java.util.ArrayList;
import java.util.List;

import org.unix4j.line.Line;
import org.unix4j.line.SingleCharSequenceLine;

/**
 * Utility class with static methods for strings.
 */
public class StringUtil {
	/**
	 * Returns the given {@code value} as a string of fixed length {@code size}
	 * padding or truncating the value if necessary.
	 * <p>
	 * If {@code left==true}, the given {@code value} is left-aligned appending
	 * the given {@code filler} character to make up the fixed length. If the
	 * given {@code value} turns out to be longer than {@code size} when
	 * transformed into a string, it is truncated from the right.
	 * <p>
	 * If {@code left==false}, the given {@code value} is right-aligned and
	 * {@code filler} characters are added from the left if necessary. If
	 * {@code value} is longer than {@code size} it is truncated from the left.
	 * <p>
	 * Examples with {@code size=3}:
	 * <ul>
	 * <li>left=true, filler=' ', value=89 --> "89 "</li>
	 * <li>left=true, filler=' ', value=1234 --> "123"</li>
	 * <li>left=false, filler=' ', value=89 --> " 89"</li>
	 * <li>left=false, filler='0', value=89 --> "089"</li>
	 * <li>left=false, filler=' ', value=1234 --> "234"</li>
	 * </ul>
	 * 
	 * @param size
	 *            the fixed size of the returned string
	 * @param alignLeft
	 *            true if {@code value} should be left-aligned
	 * @param filler
	 *            the filler character if {@code value} is shorter than
	 *            {@code size}
	 * @param value
	 *            the value to format
	 * @return the value as a fixed size string, padded or truncated if
	 *         necessary
	 */
	public static final String fixSizeString(int size, boolean alignLeft,
			char filler, long value) {
		return fixSizeString(size, alignLeft, filler, String.valueOf(value));
	}

	/**
	 * Returns the given string {@code s} into a string of fixed length
	 * {@code size} padding or truncating the string with spaces if necessary.
	 * <p>
	 * If {@code left==true}, the given string {@code s} is left-aligned
	 * appending spaces to make up the fixed length. If {@code s} turns out to
	 * be longer than {@code size} it is truncated from the right.
	 * <p>
	 * If {@code left==false}, the given string {@code s} is right-aligned and
	 * space characters are added from the left if necessary. If {@code s} is
	 * longer than {@code size} it is truncated from the left.
	 * <p>
	 * Examples with {@code size=3}:
	 * <ul>
	 * <li>left=true, s="XY" --> "XY "</li>
	 * <li>left=true, s="Abcd" --> "Abc"</li>
	 * <li>left=false, s="XY" --> " XY"</li>
	 * <li>left=false, s="Abcd" --> "bcd"</li>
	 * </ul>
	 * 
	 * @param size
	 *            the fixed size of the returned string
	 * @param alignLeft
	 *            true if {@code value} should be left-aligned
	 * @param s
	 *            the string to format
	 * @return the string {@code s} as a fixed size string, padded or truncated
	 *         if necessary
	 */
	public static final String fixSizeString(int size, boolean alignLeft,
			String s) {
		return fixSizeString(size, alignLeft, ' ', s);
	}

	/**
	 * Returns the given string {@code s} into a string of fixed length
	 * {@code size} padding or truncating the string if necessary.
	 * <p>
	 * If {@code left==true}, the given string {@code s} is left-aligned
	 * appending the given {@code filler} character to make up the fixed length.
	 * If {@code s} turns out to be longer than {@code size} it is truncated
	 * from the right.
	 * <p>
	 * If {@code left==false}, the given string {@code s} is right-aligned and
	 * {@code filler} characters are added from the left if necessary. If
	 * {@code s} is longer than {@code size} it is truncated from the left.
	 * <p>
	 * Examples with {@code size=3}:
	 * <ul>
	 * <li>left=true, filler=' ', s="XY" --> "XY "</li>
	 * <li>left=true, filler=' ', s="Abcd" --> "Abc"</li>
	 * <li>left=false, filler=' ', s="XY" --> " XY"</li>
	 * <li>left=false, filler='0', s="12" --> "012"</li>
	 * <li>left=false, filler=' ', s="Abcd" --> "bcd"</li>
	 * </ul>
	 * 
	 * @param size
	 *            the fixed size of the returned string
	 * @param alignLeft
	 *            true if {@code value} should be left-aligned
	 * @param filler
	 *            the filler character if {@code s} is shorter than {@code size}
	 * @param s
	 *            the string to format
	 * @return the string {@code s} as a fixed size string, padded or truncated
	 *         if necessary
	 */
	public static final String fixSizeString(int size, boolean alignLeft,
			char filler, String s) {
		if (s.length() < size) {
			final StringBuilder sb = new StringBuilder(size);
			if (alignLeft)
				sb.append(s);
			for (int i = 0; i < size - s.length(); i++) {
				sb.append(filler);
			}
			if (!alignLeft)
				sb.append(s);
			return sb.toString();
		} else {
			return alignLeft ? s.substring(0, size) : s.substring(s.length()
					- size, s.length());
		}
	}

	/**
	 * Splits the given string into lines and returns each line as a separate
	 * string in the result list. The result list will contain at least one
	 * entry unless the string is empty.
	 * <p>
	 * A trailing newline after the last line is ignored, meaning that no empty
	 * string is appended as separate line if the string ends with a newline.
	 * However multiple trailing newlines will still lead to empty line strings
	 * at the end of the list.
	 * <p>
	 * Note that all line ending characters are accepted to split lines, no
	 * matter what operating system this code is hosted on. More precisely, the
	 * {@link Line#LF LF} and {@link Line#CR CR} characters are recognized as
	 * line ending characters, either as single character or as a pair
	 * {@code CR+LF} or {@code LF+CR}.
	 * 
	 * @param s
	 *            the string to split
	 * @return a list with the lines found in {@code s}
	 */
	public static final List<Line> splitLines(String s) {
		final List<Line> lines = new ArrayList<Line>();
		int start = 0;
		int index = 0;
		while (index < s.length()) {
			final char ch = s.charAt(index);
			if (ch == Line.LF || ch == Line.CR) {
				final int lineEndingStart = index;
				index++;
				if (index < s.length()) {
					final char ch2 = s.charAt(index);
					if (ch2 != ch && (ch2 == Line.LF || ch2 == Line.CR)) {
						index++;
					}
				}
				final Line line = new SingleCharSequenceLine(s, start,
						lineEndingStart - start, index - lineEndingStart);
				lines.add(line);
				start = index;
			} else {
				index++;
			}
		}
		if (start < s.length()) {
			final Line line = new SingleCharSequenceLine(s, start, s.length()
					- start, 0);
			lines.add(line);
		}
		return lines;
	}

	/**
	 * Finds and returns the start of the given sequence after trimming
	 * whitespace characters from the left.
	 * 
	 * @param s
	 *            the character sequence
	 * @return the index containing the first non-whitespace character, or the
	 *         length of the character sequence if all characters are blank
	 */
	public static int findStartTrimWhitespace(CharSequence s) {
		return findStartTrimWhitespace(s, 0);
	}

	/**
	 * Finds and returns the start of the given sequence after trimming
	 * whitespace characters from the left, starting at the given {@code start}
	 * index.
	 * 
	 * @param s
	 *            the character sequence
	 * @param start
	 *            the first index to consider in the char sequence
	 * @return the index containing the first non-whitespace character, or the
	 *         length of the character sequence if all characters are blank
	 */
	public static int findStartTrimWhitespace(CharSequence s, int start) {
		final int len = s.length();
		for (int i = start; i < len; i++) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return i;
			}
		}
		return len;
	}

	/**
	 * Finds and returns the end of the given character sequence after trimming
	 * white space characters from the right. Whitespace characters are defined
	 * by {@link Character#isWhitespace(char)}. .
	 * 
	 * @param s
	 *            the character sequence
	 * @return the index after the last non-whitespace character, or zero if all
	 *         characters are blank
	 */
	public static int findEndTrimWhitespace(CharSequence s) {
		for (int i = s.length(); i > 0; i--) {
			if (!Character.isWhitespace(s.charAt(i - 1))) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Finds and returns the start of the given sequence after trimming newline
	 * characters from the left. The following character sequences are treated
	 * as newline characters: "\n", "\r\n".
	 * 
	 * @param s
	 *            the character sequence
	 * @return the index containing the first character that is not part of a
	 *         newline sequence, or the length of the character sequence if all
	 *         characters are newline chars
	 */
	public static int findStartTrimNewlineChars(CharSequence s) {
		return findStartTrimNewlineChars(s, 0);
	}

	/**
	 * Finds and returns the start of the given sequence after trimming newline
	 * characters from the left, starting at the given {@code start} index. .
	 * The following character sequences are treated as newline characters:
	 * "\n", "\r\n".
	 * 
	 * @param s
	 *            the character sequence
	 * @param start
	 *            the first index to consider in the char sequence
	 * @return the index containing the first character that is not part of a
	 *         newline sequence, or the length of the character sequence if all
	 *         characters are newline chars
	 */
	public static int findStartTrimNewlineChars(CharSequence s, int start) {
		final int len = s.length();
		for (int i = start; i < len;) {
			final int ch = s.charAt(i);
			i++;
			if (ch != '\n') {
				if (ch != '\r' || i >= len || s.charAt(i) != '\n') {
					return i - 1;
				}
				i++;// increment again, it was "\r\n"
			}
		}
		return len;
	}

	/**
	 * Finds and returns the end of the given character sequence after trimming
	 * new line characters from the right. The following character sequences are
	 * treated as newline characters: "\n", "\r\n".
	 * 
	 * @param s
	 *            the character sequence
	 * @return the index after the last character that is not part of a newline
	 *         sequence, or zero if all characters are newline chars
	 */
	public static int findEndTrimNewlineChars(CharSequence s) {
		for (int i = s.length(); i > 0;) {
			if (s.charAt(i - 1) != '\n') {
				return i;
			}
			i--;
			if (i > 0 && s.charAt(i - 1) == '\r') {
				i--;
			}
		}
		return 0;
	}

	/**
	 * Finds and returns the first whitespace character in the given sequence,
	 * or the length of the string if no whitespace is found.
	 * 
	 * @param s
	 *            the character sequence
	 * @return the index containing the first whitespace character, or the
	 *         length of the character sequence if all characters are blank
	 */
	public static int findWhitespace(CharSequence s) {
		return findWhitespace(s, 0);
	}

	/**
	 * Finds and returns the first whitespace character in the given sequence at
	 * or after start. Returns the length of the string if no whitespace is
	 * found.
	 * 
	 * @param s
	 *            the character sequence
	 * @param start
	 *            the first index to consider in the char sequence
	 * @return the index containing the first whitespace character at or after
	 *         start, or the length of the character sequence if all characters
	 *         are blank
	 */
	public static int findWhitespace(CharSequence s, int start) {
		final int len = s.length();
		for (int i = start; i < len; i++) {
			if (Character.isWhitespace(s.charAt(i))) {
				return i;
			}
		}
		return len;
	}

	/**
	 * Returns true if and only if the string {@code s} contains the specified
	 * target string performing case insensitive string comparison.
	 * 
	 * @param s
	 *            the sequence to search for
	 * @return true if this string contains <code>s</code>, false otherwise
	 * @throws NullPointerException
	 *             if <code>s</code> is <code>null</code>
	 */
	public static boolean containsIgnoreCase(String source, String target) {
		return 0 <= indexOfIgnoreCase(source, target);
	}

	/**
	 * Tests if this string {@code s} starts with the specified prefix
	 * performing case insensitive string comparison.
	 * 
	 * @param s
	 *            the string to search
	 * @param prefix
	 *            the prefix.
	 * @return <code>true</code> if the character sequence represented by the
	 *         argument is a prefix of the character sequence represented by the
	 *         string s; <code>false</code> otherwise. Note also that
	 *         <code>true</code> will be returned if the argument is an empty
	 *         string or is equal to this <code>String</code> object as
	 *         determined by the {@link #equals(Object)} method.
	 */
	public static boolean startsWithIgnoreCase(String s, String prefix) {
		return 0 == indexOfIgnoreCase(s, prefix, 0);
	}

	/**
	 * Returns the index within the source string of the first occurrence of the
	 * specified target substring performing case insensitive string comparison.
	 * 
	 * 
	 * <p>
	 * The returned index is the smallest value <i>k</i> for which: <blockquote>
	 * 
	 * <pre>
	 * startsWithIgnoreCase(source.substring(<i>k</i>), target)
	 * </pre>
	 * 
	 * </blockquote> If no such value of <i>k</i> exists, then {@code -1} is
	 * returned.
	 * 
	 * <p>
	 * Copied from {@code String.indexOf(..)} modified to do case-insensitive
	 * search. The source is the character array being searched, and the target
	 * is the string being searched for.
	 * 
	 * @param str
	 *            the substring to search for.
	 * @return the index of the first occurrence of the specified substring
	 *         (ignoring the case), or {@code -1} if there is no such
	 *         occurrence.
	 */
	public static final int indexOfIgnoreCase(String source, String target) {
		return indexOfIgnoreCase(source, target, Integer.MAX_VALUE);
	}

	/**
	 * 
	 * @param source
	 *            the characters being searched.
	 * @param target
	 *            the characters being searched for.
	 * @param maxIndex
	 *            the maximum index to return (for instance 0 if only the start
	 *            of the string is of interest)
	 */
	private static int indexOfIgnoreCase(String source, String target,
			int maxIndex) {
		if (maxIndex < 0) {
			throw new IllegalArgumentException("maxIndex cannot be negative: "
					+ maxIndex);
		}
		final int sourceCount = source.length();
		final int targetCount = target.length();
		final char first = target.charAt(0);
		int max = Math.min(maxIndex, sourceCount - targetCount);

		for (int i = 0; i <= max; i++) {
			final char ch = source.charAt(i);
			/* Look for first character. */
			if (!equalsIgnoreCase(ch, first)) {
				while (++i <= max && !equalsIgnoreCase(ch, first))
					;
			}

			/* Found first character, now look at the rest of v2 */
			if (i <= max) {
				int j = i + 1;
				int end = j + targetCount - 1;
				for (int k = 1; j < end && equalsIgnoreCase(source.charAt(j), target.charAt(k)); j++, k++)
					;

				if (j == end) {
					/* Found whole string. */
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Returns true if the two characters are equal if case is ignored.
	 * 
	 * @param ch1
	 *            the first character
	 * @param ch2
	 *            the second character
	 * @return true if both characters are the same according to
	 *         case-insensitive comparison
	 */
	public static boolean equalsIgnoreCase(char ch1, char ch2) {
		if (ch1 == ch2)
			return true;
		// If characters try converting both characters to uppercase
		ch1 = Character.toUpperCase(ch1);
		ch2 = Character.toUpperCase(ch2);
		if (ch1 == ch2)
			return true;
		// Unfortunately, conversion to uppercase does not work properly
		// for the Georgian alphabet, which has strange rules about case
		// conversion. So we need to make one last check before
		// exiting.
		return Character.toLowerCase(ch1) == Character.toLowerCase(ch2);
	}

	// no instances
	private StringUtil() {
		super();
	}

}
