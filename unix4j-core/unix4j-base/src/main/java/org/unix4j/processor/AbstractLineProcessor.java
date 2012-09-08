package org.unix4j.processor;

import org.unix4j.command.Arguments;
import org.unix4j.command.Command;
import org.unix4j.command.ExecutionContext;

/**
 * Abstract base implementation for {@link LineProcessor} returned by the
 * execute method of commands constructed with a reference to the command plus
 * context and output passed to
 * {@link Command#execute(ExecutionContext, LineProcessor)}.
 */
abstract public class AbstractLineProcessor<A extends Arguments<A>> implements LineProcessor {
	private final Command<A> command;
	private final ExecutionContext context;
	private final LineProcessor output;

	/**
	 * Constructor with command creating this processor, execution context and
	 * output to write to.
	 * 
	 * @param command
	 *            the command whose execute method usually returns this line
	 *            processor
	 * @param context
	 *            the execution context passed to the command's execute method
	 * @param output
	 *            the output object to write to when executing the command
	 *            through this processor
	 */
	public AbstractLineProcessor(Command<A> command, ExecutionContext context, LineProcessor output) {
		this.command = command;
		this.context = context;
		this.output = output;
	}

	/**
	 * Returns the command that was passed to the constructor of this line
	 * processor, the command whose execute method usually returns this line
	 * processor.
	 * 
	 * @return the command whose execute method usually returns this line
	 *         processor
	 */
	protected Command<A> getCommand() {
		return command;
	}

	/**
	 * Returns the command arguments, a shortcut for
	 * {@code getCommand().getArguments()}.
	 * 
	 * @return the command arguments
	 */
	protected A getArguments() {
		return getCommand().getArguments();
	}

	/**
	 * Returns the execution context that was passed to the constructor
	 * 
	 * @return the execution context
	 */
	protected ExecutionContext getContext() {
		return context;
	}

	/**
	 * Returns the output that was passed to the constructor of this line
	 * processor, the object to write to when executing the command through the
	 * this processor
	 * 
	 * @return the output to write to when using this processor
	 */
	protected LineProcessor getOutput() {
		return output;
	}
}