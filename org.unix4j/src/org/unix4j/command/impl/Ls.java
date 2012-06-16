package org.unix4j.command.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.TypedMap;


public final class Ls {
	
	public static final String NAME = "ls";
	
	public static interface Interface<S> extends CommandInterface<S> {
		S ls();
		S ls(File... files);
		S ls(Option... options);
		S ls(List<File> files, Option... options);
	}

	public static enum Option {
		l,a,r,t;
	}

	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<List<File>> FILES = TypedMap.DefaultKey.keyForListOf("files", File.class);
		public Args() {
			this(new File(System.getProperty("user.dir")));
		}
		public Args(File file) {
			this(Collections.singletonList(file));
		}
		public Args(File... files) {
			this(Arrays.asList(files));
		}
		public Args(List<File> files) {
			super(Option.class);
			setArg(FILES, files);
		}
		public List<File> getFiles() {
			return getArg(FILES);
		}
	}
	
	public static final Factory FACTORY = new Factory();
	public static final class Factory implements Interface<Command> {
		@Override
		public Command ls() {
			return new Command(new Args());
		}
		@Override
		public Command ls(File... files) {
			return new Command(new Args(files));
		}
		@Override
		public Command ls(Option... options) {
			final Args args = new Args();
			args.setOpts(options);
			return new Command(args);
		}
		@Override
		public Command ls(List<File> files, Option... options) {
			final Args args = new Args(files);
			args.setOpts(options);
			return new Command(args);
		}
	};
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.NoInput, arguments);
		}
		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}
		@Override
		public void executeBatch(Input input, Output output) {
			final List<File> files = getArguments().getFiles();
			for (final File file : files) {
				for (File f : file.listFiles()) {
					output.writeLine(f.toString());
				}
			}
		}
	}
	
	// no instances
	private Ls() {
		super();
	}
}
