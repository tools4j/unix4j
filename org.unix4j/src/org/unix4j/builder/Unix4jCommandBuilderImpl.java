package org.unix4j.builder;

import java.io.File;
import java.util.List;

import org.unix4j.command.Command;
import org.unix4j.command.impl.Echo;
import org.unix4j.command.impl.Grep;
import org.unix4j.command.impl.Ls;
import org.unix4j.command.impl.Sort;
import org.unix4j.command.impl.Xargs;
import org.unix4j.io.Input;

public class Unix4jCommandBuilderImpl extends DefaultCommandBuilder implements Unix4jCommandBuilder {
	
	public Unix4jCommandBuilderImpl() {
		super();
	}
	public Unix4jCommandBuilderImpl(Input input) {
		super(input);
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
	
	@Override
	public Unix4jCommandBuilder join(Command<?> command) {
		super.join(command);
		return this;
	}
}
