package org.unix4j.unix.tail;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

class TailLinesFromStartProcessor extends AbstractTailProcessor {

	private final Counter counter = new Counter();

	public TailLinesFromStartProcessor(TailCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		if (counter.increment() >= count) {
			return getOutput().processLine(line);
		} else {
			return true;//we want more lines
		}
	}

	@Override
	public void finish() {
		counter.reset();
		getOutput().finish();
	}

}
