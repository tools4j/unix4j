package org.unix4j.unix.tail;

import org.unix4j.context.ExecutionContext;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;

/**
 * Base class for the line processors used by the {@link TailCommand}.
 */
abstract class AbstractTailProcessor extends AbstractLineProcessor<TailArguments> {
	protected final long count;
	public AbstractTailProcessor(TailCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		final TailArguments args = getArguments();
		this.count = args.isCountSet() ? args.getCount() : 10;
	}
}