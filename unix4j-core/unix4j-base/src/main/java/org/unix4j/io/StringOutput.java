package org.unix4j.io;

import java.io.StringWriter;

/**
 * Writes the output lines to a string.
 */
public class StringOutput extends WriterOutput {

	private final boolean writeLastLineEnding;

	/**
	 * Constructor for {@code StringOutput} that does NOT terminated the last
	 * line with a line ending no matter whether the last input line has an
	 * empty line ending or not.
	 */
	public StringOutput() {
		this(false);
	}

	/**
	 * Constructor with flag indicating whether the last line ending should be
	 * written or not when {@link #finish()} is called.
	 * 
	 * @param writeLastLineEnding
	 *            if true the line ending of the last line is written to the
	 *            output
	 */
	public StringOutput(boolean writeLastLineEnding) {
		super(new StringWriter());
		this.writeLastLineEnding = writeLastLineEnding;
	}

	@Override
	protected StringWriter getWriter() {
		return (StringWriter) super.getWriter();
	}

	@Override
	public boolean writeLastLineEnding() {
		return writeLastLineEnding;
	}

	/**
	 * Return the buffer's current value as a string.
	 */
	@Override
	public String toString() {
		return getWriter().toString();
	}
}
