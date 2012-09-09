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
	 * @param left
	 *            true if {@code value} should be left-aligned
	 * @param filler
	 *            the filler character if {@code value} is shorter than
	 *            {@code size}
	 * @param value
	 *            the value to format
	 * @return the value as a fixed size string, padded or truncated if
	 *         necessary
	 */
	public static final String fixSizeString(int size, boolean left, char filler, long value) {
		return fixSizeString(size, left, filler, String.valueOf(value));
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
	 * @param left
	 *            true if {@code value} should be left-aligned
	 * @param s
	 *            the string to format
	 * @return the string {@code s} as a fixed size string, padded or truncated
	 *         if necessary
	 */
	public static final String fixSizeString(int size, boolean left, String s) {
		return fixSizeString(size, left, ' ', s);
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
	 * @param left
	 *            true if {@code value} should be left-aligned
	 * @param filler
	 *            the filler character if {@code s} is shorter than {@code size}
	 * @param s
	 *            the string to format
	 * @return the string {@code s} as a fixed size string, padded or truncated
	 *         if necessary
	 */
	public static final String fixSizeString(int size, boolean left, char filler, String s) {
		if (s.length() < size) {
			final StringBuilder sb = new StringBuilder(size);
			if (left)
				sb.append(s);
			for (int i = 0; i < size - s.length(); i++) {
				sb.append(filler);
			}
			if (!left)
				sb.append(s);
			return sb.toString();
		} else {
			return left ? s.substring(0, size) : s.substring(s.length() - size, s.length());
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
				final Line line = new SingleCharSequenceLine(s, start, lineEndingStart - start, index - lineEndingStart);
				lines.add(line);
				start = index;
			} else {
				index++;
			}
		}
		if (start < s.length()) {
			final Line line = new SingleCharSequenceLine(s, start, s.length() - start, 0);
			lines.add(line);
		}
		return lines;
	}

	// no instances
	private StringUtil() {
		super();
	}

}
