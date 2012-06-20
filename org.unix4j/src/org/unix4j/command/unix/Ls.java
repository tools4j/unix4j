package org.unix4j.command.unix;

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

/**
 * Non-instantiable module with inner types making up the ls command.
 */
public final class Ls {

	/**
	 * The "ls" command name.
	 */
	public static final String NAME = "ls";

	/**
	 * Interface defining all method signatures for the ls command.
	 * 
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command builder providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Lists all files and directories in the user's current working
		 * directory and writes them to the output.
		 * 
		 * @return the generic return type {@link R}, usually a new command
		 *         signature or a builder with methods for chained invocation of
		 *         following commands
		 */
		R ls();

		/**
		 * Prints the name of the given files and lists all files contained in
		 * directories for every directory in {@code files}.
		 * 
		 * @param files
		 *            the files or directories used as starting point for the
		 *            listing
		 * @return the generic return type {@link R}, usually a new command
		 *         signature or a builder with methods for chained invocation of
		 *         following commands
		 */
		R ls(File... files);

		/**
		 * Lists all files and directories in the user's current working
		 * directory and writes them to the output using the given options
		 * specifying the details of the output format.
		 * 
		 * @param options
		 *            the options defining the output format
		 * @return the generic return type {@link R}, usually a new command
		 *         signature or a builder with methods for chained invocation of
		 *         following commands
		 */
		R ls(Option... options);

		/**
		 * Prints the name of the given files and lists all files contained in
		 * directories for every directory in {@code files}. The given options
		 * define the details of the output format.
		 * 
		 * @param files
		 *            the files or directories used as starting point for the
		 *            listing
		 * @param options
		 *            the options defining the output format
		 * @return the generic return type {@link R}, usually a new command
		 *         signature or a builder with methods for chained invocation of
		 *         following commands
		 */
		R ls(List<File> files, Option... options);
	}

	/**
	 * Option flags for the ls command.
	 */
	public static enum Option {
		// l,a,r,t;
	}

	/**
	 * Arguments and options for the ls command.
	 */
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

	/**
	 * Singleton {@link Factory} for the ls command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
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

	/**
	 * Ls command implementation.
	 */
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
			boolean single = files.size() == 1;
			for (final File file : files) {
				if (file.isDirectory()) {
					if (!single) {
						output.writeLine(file.toString());
					}
					for (File f : file.listFiles()) {
						if (single) {
							output.writeLine(f.getName());
						} else {
							output.writeLine("\t" + f.getName());
						}
					}
				} else {
					if (single) {
						output.writeLine(file.getName());
					} else {
						output.writeLine("\t" + file.getName());
					}
				}
			}
		}
	}

	// no instances
	private Ls() {
		super();
	}
}
