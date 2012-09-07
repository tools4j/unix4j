package org.unix4j.unix.tail;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.LineProcessor;
import org.unix4j.unix.Tail;

/**
 * Tail command implementation.
 */
public class TailCommand extends AbstractCommand<TailArguments> {
	public TailCommand(TailArguments arguments) {
		super(Tail.NAME, arguments);
		if (arguments.isCountSet() && arguments.getCount() < 0) {
			throw new IllegalArgumentException("count cannot be negative: " + arguments);
		}
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final TailArguments args = getArguments();
		if (args.isChars()) {
			if (args.isCountFromStart()) {
				return new TailCharsFromStartProcessor(this, context, output);
			} else {
				return new TailCharsFromEndProcessor(this, context, output);
			}
		} else {
			if (args.isCountFromStart()) {
				return new TailLinesFromStartProcessor(this, context, output);
			} else {
				return new TailLinesFromEndProcessor(this, context, output);
			}
		}
	}
}