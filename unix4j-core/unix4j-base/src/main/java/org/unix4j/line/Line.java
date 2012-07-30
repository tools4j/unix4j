package org.unix4j.line;

import java.io.Writer;

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
 */
public interface Line extends CharSequence {
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
	 * Uses the specified {@code writer} to output this line including line
	 * ending characters.
	 * 
	 * @param writer
	 *            the writer used to write this line to an output device
	 */
	void write(Writer writer);

	/**
	 * Returns this line including the line ending characters as a string.
	 * 
	 * @return the line with line ending
	 */
	@Override
	public String toString();
}
