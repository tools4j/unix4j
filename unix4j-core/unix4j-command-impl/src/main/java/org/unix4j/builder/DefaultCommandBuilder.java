package org.unix4j.builder;

import org.unix4j.command.Command;
import org.unix4j.io.BufferedOutput;
import org.unix4j.io.FileOutput;
import org.unix4j.io.Input;
import org.unix4j.io.NullInput;
import org.unix4j.io.Output;
import org.unix4j.io.StdOutput;

import java.io.File;

public class DefaultCommandBuilder implements CommandBuilder {

	protected final Input input;
	protected Command<?> command = null;

	public DefaultCommandBuilder() {
		this(NullInput.INSTANCE);
	}
	public DefaultCommandBuilder(Input input) {
		this.input = input;
	}
	public CommandBuilder join(Command<?> command) {
		this.command = this.command == null ? command : this.command.join(command);
		return this;
	}
	@Override
	public Command<?> build() {
		if (command == null) {
			throw new IllegalStateException("no command has been built yet");
		}
		return command;
	}
	@Override
	public String toString() {
		return command == null ? "nop" : command.toString();
	}
	@Override
	public void toStdOut() {
		toOutput(new StdOutput());
	}
	@Override
	public void toOutput(Output output) {
		build().execute(input, output);
		output.finish();
	}
	@Override
	public void toFile(File file) {
		toOutput(new FileOutput(file));
	}
	@Override
	public String toStringResult() {
		final BufferedOutput out = new BufferedOutput();
		toOutput(out);
		return out.toMultiLineString();
	}
}
