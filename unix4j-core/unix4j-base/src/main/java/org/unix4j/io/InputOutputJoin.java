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

	/**
	 * Default constructor.
	 */
	public InputOutputJoin() {
		super();
	}

	/**
	 * Returns the underlying line buffer.
	 * 
	 * @return the list storing the lines
	 */
	public List<Line> getBuffer() {
		return buffer;
	}

	/**
	 * Returns the input device based on the {@link #getBuffer() buffer}.
	 * 
	 * @return the buffered input device
	 */
	public BufferedInput getInput() {
		return input;
	}

	/**
	 * Returns the output device based on the {@link #getBuffer() buffer}.
	 * 
	 * @return the buffered output device
	 */
	public BufferedOutput getOutput() {
		return output;
	}

	/**
	 * Returns true if the {@link #getBuffer() buffer} is empty.
	 * 
	 * @return true if the buffer is empty
	 */
	public boolean isEmpty() {
		return buffer.isEmpty();
	}

}
