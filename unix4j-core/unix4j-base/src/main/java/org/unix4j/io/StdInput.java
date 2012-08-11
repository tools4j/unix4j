package org.unix4j.io;

/**
 * Input device reading from the {@link System#in standard input} stream.
 */
public class StdInput extends StreamInput {
	
	/**
	 * The singleton instance.
	 */
	public static final StdInput INSTANCE = new StdInput();
	
	/**
	 * Default constructor, application code should use the singleton {@link #INSTANCE}.
	 */
	public StdInput() {
		super(System.in);
	}
}
