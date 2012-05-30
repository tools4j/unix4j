package org.unix4j;

import java.util.List;

import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgList;
import org.unix4j.arg.Opt;

public class JoinedCommand<O2 extends Enum<O2>> implements Command<O2> {
	
	private final Command<?> first;
	private final Command<O2> second;
	
	public JoinedCommand(Command<?> first, Command<O2> second) {
		this.first = first;
		this.second = second;
	}

	public Command<?> getFirst() {
		return first;
	}
	public Command<O2> getSecond() {
		return second;
	}
	
	@Override
	public String getName() {
		return first.getName() + " | " + second.getName();
	}
	
	@Override
	public boolean isBatchable() {
		return first.isBatchable();
	}

	@Override
	public <V> Command<O2> withArg(Arg<O2,V> arg, V value) {
		second.withArg(arg, value);
		return this;
	}
	@Override
	public <V> Command<O2> withArgs(Arg<O2,V> arg, V... values) {
		second.withArgs(arg, values);
		return this;
	}
	@Override
	public <V> Command<O2> withArgs(Arg<O2,V> arg, List<? extends V> values) {
		second.withArgs(arg, values);
		return this;
	}
	
	@Override
	public <V> ArgList<O2, V> getArgs(Arg<O2, V> arg) {
		return second.getArgs(arg);
	}

	@Override
	public Command<O2> withOpt(Opt<O2> opt) {
		second.withOpt(opt);
		return this;
	}
	
	@Override
	public boolean isOptSet(Opt<O2> opt) {
		return second.isOptSet(opt);
	}

	@Override
	public Command<O2> writeTo(Output output) {
		second.writeTo(output);
		return this;
	}

	@Override
	public <O3 extends Enum<O3>> JoinedCommand<O3> join(Command<O3> next) {
		return new JoinedCommand<O3>(this, next);
	}

	@Override
	public Command<O2> readFrom(Input input) {
		first.readFrom(input);
		return this;
	}
	@Override
	public void execute() {
		new Join(this).execute();
	}
	
	public JoinedCommand<O2> clone() {
		return new JoinedCommand<O2>(first.clone(), second.clone());
	}

	@Override
	public String toString() {
		return first + " | " + second;
	}
	
}
