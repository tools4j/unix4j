package org.unix4j.unix.head;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

final class HeadLinesProcessor extends AbstractHeadProcessor {
	HeadLinesProcessor(HeadCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		if (counter.getCount() < count) {
			final boolean more = getOutput().processLine(line);
			return counter.increment() < count && more;
		} else {
			return false;
		}
	}
}