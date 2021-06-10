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
	 *            a single line ending character
	 * @throws IllegalArgumentException
	 *             if {@code lineEnding} contains more than two characters
	 * @throws NullPointerException
	 *             if {@code content} or {@code LineEvent} is null
	 */
	public SimpleLine(CharSequence content, char lineEnding) {
		this(content, String.valueOf(lineEnding));
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
		final int estart = start - clen;
		final int eend = end - clen;
		if (estart >= 0 && estart <= elen && eend >= 0 && eend <= elen) {
			return lineEnding.subSequence(estart, eend);
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
	
	/**
	 * Returns a sub-line of the given {@code line}, similar to a substring or
	 * the method {@link #subSequence(int, int)}. The difference is that the
	 * result is again a proper line, with or without line ending depending on
	 * the cutting point and the {@code preserveLineEnding} flag.
	 * 
	 * @param line	the line to cut
	 * @param start	the start index (inclusive), valid from 0 to {@code end}
	 * @param end	the end index (exclusive), valid from 0 to 
	 * 				{@code line.length()} if  {@code preserveLineEnding=false}, 
	 * 				and from 0 to {@code line.getContentLength()} if 
	 * 				{@code preserveLineEnding=true}
	 * @param preserveLineEnding	if true, the line's ending is preserved and
	 * 								copied to the returned line (only the 
	 * 								content of the line is cut in this case)
	 * 								
	 * @return a new line representing a sub-line of the input line
	 */
	public static Line subLine(Line line, int start, int end, boolean preserveLineEnding) {
		if (start == 0 && (end == line.length() || (preserveLineEnding && end == line.getContentLength()))) {
			return line;
		}
		if (start < 0) {
			throw new IllegalArgumentException("start cannot be negative: " + start);
		}
		if (end < 0) {
			throw new IllegalArgumentException("end cannot be negative: " + end);
		}
		if (start > end) {
			throw new IllegalArgumentException("start cannot be after end: " + start + " > " + end);
		}
		if (preserveLineEnding) {
			if (end > line.getContentLength()) {
				throw new IllegalArgumentException("start cannot be after line content end: " + end + " > " + line.getContentLength());
			}
			return new SimpleLine(line.subSequence(start, end), line.getLineEnding());
		} else {
			final int clen = line.getContentLength();
			if (end > line.length()) {
				throw new IllegalArgumentException("start cannot be after line end: " + end + " > " + line.length());
			}
			if (end <= clen) {
				return new SimpleLine(line.subSequence(start, end), "");
			} else {
				if (start < clen) {
					return new SimpleLine(line.subSequence(start, clen), line.subSequence(line.getContentLength(), end));
				} else {
					return new SimpleLine("", line.subSequence(start, end));
				}
			}
		}
	}
}
