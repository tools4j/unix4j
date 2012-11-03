package org.unix4j.unix.xargs;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Find;
import org.unix4j.unix.Xargs;

/**
 * Implementation of the {@link Xargs xargs} command.
 */
class XargsCommand extends AbstractCommand<XargsArguments> {
	public XargsCommand(XargsArguments arguments) {
		super(Find.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		throw new RuntimeException("not implemented");
	}

}
