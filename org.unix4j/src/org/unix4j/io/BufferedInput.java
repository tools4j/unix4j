package org.unix4j.io;

import java.util.LinkedList;


public class BufferedInput implements Input {
	private final LinkedList<String> lines;
	public BufferedInput(LinkedList<String> lines) {
		this.lines = lines;
	}
	@Override
	public boolean hasMoreLines() {
		return !lines.isEmpty();
	}
	@Override
	public String readLine() {
		if (!lines.isEmpty()) {
			return lines.remove(0);
		}
		return null;
	}

}
