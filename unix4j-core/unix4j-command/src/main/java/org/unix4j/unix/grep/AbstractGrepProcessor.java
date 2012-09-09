package org.unix4j.unix.grep;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;

abstract class AbstractGrepProcessor extends AbstractLineProcessor<GrepArguments> {
	
	private final LineMatcher matcher;
	
	public AbstractGrepProcessor(GrepCommand command, ExecutionContext context, LineProcessor output, LineMatcher matcher) {
		super(command, context, output);
		this.matcher = matcher;
	}

	@Override
	public boolean processLine(Line line) {
		final boolean isMatch = matcher.matches(line);
		return processLine(line, isMatch);
	}

	@Override
	public void finish() {
		getOutput().finish();
	}

	abstract protected boolean processLine(Line line, boolean isMatch);
}