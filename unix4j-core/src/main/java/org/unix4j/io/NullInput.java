package org.unix4j.io;


public class NullInput implements Input {
	
	public static final NullInput INSTANCE = new NullInput();
	
	@Override
	public boolean hasMoreLines() {
		return false;
	}

	@Override
	public String readLine() {
		return null;
	}
	
}
