package org.unix4j.io;

import org.unix4j.line.Line;

/**
 * Input device representing an empty set with no lines.
 */
public class NullInput extends AbstractInput {
	
	public static final NullInput INSTANCE = new NullInput();
	
	@Override
	public boolean hasMoreLines() {
		return false;
	}

	@Override
	public Line readLine() {
		return null;
	}

	/**
	 * Performs a no-op as there are no underlying resources
	 */
	@Override
	public void close() {
		//nothing to do
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
