package org.unix4j.builder;

import java.io.File;
import java.util.List;

import org.unix4j.command.Command;
import org.unix4j.command.impl.Echo;
import org.unix4j.command.impl.Grep;
import org.unix4j.command.impl.Ls;
import org.unix4j.command.impl.Sort;
import org.unix4j.command.impl.Xargs;
import org.unix4j.io.BufferedOutput;
import org.unix4j.io.FileOutput;
import org.unix4j.io.Input;
import org.unix4j.io.NullInput;
import org.unix4j.io.Output;
import org.unix4j.io.StdOutput;

public class Unix4jCommandBuilderImpl implements Unix4jCommandBuilder {
	
	private final Input input;
	
	private Command<?> command = null;

	public Unix4jCommandBuilderImpl() {
		this(NullInput.INSTANCE);
	}
	public Unix4jCommandBuilderImpl(Input input) {
		this.input = input;
	}

	@Override
	public Unix4jCommandBuilder ls() {
		join(Ls.FACTORY.ls());
		return this;
	}

	@Override
	public Unix4jCommandBuilder ls(File... files) {
		join(Ls.FACTORY.ls(files));
		return this;
	}

	@Override
	public Unix4jCommandBuilder ls(Ls.Option... options) {
		join(Ls.FACTORY.ls(options));
		return this;
	}

	@Override
	public Unix4jCommandBuilder ls(List<File> files, Ls.Option... options) {
		join(Ls.FACTORY.ls(files, options));
		return this;
	}

	@Override
	public Unix4jCommandBuilder grep(String matchString) {
		join(Grep.FACTORY.grep(matchString));
		return this;
	}

	@Override
	public Unix4jCommandBuilder grep(String matchString, Grep.Option... options) {
		join(Grep.FACTORY.grep(matchString, options));
		return this;
	}

	@Override
	public Unix4jCommandBuilder echo(String message) {
		join(Echo.FACTORY.echo(message));
		return this;
	}

	@Override
	public Unix4jCommandBuilder echo(String... messages) {
		join(Echo.FACTORY.echo(messages));
		return this;
	}

	@Override
	public Unix4jCommandBuilder sort() {
		join(Sort.FACTORY.sort());
		return this;
	}

	@Override
	public Unix4jCommandBuilder sort(Sort.Option... options) {
		join(Sort.FACTORY.sort(options));
		return this;
	}

	@Override
	public Unix4jCommandBuilder xargs() {
		join(Xargs.FACTORY.xargs());
		return this;
	}

	public void join(Command<?> command) {
		this.command = this.command == null ? command : this.command.join(command);
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
		return command == null ? "null" : command.toString();
	}

	@Override
	public void execute() {
		execute(new StdOutput());
	}
	@Override
	public void execute(Output output) {
		build().execute(input, output);
		output.finish();
	}
	@Override
	public void execute(File file) {
		execute(new FileOutput(file));
	}
	@Override
	public String executeToString() {
		final BufferedOutput out = new BufferedOutput();
		execute(out);
		return out.toMultiLineString();
	}
}
