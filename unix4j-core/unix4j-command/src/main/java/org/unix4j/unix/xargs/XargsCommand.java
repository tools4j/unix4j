package org.unix4j.unix.xargs;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.Command;
import org.unix4j.context.ExecutionContext;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Echo;
import org.unix4j.unix.Find;
import org.unix4j.unix.Xargs;

/**
 * Implementation of the {@link Xargs xargs} command.
 */
class XargsCommand extends AbstractCommand<XargsArguments> {
	
	private final Command<?> invokedCommand;
	
	public XargsCommand(XargsArguments arguments) {
		this(arguments, Echo.Factory.echo(Xarg.$args));
	}
	protected XargsCommand(XargsArguments arguments, Command<?> invokedCommand) {
		super(Find.NAME, arguments);
		this.invokedCommand = invokedCommand;
	}
	
	protected Command<?> getInvokedCommand() {
		return invokedCommand;
	}
	
	@Override
	public Command<?> join(Command<?> next) {
		return new XargsCommand(getArguments(null), next);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		return new XargsLineProcessor(this, context, output);
	}

}
