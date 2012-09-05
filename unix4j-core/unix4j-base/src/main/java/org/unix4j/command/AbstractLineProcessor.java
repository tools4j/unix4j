package org.unix4j.command;

import org.unix4j.line.LineProcessor;

/**
 * Abstract base implementation for {@link LineProcessor} returned by the
 * execute method of commands constructed with a reference to the command plus
 * context and output passed to
 * {@link Command#execute(ExecutionContext, LineProcessor)} .
 */
abstract public class AbstractLineProcessor<A extends Arguments<A>> implements LineProcessor {
	private final Command<A> command;
	private final ExecutionContext context;
	private final LineProcessor output;

	public AbstractLineProcessor(Command<A> command, ExecutionContext context, LineProcessor output) {
		this.command = command;
		this.context = context;
		this.output = output;
	}

	protected Command<A> getCommand() {
		return command;
	}
	protected A getArguments() {
		return getCommand().getArguments();
	}
	protected ExecutionContext getContext() {
		return context;
	}
	protected LineProcessor getOutput() {
		return output;
	}
}