package org.unix4j.redirect;

import org.unix4j.command.Command;
import org.unix4j.io.Output;
import org.unix4j.line.Line;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

/**
 * Interface defining command execution and output redirection methods.
 */
public interface To {
	/**
	 * Executes the composite command and writes the result to the standard
	 * output.
	 */
	void toStdOut();

	/**
	 * Executes the composite command and returns the result as string. Line
	 * ending characters are inserted between lines if the result contains
	 * multiple lines. Note that the last line is NOT terminated with a line
	 * ending.
	 * <p>
	 * To return a representation of the command with its arguments without
	 * executing the command, {@link Command#toString() toString()} can be used
	 * instead.
	 * 
	 * @return the result as a string, possibly a multiline string with newline
	 *         characters between the lines but not after the last line
	 */
	String toStringResult();

	/**
	 * Executes the composite command and returns the result as a list
	 * containing the output lines.
	 * 
	 * @return the result as a list of line strings
	 */
	List<Line> toLineList();

	/**
	 * Executes the composite command and writes the result to the given file.
	 * 
	 * @param file
	 *            the target output file
	 */
	void toFile(String file);

	/**
	 * Executes the composite command and does not write the result anywhere.
	 * 
	 */
	void toDevNull();

	/**
	 * Executes the composite command and writes the result to the given file.
	 * 
	 * @param file
	 *            the target output file
	 */
	void toFile(File file);

	/**
	 * Executes the composite command and writes the result to the given stream.
	 * 
	 * @param stream
	 *            the target output stream
	 */
	void toOutputStream(OutputStream stream);

	/**
	 * Executes the composite command and writes the result using the given
	 * writer.
	 * 
	 * @param writer
	 *            the writer used to write the output
	 */
	void toWriter(Writer writer);

	/**
	 * Executes the composite command and writes the result to the given output.
	 */
	void toOutput(Output output);

}
