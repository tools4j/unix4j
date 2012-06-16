package org.unix4j.command;

import java.util.LinkedList;

import org.unix4j.io.BufferedInput;
import org.unix4j.io.Output;

/**
 * 
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

	@Override
	public void writeLine(String line) {
		buffer.add(line);
		if (joinedCommand.getType().isLineByLine()) {
			joinedCommand.execute(input, output);
		}
	}

	@Override
	public void finish() {
		if (!joinedCommand.getType().isLineByLine()) {
			joinedCommand.execute(input, output);
		}
		output.finish();
	}
}
