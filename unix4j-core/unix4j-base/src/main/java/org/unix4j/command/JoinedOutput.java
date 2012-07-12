package org.unix4j.command;

import java.util.LinkedList;

import org.unix4j.command.Command.Type;
import org.unix4j.io.BufferedInput;
import org.unix4j.io.Output;

/**
 * Output object used for joined commands. The first command in a join writes to
 * this output. The data is buffered in and fed into the joined second command
 * as input. Depending on the {@link Type type} of the second command, writing
 * to the output triggers the second command for every written line or once at
 * the end when {@link #finish()} is called.
 * 
 * @see JoinedCommand
 */
public class JoinedOutput implements Output {

	private final Command<?> joinedCommand;
	private final LinkedList<String> buffer = new LinkedList<String>();
	private final BufferedInput input = new BufferedInput(buffer);
	private final Output output;

	/**
	 * Constructor with joined command and output for second command.
	 * 
	 * @param joinedCommand
	 *            the joined command
	 * @param output
	 *            the output for the second command
	 */
	public JoinedOutput(Command<?> joinedCommand, Output output) {
		this.joinedCommand = joinedCommand;
		this.output = output;
	}

	// --- output ---\\

	/**
	 * Writes a line to the output. If the second command in the joine is a
	 * {@link Type#isLineByLine() line-by-line} command, it is invoked
	 * immediately after writing the line. Otherwise, the line stored in the
	 * internal line buffer of this joined output.
	 * 
	 * @param line
	 *            the line to write to the output
	 */
	@Override
	public void writeLine(String line) {
		buffer.add(line);
		if (joinedCommand.getType().isLineByLine()) {
			joinedCommand.execute(input, output);
		}
	}

	/**
	 * Indicates that all output has been written. If the second command in the
	 * joine is <b>not</b> {@link Type#isLineByLine() line-by-line} command,
	 * execution of the second command is triggered now. The lines that have
	 * been added to the internal line buffer with every
	 * {@link #writeLine(String) writeLine(..)} call constitute the input for
	 * the second command.
	 */
	@Override
	public void finish() {
		if (!joinedCommand.getType().isLineByLine()) {
			joinedCommand.execute(input, output);
		}
		output.finish();
	}
}
