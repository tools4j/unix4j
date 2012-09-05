package org.unix4j.unix.uniq;

import org.unix4j.command.AbstractLineProcessor;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;

abstract class UniqLineProcessor extends AbstractLineProcessor<UniqArguments> {

	public UniqLineProcessor(UniqCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public final boolean processLine(Line line) {
		return processLine(line, getOutput());
	}

	abstract protected boolean processLine(Line line, LineProcessor output);

}