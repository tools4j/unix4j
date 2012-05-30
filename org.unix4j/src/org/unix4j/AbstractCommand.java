package org.unix4j;

import org.unix4j.cmd.CommandArguments;
import org.unix4j.io.NullInput;
import org.unix4j.io.StdOutput;

abstract public class AbstractCommand<O extends Enum<O>> implements Command<O> {

	private final String name;
	private final boolean batchable;
	private CommandArguments<O> args;
	private Input input = new NullInput();
	private Output output = new StdOutput();

	public AbstractCommand(String name, boolean batchable) {
		this.name = name;
		this.batchable = batchable;
		args = new CommandArguments<O>();
	}

	abstract protected void executeBatch();

	@Override
	public final void execute() {
		do {
			executeBatch();
		} while (getInput().hasMoreLines());
		getOutput().finish();
	}

	protected CommandArguments<O> getArgs() {
		return args;
	}

	protected boolean isOptSet(O option) {
		return args.isOptSet(option);
	}

	protected Input getInput() {
		return input;
	}

	protected Output getOutput() {
		return output;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isBatchable() {
		return batchable;
	}

	@Override
	public Command<O> withOpt(O option) {
		args.addArg(option);
		return this;
	}

	@Override
	public Command<O> withArg(O option, String value) {
		args.addArg(option, value);
		return this;
	}

	@Override
	public Command<O> withArgs(O option, String ... values) {
		args.addArgs(option, values);
		return this;
	}

	@Override
	public Command<O> withArgs(String... values) {
		args.addArgs(getDefaultArgumentOption(), values);
		return this;
	}

	@Override
	public Command<O> withArg(String value) {
		args.addArg(getDefaultArgumentOption(), value);
		return this;
	}

	@Override
	public Command<O> withOpts(O... options) {
		args.addArgs(options);
		return this;
	}

	@Override
	public Command<O> readFrom(Input input) {
		if (input == null) {
			throw new NullPointerException("input cannot be null");
		}
		this.input = input;
		return this;
	}

	@Override
	public Command<O> writeTo(Output output) {
		if (output == null) {
			throw new NullPointerException("output cannot be null");
		}
		this.output = output;
		return this;
	}

	@Override
	public <O2 extends Enum<O2>> JoinedCommand<O2> join(Command<O2> next) {
		return new JoinedCommand<O2>(this, next);
	}

	@Override
	public AbstractCommand<O> clone() {
		try {
			@SuppressWarnings("unchecked")
			final AbstractCommand<O> clone = (AbstractCommand<O>) super.clone();
			clone.args = this.args.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable: " + getClass().getName());
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getName());
		sb.append(" ").append(args.toString());
		return sb.toString();
	}
}