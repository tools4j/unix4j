package org.unix4j.unix.grep;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

/**
 * Writes the matching line count to the output. The matching operation is delegated
 * to the {@link LineMatcher} passed to the constructor. 
 */
final class CountMatchingLinesProcessor extends AbstractGrepProcessor {

	private final Counter counter = new Counter();

	public CountMatchingLinesProcessor(GrepCommand command, ExecutionContext context, LineProcessor output, LineMatcher matcher) {
		super(command, context, output, matcher);
	}

	@Override
	protected boolean processLine(Line line, boolean isMatch) {
		if (isMatch) {
			counter.increment();
		}
		return true;//even if line is not a match, we still want the next line
	}

	@Override
	public void finish() {
		try {
			getOutput().processLine(new SimpleLine(String.valueOf(counter.getCount())));
			super.finish();
		} finally {
			counter.reset();
		}
	}
}