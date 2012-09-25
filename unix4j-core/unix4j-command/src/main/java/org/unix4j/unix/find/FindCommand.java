package org.unix4j.unix.find;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Find;

/**
 * Implementation of the {@link Find find} command.
 */
class FindCommand extends AbstractCommand<FindArguments> {
	public FindCommand(FindArguments arguments) {
		super(Find.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		throw new RuntimeException("not implemented");
	}

}
