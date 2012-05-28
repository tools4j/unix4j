package org.unix4j.io;

import org.unix4j.Input;

public class NullInput implements Input {
	
	@Override
	public boolean hasMoreLines() {
		return false;
	}

	@Override
	public String readLine() {
		return null;
	}
	
}
