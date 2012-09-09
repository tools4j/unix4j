package org.unix4j.unix.grep;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;

abstract class AbstractGrepProcessor extends AbstractLineProcessor<GrepArguments> {
	public AbstractGrepProcessor(GrepCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		final LineProcessor output = getOutput();
		final boolean isInvert = getArguments().isInvert();
		final boolean matches = matches(line);
		if (isInvert ^ matches) {
			return output.processLine(line);
		}
		return true;
	}

	@Override
	public void finish() {
		getOutput().finish();
	}

	abstract protected boolean matches(Line line);
}