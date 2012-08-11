package org.unix4j.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.unix4j.line.Line;
import org.unix4j.util.LineUtil;

/**
 * Output device storing all written lines in memory in a {@link List}.
 */
public class BufferedOutput implements Output {

	private final List<Line> buffer;

	/**
	 * Constructor using an {@link ArrayList} as backing buffer for the lines.
	 */
	public BufferedOutput() {
		this(new ArrayList<Line>());
	}

	/**
	 * Constructor using the specified list as line buffer. The buffer is NOT
	 * cloned, that is, changes to the buffer affect this {@code BufferedOutput}
	 * and vice versa.
	 * 
	 * @param buffer
	 *            the line buffer used "as is" without cloning
	 */
	public BufferedOutput(List<Line> buffer) {
		this.buffer = buffer;
	}

	@Override
	public boolean processLine(Line line) {
		buffer.add(line);
		return true;
	}

	@Override
	public void finish() {
		// nothing to do
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
	 * Writes the buffered output lines to the specified {@code output} device.
	 * This buffered output devices does not change, that is, all lines will
	 * still be contained in the buffer after calling this method.
	 * 
	 * @param output
	 *            the output device to write to
	 */
	public void writeTo(Output output) {
		boolean more = true;
		for (int i = 0; i < buffer.size() && more; i++) {
			more = output.processLine(buffer.get(i));
		}
		output.finish();
	}

	/**
	 * Returns a {@link BufferedInput} with all lines contained in this
	 * {@code BufferedOutput}. The lists used as buffer in this
	 * {@link BufferedOutput} and the returned {@link BufferedInput} are NOT
	 * shared, meaning that subsequent modifications of this
	 * {@code BufferedOutput} are not reflected in the returned
	 * {@code BufferedInput}.
	 * 
	 * @return a {@code BufferdInput} object reflecting the current snapshot of
	 *         lines in this {@code BufferedOutput}
	 */
	public BufferedInput asInput() {
		return new BufferedInput(new LinkedList<Line>(buffer));
	}

	/**
	 * Returns a new list with the lines currently stored by this
	 * {@code BufferdOutput} object.
	 * 
	 * @return a new list with the lines of this buffer
	 */
	public List<Line> asList() {
		return new ArrayList<Line>(buffer);
	}

	/**
	 * Returns the number of lines currently stored by this
	 * {@code BufferedOutput} object.
	 * 
	 * @return the number of lines in the buffer
	 */
	public int size() {
		return buffer.size();
	}

	/**
	 * Clears all lines in this buffer.
	 * 
	 * @return this buffer for chained calls
	 */
	public BufferedOutput clear() {
		buffer.clear();
		return this;
	}
}
