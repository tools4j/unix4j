package org.unix4j.builder;

import org.unix4j.command.Command;
import org.unix4j.command.ExitValueException;
import org.unix4j.io.Output;
import org.unix4j.line.Line;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.stream.Stream;

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
	 * @return the result as a list of lines
	 */
	List<Line> toLineList();

	/**
	 * Executes the composite command and returns the result as a list
	 * containing the output lines without line ending.
	 * 
	 * @return the result as a list of line strings
	 */
	List<String> toStringList();

	/**
	 * Executes the composite command and returns the result as a stream
	 * containing the output lines.
	 *
	 * @return the result as a stream of lines
	 */
	Stream<Line> toLineStream();

	/**
	 * Executes the composite command and returns the result as a stream
	 * containing the output lines without line endings.
	 *
	 * @return the result as a stream of line strings
	 */
	Stream<String> toStringStream();

	/**
	 * Executes the composite command and writes the result to the given file.
	 * 
	 * @param file
	 *            the target output file
	 */
	void toFile(String file);

	/**
	 * Executes the composite command and does not write the result anywhere.
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
	
	/**
	 * Executes the composite command returns its exit value, 0 if the command
	 * completes successfully and another command specific error value different
	 * from zero if the command throws an {@link ExitValueException}.
	 * 
	 * @return the exit value returned by the command, 0 for success
	 */
	int toExitValue();

}
