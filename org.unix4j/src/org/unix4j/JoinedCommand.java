package org.unix4j;

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
	public Command<O2> withArg(String arg) {
		second.withArg(arg);
		return this;
	}

	@Override
	public Command<O2> withArgs(String... args) {
		second.withArgs(args);
		return this;
	}

	@Override
	public Command<O2> withOpt(O2 option) {
		second.withOpt(option);
		return this;
	}
	
	public Command<O2> withOpt(O2 option, Object optionValue) {
		second.withOpt(option, optionValue);
		return this;
	}

	@Override
	public Command<O2> withOpts(O2... options) {
		second.withOpts(options);
		return this;
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
