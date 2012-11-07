package org.unix4j.unix.head;

import org.unix4j.command.ExecutionContext;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

/**
 * Base class for the line processors used by the {@link HeadCommand}.
 */
abstract class AbstractHeadProcessor extends AbstractLineProcessor<HeadArguments> {
	protected final long count;
	protected final Counter counter = new Counter();

	public AbstractHeadProcessor(HeadCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		final HeadArguments args = getArguments();
		this.count = args.isCountSet() ? args.getCount() : 10;
	}

	@Override
	public void finish() {
		counter.reset();
		getOutput().finish();
	}
}