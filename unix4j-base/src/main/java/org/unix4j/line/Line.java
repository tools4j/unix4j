package org.unix4j.line;

/**
 * A line is a line string including the line ending character(s). The
 * {@link #getContent()} method returns the line without line ending characters;
 * {@link #getLineEnding()} returns the only the line ending.
 * <p>
 * Note that line ending characters are not necessarily line separator
 * characters for the current operating system as defined by the
 * {@code "line.separator"} {@link System#getProperties() system property}. The
 * concrete encoding of line endings depends on the source of the line, which
 * could for instance be an existing Windows or Unix file.
 * <p>
 * It is illegal for the {@link #getContent() content} part of a line to contain 
 * any character of the {@link #getLineEnding() line ending} part. A line source 
 * must ensure that this constraint is fulfilled. Algorithms and programs may 
 * return undefined results or behave unpredictably for lines not following this 
 * rule.
 */
public interface Line extends CharSequence {
	/**
	 * Operating system dependent line ending taken from the system property
	 * {@code "line.separator"}. This is usually {@code "\n"} on UNIX systems
	 * and {@code "\r\n"} on WINDOWS.
	 */
	String LINE_ENDING = System.getProperty("line.separator");

	/**
	 * Line with empty content string and a default operating system dependent
	 * line ending as defined by {@link #LINE_ENDING}.
	 */
	Line EMPTY_LINE = new SimpleLine("");

	/**
	 * The line feed (LF) character {@code '\n'} used to encode line endings in
	 * UNIX.
	 */
	char LF = '\n';

	/**
	 * The carriage return (CR) character {@code '\r'} used to encode line
	 * endings in WINDOWS together with {@link #LF}.
	 */
	char CR = '\r';

	/**
	 * The zero character {@code '\0'} used to encode line endings for special
	 * purpose use, such as {@code find --print0 | xargs --delimiter0}.
	 */
	char ZERO = '\0';

	/**
	 * Returns the contents making up this line, but without the line ending
	 * characters. Returns an empty string if the line consists only of line
	 * ending characters.
	 * 
	 * @return the line contents without line ending characters; can be an empty
	 *         string but never null
	 */
	String getContent();

	/**
	 * Returns the length of the content string returned by
	 * {@link #getContent()}, which is zero for a line without content and
	 * positive otherwise.
	 * 
	 * @return the non-negative length of the content string returned by
	 *         {@link #getContent()}
	 */
	int getContentLength();

	/**
	 * Returns the line ending characters, usually one or two characters such as
	 * {@code "\n"} or {@code "\r\n"}. If the last line in a file is not
	 * terminated with a new line character, the returned string can have zero
	 * length.
	 * <p>
	 * The encoding of line endings depends on the source of this line object.
	 * It is a single <i>line feed</i> character (LF={@code "\n"}) if the source
	 * is a Unix type file, and <i>carriage return</i> followed by <i>line
	 * feed</i> (CR+LF= {@code "\r\n"}) for lines from a Windows file.
	 * 
	 * @return the line ending characters, usually a one or two character string
	 *         and sometimes empty for the last line
	 */
	String getLineEnding();

	/**
	 * Returns the length of the line ending string returned by
	 * {@link #getLineEnding()}, which is usually one or two and sometimes zero
	 * the last line in a file is not terminated with a new line character.
	 * 
	 * @return the length of the line ending string, usually one or two and
	 *         sometimes zero for the last line
	 */
	int getLineEndingLength();

	/**
	 * Returns this line including the line ending characters as a string.
	 * 
	 * @return the line with line ending
	 */
	@Override
	String toString();

	/**
	 * Compares {@code this} line with {@code obj} and returns true if
	 * {@code obj} is a {@link Line} (any subclass) and both lines are
	 * identical. The line comparison considers both the {@link #getContent()
	 * content} and the {@link #getLineEnding() line ending}.
	 * 
	 * @param obj
	 *            the object to be compared with
	 * @return true if {@code obj} is a {@code Line} identical to {@code this}
	 *         {@code Line} also considering the line ending when comparing the
	 *         lines
	 */
	@Override
	boolean equals(Object obj);

	/**
	 * The hash code for this line, based on the complete line with line ending.
	 * 
	 * @return the hash code based on content and line ending
	 */
	@Override
	public int hashCode();
}
