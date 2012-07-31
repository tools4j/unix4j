package org.unix4j.line;

import java.io.IOException;
import java.io.Writer;

/**
 * A {@link Line} implementation based on a single {@link CharSequence} such as
 * a {@link StringBuilder} or {@link String}. The sequence starts with the
 * {@link #getContent() content} string and ends with the
 * {@link #getLineEnding() line ending}.
 * <p>
 * As opposed to this implementation, the {@link SimpleLine} consists of two
 * separate character sequences for content and line ending.
 */
public class SingleCharSequenceLine implements Line {

	private final CharSequence charSequence;
	private final int offset;
	private final int contentLength;
	private final int lineEndingLength;

	/**
	 * Constructor with character sequence with the data and the
	 * {@code lineEndingLength} indicating whether there is one or two
	 * characters for the line ending. The character sequence is read from the
	 * first character with offset zero.
	 * 
	 * @param charSequence
	 *            the character sequence containing the data
	 * @param lineEndingLength
	 *            the number of characters making up the line ending; must be
	 *            one or two
	 * @throws IllegalArgumentException
	 *             if {@code lineEndingLength} is negative or larger than two
	 */
	public SingleCharSequenceLine(CharSequence charSequence, int lineEndingLength) {
		this(charSequence, 0, charSequence.length() - lineEndingLength, lineEndingLength);
	}

	/**
	 * Constructor with character sequence with the data and {@link offset}
	 * pointing to the first content character of the line. The
	 * {@code contentLength} and {@code lineEndingLength} parameters define the
	 * respective parts in {@code buffer}.
	 * 
	 * @param charSequence
	 *            the character sequence containing the data
	 * @param offset
	 *            the offset containing the first character of line
	 * @param contentLength
	 *            the number of characters making up the content of the line
	 *            without line ending; must be non-negative
	 * @param lineEndingLength
	 *            the number of characters making up the line ending; must be
	 *            zero, one or two (zero only for the last line)
	 * @throws IllegalArgumentException
	 *             if {@code offset} or {@code contentLength} are negative or
	 *             {@code lineEndingLength} is negative or larger than two
	 * @throws IndexOutOfBoundsException
	 *             if the buffer length is smaller than
	 *             {@code offset+contentLength+lineEndingLength}
	 */
	public SingleCharSequenceLine(CharSequence charSequence, int offset, int contentLength, int lineEndingLength) {
		if (offset < 0) {
			throw new IllegalArgumentException("offset must be non-negative, but was found to be " + offset);
		}
		if (contentLength < 0) {
			throw new IllegalArgumentException("contentLength must be non-negative, but was found to be " + contentLength);
		}
		if (lineEndingLength < 0 || lineEndingLength > 2) {
			throw new IllegalArgumentException("lineEndingLength must between zero and two, but was found to be " + lineEndingLength);
		}
		if (offset + contentLength + lineEndingLength > charSequence.length()) {
			throw new IndexOutOfBoundsException("line end is after charSequence end: " + (offset + contentLength + lineEndingLength) + " > " + charSequence.length());
		}
		this.charSequence = charSequence;
		this.offset = offset;
		this.contentLength = contentLength;
		this.lineEndingLength = lineEndingLength;
	}

	@Override
	public int length() {
		return contentLength + lineEndingLength;
	}

	@Override
	public char charAt(int index) {
		return charSequence.charAt(offset + index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return charSequence.subSequence(offset + start, offset + end - start);
	}

	@Override
	public String getContent() {
		return charSequence.subSequence(offset, offset + contentLength).toString();
	}

	@Override
	public int getContentLength() {
		return contentLength;
	}

	@Override
	public String getLineEnding() {
		return charSequence.subSequence(offset + contentLength, offset + contentLength + lineEndingLength).toString();
	}

	@Override
	public int getLineEndingLength() {
		return lineEndingLength;
	}

	@Override
	public void write(Writer writer) {
		try {
			writer.write(charSequence.toString(), offset, contentLength + lineEndingLength);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return charSequence.subSequence(offset, offset + contentLength + lineEndingLength).toString();
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (obj instanceof Line) {
			return toString().equals(obj.toString());
		}
		return false;
	}

}
