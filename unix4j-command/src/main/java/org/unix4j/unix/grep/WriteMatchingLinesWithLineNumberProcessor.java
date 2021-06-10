package org.unix4j.unix.grep;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

/**
 * Writes all matching lines to the output. The matching operation is delegated
 * to the {@link LineMatcher} passed to the constructor. 
 */
final class WriteMatchingLinesWithLineNumberProcessor extends AbstractGrepProcessor {

	private final Counter lineCounter = new Counter();

	public WriteMatchingLinesWithLineNumberProcessor(GrepCommand command, ExecutionContext context, LineProcessor output, LineMatcher matcher) {
		super(command, context, output, matcher);
	}

	@Override
	protected boolean processLine(Line line, boolean isMatch) {
		lineCounter.increment();
		if (isMatch) {
			return getOutput().processLine(new SimpleLine(
					lineCounter.getCount() + ":" + line.getContent(), line.getLineEnding()
			));
		}
		return true;//this line is not a match, but we still want the next line
	}
}