package org.unix4j.io;

/**
 * Output device writing to the {@link System#out standard output} stream.
 */
public class StdOutput extends StreamOutput {
	/**
	 * The singleton instance.
	 */
	public static final StdOutput INSTANCE = new StdOutput();

	/**
	 * Default constructor, application code should use the singleton
	 * {@link #INSTANCE}.
	 */
	public StdOutput() {
		super(System.out);
	}

	@Override
	public String toString() {
		return "/dev/stdout";
	}
}
