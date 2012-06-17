package org.unix4j.command;

import org.unix4j.io.Input;
import org.unix4j.io.Output;

abstract public class AbstractCommand<A extends Arguments<A>> implements Command<A> {
	private final String name;
	private final Type type;
	private final A arguments;
	public AbstractCommand(String name, Type type, A arguments) {
		this.name = name;
		this.type = type;
		this.arguments = arguments;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public Type getType() {
		return type;
	}
	public A getArguments() {
		return arguments;
	}
	@Override
	public Command<?> join(Command<?> next) {
		return JoinedCommand.join(this, next);
	}
	@Override
	public final void execute(Input input, Output output) {
		switch (getType()) {
		case NoInput:
			executeBatch(input, output);
			break;
		case LineByLine:
			do {
				executeBatch(input, output);
			} while (input.hasMoreLines());
			break;
		case CompleteInput:
			executeBatch(input, output);
			break;
		default:
			throw new IllegalStateException("unknown command type: " + getType());
		}
		output.finish();
	}
	abstract protected void executeBatch(Input input, Output output);
	@Override
	public String toString() {
		final String args = getArguments().toString();
		return args.isEmpty() ? getName() : getName() + " " + args;
	}
}
