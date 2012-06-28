package org.unix4j.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BufferedOutput implements Output {
	private static final String NEW_LINE = System.getProperty("line.separator");
	private final List<String> lines = new ArrayList<String>();
	public BufferedOutput() {
		super();
	}
	@Override
	public void writeLine(String line) {
		lines.add(line);
	}
	@Override
	public void finish() {
		//nothing to do
	}
	@Override
	public String toString() {
		return lines.toString();
	}
	public String toMultiLineString() {
		final StringBuilder sb = new StringBuilder();
		for (final String line : lines) {
			sb.append(line).append(NEW_LINE);
		}
		return sb.toString();
	}
	public void writeTo(Output output) {
		for (final String line : lines) {
			output.writeLine(line);
		}
		output.finish();
	}
	public BufferedInput asInput() {
		return new BufferedInput(new LinkedList<String>(lines));
	}
	public BufferedOutput clear() {
		lines.clear();
		return this;
	}
}
