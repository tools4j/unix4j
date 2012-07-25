package org.unix4j.io;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BufferedOutput implements Output {
	private static final String NEW_LINE = System.getProperty("line.separator");
	private final List<String> buffer;
	public BufferedOutput() {
		this(new ArrayList<String>());
	}
	public BufferedOutput(List<String> buffer) {
		this.buffer = buffer;
	}
	@Override
	public void writeLine(String line) {
		buffer.add(line);
	}
	@Override
	public void finish() {
		//nothing to do
	}
	@Override
	public String toString() {
		return buffer.toString();
	}
	public String toMultiLineString() {
		return toMultiLineString(true);
	}
	public String toMultiLineString(boolean appendTrailingLineEnding) {
		final StringBuilder sb = new StringBuilder();
		for (final String line : buffer) {
			if(sb.length() > 0){
				sb.append(NEW_LINE);
			}
			sb.append(line);
		}
		if(appendTrailingLineEnding){
			sb.append(NEW_LINE);
		}
		return sb.toString();
	}
	public void writeTo(Output output) {
		for (final String line : buffer) {
			output.writeLine(line);
		}
		output.finish();
	}
	public BufferedInput asInput() {
		return new BufferedInput(new LinkedList<String>(buffer));
	}
	public BufferedOutput clear() {
		buffer.clear();
		return this;
	}
}
