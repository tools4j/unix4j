package org.unix4j.operation;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

/**
 * An interface to operate on input lines. Output lines can be written to an
 * output object. An input line can lead to single or multiple output lines or
 * to no output at all.
 */
public interface LineOperation {
	/**
	 * Performs an operation for the given input line. Result lines can be
	 * written to the provided output object. If the
	 * {@link LineProcessor#finish()} method of the output object is called, no
	 * further calls are expected for this execution context and lines
	 * subsequently written to the output object will be ignored.
	 * 
	 * @param context
	 *            the execution context
	 * @param input
	 *            the input line
	 * @param output
	 *            the output object to write result lines
	 */
	void operate(ExecutionContext context, Line input, LineProcessor output);
}
