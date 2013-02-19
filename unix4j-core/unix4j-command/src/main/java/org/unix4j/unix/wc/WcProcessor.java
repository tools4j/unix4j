package org.unix4j.unix.wc;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;

/**
 * Standard input processor for line, word and char count.
 */
final class WcProcessor extends AbstractLineProcessor<WcArguments> {
	private final Counters counters;

	public WcProcessor(WcCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		counters = new Counters(getArguments());
	}

	@Override
	public boolean processLine(Line line) {
		counters.update(line);
		return true;//we want to count all lines
	}

	@Override
	public void finish() {
		final LineProcessor output = getOutput();
		counters.writeCountsLine(output);
		output.finish();
	}
}