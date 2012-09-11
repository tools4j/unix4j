package org.unix4j.unix.cat;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Echo;

/**
 * Implementation of the {@code Cat cat} command.
 */
class CatCommand extends AbstractCommand<CatArguments> {
	public CatCommand(CatArguments arguments) {
		super(Echo.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		return new CatProcessor(this, context, output);
	}
}
