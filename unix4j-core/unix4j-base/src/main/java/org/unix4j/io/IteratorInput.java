package org.unix4j.io;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;

import java.util.Iterator;

/**
 * Input device based on a {@link Iterator} returning lines.
 */
public class IteratorInput extends AbstractInput {
	private final Iterator<? extends Line> lines;

	/**
	 * Constructor with linked list used as source of the input lines. The
	 * specified {@code buffer} is NOT cloned, meaning that changes to the
	 * buffer will also be reflected in this input device and vice versa.
	 *
	 * @param lines
	 *            the buffer to use as basis for this input device
	 */
	public IteratorInput(Iterator<? extends Line> lines) {
		this.lines = lines;
	}

	@Override
	public boolean hasMoreLines() {
		return lines.hasNext();
	}

	@Override
	public Line readLine() {
		if (lines.hasNext()) {
			final Line line = lines.next();
			if (lines.hasNext() && line.getLineEndingLength() == 0) {
				return new SimpleLine(line);// add line ending if not final line
			}
			return line;
		}
		return null;
	}

	/**
	 * Performs a no-op as there are no underlying resources
	 */
	@Override
	public void close() {
		//nothing to do
	}
}
