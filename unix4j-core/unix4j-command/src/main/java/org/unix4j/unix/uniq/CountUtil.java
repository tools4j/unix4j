package org.unix4j.unix.uniq;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.StringUtil;

/**
 * Helper class to format count output of the uniq command.
 */
class CountUtil {

	/**
	 * Formats the given {@code count} value to the fixed string length
	 * {@code maxDigitsForCount} padding it with spaces from the left if
	 * necessary. Note that the returned string can be longer than
	 * {@code maxDigitsForCount} as values are never truncated.
	 * 
	 * @param count
	 *            the count value to format
	 * @param countDigits
	 *            the number of digits to use, padding the given value with
	 *            spaces from left if necessary
	 * @return the formatted count string, the right-aligned {@code value}
	 *         padded with spaces from the left if necessary
	 */
	public static String formatCount(long count, int countDigits) {
		final String scount = String.valueOf(count);
		return scount.length() <= countDigits ? StringUtil.fixSizeString(countDigits, false, ' ', count) : scount;
	}

	/**
	 * Writes the given line prefixed with the count using the format <br>
	 * &nbsp;&nbsp;&nbsp;{@code ' ' + <count> + ' ' + <line>} <br>
	 * and using {@link #formatCount(long, int)} to format {@code <count>}.
	 * 
	 * @param line
	 *            the line to write to the output
	 * @param count
	 *            the count written before the line
	 * @param countDigits
	 *            the number of digits to use for {@code <count>}, padding the
	 *            given value with spaces from left if necessary
	 * @param output
	 *            the output device to write to
	 * @return the length of the formatted count string as returned by
	 *         {@code #formatCount(long, int)}
	 */
	public static int writeCountLine(Line line, long count, int countDigits, LineProcessor output) {
		final String countString = formatCount(count, countDigits);
		final Line outputLine = new SimpleLine(" " + countString + " " + line.getContent(), line.getLineEnding());
		output.processLine(outputLine);
		return countString.length();
	}

	// no instances
	private CountUtil() {
		super();
	}
}
