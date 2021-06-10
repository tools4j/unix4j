package org.unix4j.unix.cat;

import org.unix4j.command.Command;
import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;

class CatProcessor extends AbstractLineProcessor<CatArguments> {

	public CatProcessor(Command<CatArguments> command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		return getOutput().processLine(line);
	}

	@Override
	public void finish() {
		getOutput().finish();
	}
}