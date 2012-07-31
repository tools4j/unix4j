package org.unix4j.io;

import org.unix4j.line.Line;

/**
 * Output object writing to {@code /dev/null}, which means that all lines
 * written to this output are ignored.
 * <p>
 * Two instances are available, the {@link #ABORT} instance which returns false
 * when {@link #processLine(Line)} which usually causes the program execution to
 * abort. The {@link #DEFAULT} instance does not abort program execution to make
 * sure that no side effects are suppressed.
 */
public class NullOutput implements Output {

	/**
	 * An instance which returns true when {@link #processLine(Line)} is called
	 * making sure that all lines are passed to the output object. With this, no
	 * desired side effects are suppressed.
	 */
	public static final NullOutput DEFAULT = new NullOutput(false);
	/**
	 * An instance which returns false when {@link #processLine(Line)} is called
	 * which usually causes the program execution to abort and terminate
	 * immediately. Note that this is more efficient but possibly suppresses
	 * desired side effects.
	 */
	public static final NullOutput ABORT = new NullOutput(true);

	private final boolean processLines;

	public NullOutput(boolean abort) {
		this.processLines = !abort;
	}

	@Override
	public boolean processLine(Line line) {
		return processLines;
	}

	@Override
	public void finish() {
		// ignore
	}

}
