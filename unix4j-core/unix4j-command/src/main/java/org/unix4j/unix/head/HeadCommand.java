package org.unix4j.unix.head;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.LineProcessor;
import org.unix4j.unix.Head;

/**
 * Head command implementation.
 */
public class HeadCommand extends AbstractCommand<HeadArguments> {
	public HeadCommand(HeadArguments arguments) {
		super(Head.NAME, arguments);
		if (arguments.isCountSet() && arguments.getCount() < 0) {
			throw new IllegalArgumentException("count cannot be negative: " + arguments);
		}
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		if (getArguments().isChars()) {
			//count chars
			return new HeadCharsProcessor(this, context, output);
		} else {
			//count lines
			return new HeadLinesProcessor(this, context, output);
		}
	}
}