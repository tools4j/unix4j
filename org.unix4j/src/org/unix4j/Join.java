package org.unix4j;

import java.util.LinkedList;
import java.util.List;

/**
 * Join object used when {@link JoinedCommand#execute() executing} a
 * {@link JoinedCommand}.
 * <p>
 * A join clones the joined commands upon construction and redirects the input
 * of the first command to the output of the second command.
 */
public class Join implements Input, Output {

	private final Command<?> first;
	private final Command<?> second;
	private final List<String> lines = new LinkedList<String>();

	/**
	 * Constructor with joined command
	 * 
	 * @param joinedCommand the joined command
	 */
	public Join(JoinedCommand<?> joinedCommand) {
		first = joinedCommand.getFirst().clone();
		second = joinedCommand.getSecond().clone();
		first.writeTo(this);
		second.readFrom(this);
	}

	/**
	 * Invokes execution on the first command.
	 */
	public void execute() {
		first.execute();
	}

	// --- input ---\\

	@Override
	public boolean hasMoreLines() {
		return !lines.isEmpty();
	}

	@Override
	public String readLine() {
		return lines.remove(0);
	}

	// --- output ---\\

	@Override
	public void writeLine(String line) {
		lines.add(line);
		if (second.isBatchable()) {
			second.execute();
		}
	}

	@Override
	public void finish() {
		if (!second.isBatchable()) {
			second.execute();
		}
	}
}
