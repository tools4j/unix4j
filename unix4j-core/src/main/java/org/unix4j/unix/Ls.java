package org.unix4j.unix;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.Command;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Output;
import org.unix4j.unix.ls.LsFactory;

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
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link LsFactory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R ls();

		/**
		 * Prints the name of the given files and lists all files contained in
		 * directories for every directory in {@code files}.
		 * 
		 * @param files
		 *            the files or directories used as starting point for the
		 *            listing
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link LsFactory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R ls(String... files);

		/**
		 * Lists all files and directories in the user's current working
		 * directory and writes them to the output using the given options
		 * specifying the details of the output format.
		 * 
		 * @param options
		 *            the options defining the output format
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link LsFactory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R ls(Option... options);

		/**
		 * Prints the name of the given file and lists all files contained in
		 * {@code files} if it is a directory. The given options define the
		 * details of the output format.
		 * 
		 * @param file
		 *            the file or directory used as starting point for the
		 *            listing
		 * @param options
		 *            the options defining the output format
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link LsFactory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R ls(File file, Option... options);

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
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link LsFactory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R ls(List<File> files, Option... options);
	}

	/**
	 * Option flags for the ls command.
	 */
	public static enum Option {
		/**
		 * Lists all files in the given directory, including hidden files (those
		 * whose names start with "." in Unix). By default, these files are
		 * excluded from the list.
		 * <p>
		 * This option is identical to the option {@link #a}.
		 */
		allFiles,
		/**
		 * Lists all files in the given directory, including hidden files (those
		 * whose names start with "." in Unix). By default, these files are
		 * excluded from the list.
		 * <p>
		 * This option is identical to the option {@link #allFiles}.
		 */
		a(allFiles),
		/**
		 * Print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.)
		 * <p>
		 * This option is identical to the option {@link #h}.
		 */
		humanReadable,
		/**
		 * Print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.)
		 * <p>
		 * This option is identical to the option {@link #humanReadable}.
		 */
		h(humanReadable),
		/**
		 * Long format, displaying file types, permissions, number of hard
		 * links, owner, group, size, date, and filename.
		 * <p>
		 * This option is identical to the option {@link #l}.
		 */
		longFormat,
		/**
		 * Long format, displaying file types, permissions, number of hard
		 * links, owner, group, size, date, and filename.
		 * <p>
		 * This option is identical to the option {@link #longFormat}.
		 */
		l(longFormat),
		/**
		 * Recursively lists subdirectories encountered.
		 * <p>
		 * This option is identical to the option {@link #R}.
		 */
		recurseSubdirs,
		/**
		 * Recursively lists subdirectories encountered.
		 * <p>
		 * This option is identical to the option {@link #recurseSubdirs}.
		 */
		R(recurseSubdirs),
		/**
		 * Reverses the order of the sort to get reverse collating sequence or
		 * oldest first.
		 * <p>
		 * This option is identical to the option {@link #r}.
		 */
		reverseOrder,
		/**
		 * Reverses the order of the sort to get reverse collating sequence or
		 * oldest first.
		 * <p>
		 * This option is identical to the option {@link #reverseOrder}.
		 */
		r(reverseOrder),
		/**
		 * Sorts with the primary key being time modified (most recently
		 * modified first) and the secondary key being filename in the collating
		 * sequence.
		 * <p>
		 * This option is identical to the option {@link #t}.
		 */
		timeSorted,
		/**
		 * Sorts with the primary key being time modified (most recently
		 * modified first) and the secondary key being filename in the collating
		 * sequence.
		 * <p>
		 * This option is identical to the option {@link #timeSorted}.
		 */
		t(timeSorted);

		private Option alias;

		private Option() {
			this.alias = null;
		}

		private Option(Option alias) {
			this.alias = alias;
			alias.alias = this;
		}

		public boolean isSame(Option option) {
			return equals(option) || alias.equals(option);
		}

		public boolean isSet(Set<? extends Option> options) {
			return options.contains(this) || options.contains(alias);
		}
	}

	/**
	 * Singleton {@link LsFactory factory} instance for the ls command.
	 */
	public static final LsFactory FACTORY = LsFactory.INSTANCE;

	// no instances
	private Ls() {
		super();
	}
}
