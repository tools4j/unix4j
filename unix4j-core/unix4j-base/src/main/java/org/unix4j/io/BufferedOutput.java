package org.unix4j.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.unix4j.line.Line;
import org.unix4j.util.StringUtil;

public class BufferedOutput implements Output {
	private final List<Line> buffer;

	public BufferedOutput() {
		this(new ArrayList<Line>());
	}

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

	@Override
	public String toString() {
		return buffer.toString();
	}

	public String toMultiLineString() {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < buffer.size(); i++) {
			final Line line = buffer.get(i);
			sb.append(line.getContent());
			if (i + 1 < buffer.size()) {
				sb.append(line.getLineEndingLength() == 0 ? StringUtil.LINE_ENDING : line.getLineEnding());
			}
		}
		return sb.toString();
	}

	public void writeTo(Output output) {
		for (final Line line : buffer) {
			output.processLine(line);
		}
		output.finish();
	}

	public BufferedInput asInput() {
		return new BufferedInput(new LinkedList<Line>(buffer));
	}

	public BufferedOutput clear() {
		buffer.clear();
		return this;
	}
}
