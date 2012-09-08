package org.unix4j.processor;

import org.unix4j.io.Input;
import org.unix4j.line.Line;

/**
 * An {@code InputProcessor} is a program or device that processes line based
 * data from an {@link Input} device. It is very similar to a
 * {@link LineProcessor} but has an extra {@link #begin(Input, LineProcessor)
 * begin(..)} method and both the input and the output device are passed to
 * every method.
 */
public interface InputProcessor {
	/**
	 * Indicates that the line processing task is about to start for the
	 * specified {@code input} device.
	 * 
	 * @param input
	 *            the input device whose lines are about to be processed next
	 * @param output
	 *            the output to write to
	 */
	void begin(Input input, LineProcessor output);

	/**
	 * Processes a single line and returns true if this {@code InputProcessor}
	 * is ready to process more lines. Returning false indicates that the
	 * process can be {@link #finish(Input, LineProcessor) finished} because
	 * subsequent lines would not change the result anyway.
	 * 
	 * @param input
	 *            the input device, the source of the given {@code line}
	 * @param line
	 *            the line to process
	 * @param output
	 *            the output to write to
	 * @return true if this {@code InputProcessor} is ready to process more
	 *         lines, and false if it does not require any more input lines
	 */
	boolean processLine(Input input, Line line, LineProcessor output);

	/**
	 * Indicates that this line processing task is complete for the specified
	 * {@code input} device and can finished.
	 * <p>
	 * Simple output devices usually perform a {@code flush} operation in this
	 * method. More complex commands may perform the real operation in this
	 * method, for instance write the total count of lines or words to the
	 * target file or stream.
	 * 
	 * @param input
	 *            the input device whose lines have now all been processed
	 * @param output
	 *            the output to write to
	 */
	void finish(Input input, LineProcessor output);
}
