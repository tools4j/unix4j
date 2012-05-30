package org.unix4j;

import java.util.List;

import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgList;
import org.unix4j.arg.Opt;
import org.unix4j.arg.OptMap;
import org.unix4j.io.NullInput;
import org.unix4j.io.StdOutput;

abstract public class AbstractCommand<E extends Enum<E>> implements Command<E> {

	private final String name;
	private final boolean batchable;
	private OptMap<E> opts = new OptMap<E>();
	private Input input = new NullInput();
	private Output output = new StdOutput();

	public AbstractCommand(String name, boolean batchable) {
		this.name = name;
		this.batchable = batchable;
	}

	abstract protected void executeBatch();

	@Override
	public final void execute() {
		do {
			executeBatch();
		} while (getInput().hasMoreLines());
		getOutput().finish();
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

	public <V> Command<E> withArg(Arg<E,V> arg, V value) {
		opts.addArg(arg, value);
		return this;
	}
	
	public <V> Command<E> withArgs(Arg<E,V> arg, V... values) {
		opts.addArgs(arg, values);
		return this;
	}

	public <V> Command<E> withArgs(Arg<E,V> arg, List<? extends V> values) {
		opts.addArgs(arg, values);
		return this;
	}
	
	@Override
	public Command<E> withOpt(Opt<E> opt) {
		opts.setOpt(opt);
		return this;
	}

	@Override
	public <V> ArgList<E,V> getArgs(Arg<E, V> arg) {
		return opts.getArgList(arg, true);
	}
	
	@Override
	public boolean isOptSet(Opt<E> opt) {
		return opts.isOptSet(opt);
	}

	@Override
	public Command<E> readFrom(Input input) {
		if (input == null) {
			throw new NullPointerException("input cannot be null");
		}
		this.input = input;
		return this;
	}

	@Override
	public Command<E> writeTo(Output output) {
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
	public AbstractCommand<E> clone() {
		try {
			@SuppressWarnings("unchecked")
			final AbstractCommand<E> clone = (AbstractCommand<E>)super.clone();
			clone.opts= this.opts.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable: " + getClass().getName());
		}
	}
	
	@Override
	public String toString() {
		return getName() + " " + opts;
	}

}
