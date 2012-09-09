package org.unix4j.unix.wc;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

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
		if (counters.isLastLineEmpty()) {
			final Counter lineCounter = counters.getCounter(CounterType.Lines);
			if (lineCounter != null && lineCounter.getCount() == 1) {
				lineCounter.reset();
			}
		}
		counters.writeCountsLine(output);
		output.finish();
	}
	
}