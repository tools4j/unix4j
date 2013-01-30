package org.unix4j.unix.head;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;

final class HeadCharsProcessor extends AbstractHeadProcessor {
	HeadCharsProcessor(HeadCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		final long before = counter.getCount();
		if (before < count) {
			final long after = counter.increment(line.length());
			final boolean more;
			if (after < count) {
				 more = getOutput().processLine(line);
			} else {
				final int len = (int)(count-before);
				final Line cutLine = SimpleLine.subLine(line, 0, len, false);
				more = getOutput().processLine(cutLine);
			}
			return after < count && more;
		} else {
			return false;
		}
	}
}