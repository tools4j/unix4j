package org.unix4j.unix.head;

import org.unix4j.command.AbstractLineProcessor;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.LineProcessor;
import org.unix4j.util.Counter;

/**
 * Base class for the line processors used by the {@link HeadCommand}.
 */
abstract class AbstractHeadProcessor extends AbstractLineProcessor<HeadArguments> {
	protected final long count;
	protected final Counter counter = new Counter();

	public AbstractHeadProcessor(HeadCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		this.count = getArguments().isCountSet() ? getArguments().getCount() : 10;
	}

	@Override
	public void finish() {
		getOutput().finish();
	}
}