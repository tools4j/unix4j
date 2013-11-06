package org.unix4j.unix.xargs;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.Command;
import org.unix4j.context.ExecutionContext;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Echo;
import org.unix4j.unix.Find;
import org.unix4j.unix.Xargs;
import org.unix4j.variable.Arg;

/**
 * Implementation of the {@link Xargs xargs} command.
 */
class XargsCommand extends AbstractCommand<XargsArguments> {
	
	private final Command<?> invokedCommand;
	
	public XargsCommand(XargsArguments arguments) {
		this(arguments, null);
	}
	protected XargsCommand(XargsArguments arguments, Command<?> invokedCommand) {
		super(Find.NAME, arguments);
		this.invokedCommand = invokedCommand;
	}
	
	protected Command<?> getInvokedCommand() {
		return invokedCommand == null ? Echo.Factory.echo(Arg.$all) : invokedCommand;
	}
	
	@Override
	public Command<?> join(Command<?> next) {
		return invokedCommand == null ? new XargsCommand(getArguments(null), next) : super.join(next);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		return new XargsLineProcessor(this, context, output);
	}

}
