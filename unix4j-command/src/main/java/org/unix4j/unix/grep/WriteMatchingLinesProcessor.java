package org.unix4j.unix.grep;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

/**
 * Writes all matching lines to the output. The matching operation is delegated
 * to the {@link LineMatcher} passed to the constructor. 
 */
final class WriteMatchingLinesProcessor extends AbstractGrepProcessor {
	
	public WriteMatchingLinesProcessor(GrepCommand command, ExecutionContext context, LineProcessor output, LineMatcher matcher) {
		super(command, context, output, matcher);
	}

	@Override
	protected boolean processLine(Line line, boolean isMatch) {
		if (isMatch) {
			return getOutput().processLine(line);
		}
		return true;//this line is not a match, but we still want the next line
	}
}