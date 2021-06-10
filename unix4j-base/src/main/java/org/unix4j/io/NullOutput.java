package org.unix4j.io;

import org.unix4j.line.Line;

/**
 * Output device writing to {@code /dev/null}, which means that all lines
 * written to this device are ignored.
 * <p>
 * Two instances are available, the {@link #ABORT} instance which returns false
 * when {@link #processLine(Line)} which usually causes the program execution to
 * abort. The {@link #DEFAULT} instance does not abort program execution to make
 * sure that no side effects are suppressed.
 */
public class NullOutput implements Output {

	/**
	 * Default instance returning true when {@link #processLine(Line)} is
	 * called. This ensures that a command does not abort early and it is
	 * guaranteed that no (possibly important) side effects are missed.
	 */
	public static final NullOutput DEFAULT = new NullOutput(false);
	/**
	 * Aborting instance returning false when {@link #processLine(Line)} is
	 * called. Note that most commands will abort their execution early with
	 * this output device and some (possibly important) side effects could be
	 * missed.
	 */
	public static final NullOutput ABORT = new NullOutput(true);

	private final boolean processLines;

	/**
	 * NOTE: application code should normally use the {@link #DEFAULT} or
	 * {@link #ABORT} constants instead of this constructor.
	 * <p>
	 * The {@code abort} flag passed to this constructor indicates whether the
	 * process can be aborted early or not; {@code abort=true} means that the
	 * method {@link #processLine(Line)} returns false indicating that this
	 * device does not expect any more input lines. Most commands will abort
	 * their execution early in this case, which means that (possibly important)
	 * side effects are missed.
	 * 
	 * @param abort
	 *            a flag indicating whether command execution can be aborted
	 *            early
	 */
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

	@Override
	public String toString() {
		return processLines ? "/dev/null" : "/dev/null(abort)";
	}
}
