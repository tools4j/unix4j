package org.unix4j.io;

import java.util.LinkedList;


public class BufferedInput implements Input {
	private final LinkedList<String> buffer;
	public BufferedInput(LinkedList<String> buffer) {
		this.buffer = buffer;
	}
	@Override
	public boolean hasMoreLines() {
		return !buffer.isEmpty();
	}
	@Override
	public String readLine() {
		if (!buffer.isEmpty()) {
			return buffer.remove(0);
		}
		return null;
	}
	@Override
	public String toString() {
		return buffer.toString();
	}
}
