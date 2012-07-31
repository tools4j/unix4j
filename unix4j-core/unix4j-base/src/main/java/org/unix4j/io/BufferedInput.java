package org.unix4j.io;

import java.util.LinkedList;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;

public class BufferedInput extends AbstractInput {
	private final LinkedList<Line> buffer;
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
				return new SimpleLine(line);//add line ending if not final line
			}
			return line;
		}
		return null;
	}
	@Override
	public String toString() {
		return buffer.toString();
	}
}
