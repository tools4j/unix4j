package org.unix4j.io;

import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;

/**
 * Line-oriented output destination for a command.
 */
public interface Output extends LineProcessor {
	/**
	 * Writes a line to the output.
	 * 
	 * @param line
	 *            the line to write to the output.
	 * 
	 * @return true if this {@code Output} device is ready to process more
	 *         lines, and false otherwise; output devices usually return true
	 */
	@Override
	boolean processLine(Line line);

	/**
	 * Indicates that all output has been written. Usually triggers a flush
	 * operation or closes the underlying reader or stream.
	 */
	@Override
	void finish();
}
