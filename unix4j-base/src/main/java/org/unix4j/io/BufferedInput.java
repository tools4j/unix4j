package org.unix4j.io;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.util.LineUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Input device based on a {@link LinkedList} line buffer.
 */
public class BufferedInput extends AbstractInput {
	private final LinkedList<Line> buffer;

	/**
	 * Constructor with linked list used as source of the input lines. The
	 * specified {@code buffer} is NOT cloned, meaning that changes to the
	 * buffer will also be reflected in this input device and vice versa.
	 * 
	 * @param buffer
	 *            the buffer to use as basis for this input device
	 */
	public BufferedInput(LinkedList<Line> buffer) {
		this.buffer = buffer;
	}

	@Override
	public boolean hasMoreLines() {
		return !buffer.isEmpty();
	}

	@Override
	public Line readLine() {
		if (!buffer.isEmpty()) {
			final Line line = buffer.remove(0);
			if (!buffer.isEmpty() && line.getLineEndingLength() == 0) {
				return new SimpleLine(line);// add line ending if not final line
			}
			return line;
		}
		return null;
	}

	/**
	 * Returns a list-like representation of the lines contained in this buffer.
	 * Some users might also consider {@link #toMultiLineString()} instead.
	 * 
	 * @return a list-like string representation of the buffered lines
	 */
	@Override
	public String toString() {
		return buffer.toString();
	}

	/**
	 * Returns a multi-line representation of the lines in this buffer. The last
	 * line is never terminated, all other lines are terminated with guarantee
	 * even if the line itself has an empty line ending string.
	 * 
	 * @return a multi-line string of the buffered lines, without line
	 *         termination for the last line
	 * @see LineUtil#toMultiLineString(List)
	 */
	public String toMultiLineString() {
		return LineUtil.toMultiLineString(buffer);
	}

	/**
	 * Performs a no-op as there are no underlying resources
	 */
	@Override
	public void close() {
		//nothing to do
	}
}
