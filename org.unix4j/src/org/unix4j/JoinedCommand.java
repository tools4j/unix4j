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
	public Command<O2> withOpt(O2 option) {
		second.withOpt(option);
		return this;
	}

	@Override
	public Command<O2> withArg(O2 option, String optionValue) {
		second.withArg(option, optionValue);
		return this;
	}

	@Override
	public Command<O2> withOpts(O2... options) {
		second.withOpts(options);
		return this;
	}

	@Override
	public Command<O2> withArgs(O2 option, String... argValues) {
		second.withArgs(option, argValues);
		return this;
	}

	@Override
	public Command<O2> withArgs(String ... values) {
		second.withArgs(values);
		return this;
	}

	@Override
	public Command<O2> withArg(String value) {
		second.withArg(value);
		return this;
	}

	@Override
	public O2 getDefaultArgumentOption() {
		return second.getDefaultArgumentOption();
	}

	@Override
	public Command<O2> writeTo(Output output) {
		second.writeTo(output);
		return this;
	}

	@Override
	public <O3 extends Enum<O3>> JoinedCommand<O3> join(Command<O3> next) {
		return new JoinedCommand<O3>(this, second.join(next));
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

}