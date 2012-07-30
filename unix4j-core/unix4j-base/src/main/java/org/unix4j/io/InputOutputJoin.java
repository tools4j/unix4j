package org.unix4j.io;

import java.util.LinkedList;
import java.util.List;

import org.unix4j.command.JoinedCommand;
import org.unix4j.line.Line;

/**
 * Output object used for joined commands. Uses a linked list as common line
 * {@link #getBuffer() buffer} for an {@link #getInput() input} and
 * {@link #getOutput() output} object.
 * 
 * @see JoinedCommand
 */
public class InputOutputJoin {

	private final LinkedList<Line> buffer = new LinkedList<Line>();
	private final BufferedInput input = new BufferedInput(buffer);
	private final BufferedOutput output = new BufferedOutput(buffer);

	public InputOutputJoin() {
		super();
	}

	public List<Line> getBuffer() {
		return buffer;
	}

	public BufferedInput getInput() {
		return input;
	}

	public BufferedOutput getOutput() {
		return output;
	}
	
	public boolean isEmpty() {
		return buffer.isEmpty();
	}

}
