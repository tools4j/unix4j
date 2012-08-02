package org.unix4j.line;

/**
 * A {@link Line} implementation based on two {@link CharSequence}'s, usually
 * two strings one for the contents and one for the line ending characters.
 * <p>
 * As opposed to this implementation, the {@link SingleCharSequenceLine}
 * consists of a single character sequence for both the content string and the
 * line ending.
 */
public class SimpleLine implements Line {

	private final CharSequence content;
	private final CharSequence lineEnding;

	/**
	 * Constructor with contents and default operating system dependent line
	 * ending as defined by {@link Line#LINE_ENDING}.
	 * 
	 * @param content
	 *            the character sequence containing the {@link #getContent()
	 *            content} data of the line
	 * @throws NullPointerException
	 *             if {@code content} is null
	 * @see Line#LINE_ENDING
	 */
	public SimpleLine(CharSequence content) {
		this(content, LINE_ENDING);
	}

	/**
	 * Constructor with contents and lineEnding character sequences.
	 * 
	 * @param content
	 *            the character sequence containing the {@link #getContent()
	 *            content} data of the line
	 * @param lineEnding
	 *            the character sequence containing the {@link #getLineEnding()
	 *            line ending} characters, must have length zero, one or two
	 * @throws IllegalArgumentException
	 *             if {@code lineEnding} contains more than two characters
	 * @throws NullPointerException
	 *             if {@code content} or {@code LineEvent} is null
	 */
	public SimpleLine(CharSequence content, CharSequence lineEnding) {
		if (content == null) {
			throw new NullPointerException("content cannot be null");
		}
		final int elen = lineEnding.length();
		if (elen > 2) {
			throw new IllegalArgumentException("lineEndingLength must be a string of length two or less, but was found to be " + elen + ": " + lineEnding);
		}
		this.content = content;
		this.lineEnding = lineEnding;
	}

	@Override
	public int length() {
		return content.length() + lineEnding.length();
	}

	@Override
	public char charAt(int index) {
		return index < content.length() ? content.charAt(index) : lineEnding.charAt(index - content.length());
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		final int clen = content.length();
		final int elen = lineEnding.length();
		if (start < clen && end <= clen) {
			return content.subSequence(start, end);
		}
		if (start - clen < elen && end - clen <= elen) {
			return lineEnding.subSequence(start - clen, end - clen);
		}
		// overlaps the two strings
		final StringBuilder sb = new StringBuilder(clen + elen);
		return sb.append(content).append(lineEnding).subSequence(start, end);
	}

	@Override
	public String getContent() {
		return content.toString();
	}

	@Override
	public int getContentLength() {
		return content.length();
	}

	@Override
	public String getLineEnding() {
		return lineEnding.toString();
	}

	@Override
	public int getLineEndingLength() {
		return lineEnding.length();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(content.length() + lineEnding.length());
		return sb.append(content).append(lineEnding).toString();
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
