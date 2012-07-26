package org.unix4j.builder;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.unix4j.command.Command;
import org.unix4j.command.DefaultExecutionContext;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.BufferedOutput;
import org.unix4j.io.FileOutput;
import org.unix4j.io.Input;
import org.unix4j.io.NullInput;
import org.unix4j.io.Output;
import org.unix4j.io.StdOutput;
import org.unix4j.io.StreamOutput;
import org.unix4j.io.WriterOutput;

public class DefaultCommandBuilder implements CommandBuilder {

	protected Input input = null;
	protected Command<?,?> command = null;

	public DefaultCommandBuilder() {
		super();
	}
	public DefaultCommandBuilder(Input input) {
		this.input = input;
	}
	public CommandBuilder join(Command<?,?> command) {
		this.command = this.command == null ? command : this.command.join(command);
		return this;
	}
	@Override
	public Command<?,?> build() {
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
	public List<String> toLineList() {
		final List<String> lines = new ArrayList<String>();
		toOutput(new BufferedOutput(lines));
		return lines;
	}
	@Override
	public void toOutput(Output output) {
		execute(build(), output);
	}
	@Override
	public void toFile(String file) {
		toFile(new File(file));
	}
	@Override
	public void toFile(File file) {
		toOutput(new FileOutput(file));
	}
	@Override
	public void toOutputStream(OutputStream stream) {
		toOutput(new StreamOutput(stream));
	}
	@Override
	public void toWriter(Writer writer) {
		toOutput(new WriterOutput(writer));
	}
	@Override
	public String toStringResult() {
		final BufferedOutput out = new BufferedOutput();
		toOutput(out);
		return out.toMultiLineString();
	}
	
	private <L> void execute(Command<?,L> command, Output output) {
		final ExecutionContext<L> context = DefaultExecutionContext.start(command, true);
		command.execute(context, input == null ? NullInput.INSTANCE : input, output);
		output.finish();
	}
}
