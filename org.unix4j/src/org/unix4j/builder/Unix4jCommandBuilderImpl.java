package org.unix4j.builder;

import java.io.File;
import java.util.List;

import org.unix4j.command.Command;
import org.unix4j.command.unix.Cut;
import org.unix4j.command.unix.Echo;
import org.unix4j.command.unix.Grep;
import org.unix4j.command.unix.Ls;
import org.unix4j.command.unix.Sort;
import org.unix4j.command.unix.Xargs;
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
	public Unix4jCommandBuilder cut(int fieldIndex) {
		join(Cut.FACTORY.cut(fieldIndex));
		return this;
	}
	
	@Override
	public Unix4jCommandBuilder cut(String delimiter, int... fieldIndices) {
		join(Cut.FACTORY.cut(delimiter, fieldIndices));
		return this;
	}
	
	@Override
	public Unix4jCommandBuilder cut(String inputDelimiter, String outputDelimiter, int... fieldIndices) {
		join(Cut.FACTORY.cut(inputDelimiter, outputDelimiter, fieldIndices));
		return this;
	}
	
	@Override
	public Unix4jCommandBuilder cut(int start, int length) {
		join(Cut.FACTORY.cut(start, length));
		return this;
	}
	
	@Override
	public Unix4jCommandBuilder cut(int[] charIndices) {
		join(Cut.FACTORY.cut(charIndices));
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
