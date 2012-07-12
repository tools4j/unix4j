package org.unix4j.unix;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.Command;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Output;
import org.unix4j.unix.ls.LsFactory;
import org.unix4j.unix.ls.LsOptionSet;

/**
 * <b>NAME</b>
 * <p>
 * ls - list directory contents
 * </p>
 * <b>SYNOPSIS</b>
 * <p>
 * <pre>
 * ls [-ahlRrt] [file...]
 * </pre>
 * </p>
 * <b>DESCRIPTION</b>
 * <p>
 * For each operand that names a file of a type other than directory or symbolic
 * link to a directory, {@code ls} writes the name of the file as well as any
 * requested, associated information. For each operand that names a file of type
 * directory, {@code ls} writes the names of files contained within the
 * directory as well as any requested, associated information. If the <b>-l</b>
 * option is specified, for each operand that names a file of type symbolic link
 * to a directory, {@code ls} writes the name of the file as well as any
 * requested, associated information. If the <b>-l</b> option is not specified,
 * for each operand that names a file of type symbolic link to a directory,
 * {@code ls} writes the names of files contained within the directory as well
 * as any requested, associated information.
 * </p><p>
 * If no operands are specified, {@code ls} writes the contents of the current
 * directory. If more than one operand is specified, {@code ls} writes
 * non-directory operands first; it sorts directory and non-directory operands
 * separately according to the name of the file or directory.
 * </p><p>
 * TODO The {@code ls} utility detects infinite loops; that is, entering a
 * previously visited directory that is an ancestor of the last file
 * encountered. When it detects an infinite loop, {@code ls} aborts the
 * recursion.
 * </p>
 * <b>NOTES</b>
 * <p>
 * <ul>
 * <li>Infinite loop detection is currently NOT implemented.</li>
 * </ul>
 * </p>
 * <b>OPTIONS</b>
 * <p>
 * The following options are supported:
 * </p><p>
 * <table>
 * <tr><td><pre>-a  --allFiles        </pre></td><td>Lists all files in the given directory, including those whose names start with "." (which are hidden files in Unix). By default, these files are excluded from the list.</td></tr>
 * <tr><td><pre>-h  --humanReadable   </pre></td><td>Print sizes in human readable format. (e.g., 1K, 234M, 2G, etc.).</td></tr>
 * <tr><td><pre>-l  --longFormat      </pre></td><td>Long format, displaying Unix file types, permissions, number of hard links, owner, group, size, date, and filename.</td></tr>
 * <tr><td><pre>-r  --reverseOrder    </pre></td><td>Reverses the order of the sort to get reverse collating sequence or oldest first.</td></tr>
 * <tr><td><pre>-R  --recurseSubdirs  </pre></td><td>Recursively lists subdirectories encountered.</td></tr>
 * <tr><td><pre>-t  --timeSorted      </pre></td><td>Sort the list of files by last modification time.</td></tr>
 * </table>
 * </p>
 * <b>OPERANDS</b>
 * </p><p>
 * The following operand is supported:
 * <table>
 * <tr><td><pre>   file   </pre></td><td>A pathname of a file to be written. Wildcards * and ? are
 * supported</td></tr>
 * </table>
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
	 *            new command instance or a command fromFile providing methods
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
		R ls(File... files);

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
		R ls(OptionSet options);

		/**
		 * Prints the name of the given files and lists all files contained in
		 * directories for every directory in {@code files}. The given options
		 * define the details of the output format.
		 * 
		 * @param options
		 *            the options defining the output format
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
		R ls(OptionSet options, File... files);

		/**
		 * Prints the name of the given files and lists all files contained in
		 * directories for every directory in {@code files}. The given options
		 * define the details of the output format.
		 * 
		 * @param options
		 *            the options defining the output format
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
		R ls(OptionSet options, String... files);
	}

	/**
	 * Option flags for the ls command.
	 */
	public static enum Option implements OptionSet {
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

		// interface OptionSet

		@Override
		public EnumSet<Option> asSet() {
			return EnumSet.of(this);
		}

		@Override
		public boolean isSet(Option option) {
			return equals(option);
		}

		public OptionSet set(Option option) {
			return new LsOptionSet(this, option);
		}

		@Override
		public OptionSet allFiles() {
			return set(allFiles);
		}

		@Override
		public OptionSet a() {
			return set(a);
		}

		@Override
		public OptionSet humanReadable() {
			return set(humanReadable);
		}

		@Override
		public OptionSet h() {
			return set(h);
		}

		@Override
		public OptionSet longFormat() {
			return set(longFormat);
		}

		@Override
		public OptionSet l() {
			return set(l);
		}

		@Override
		public OptionSet reverseOrder() {
			return set(reverseOrder);
		}

		@Override
		public OptionSet r() {
			return set(r);
		}

		@Override
		public OptionSet recurseSubdirs() {
			return set(recurseSubdirs);
		}

		@Override
		public OptionSet R() {
			return set(R);
		}

		@Override
		public OptionSet timeSorted() {
			return set(timeSorted);
		}

		@Override
		public OptionSet t() {
			return set(t);
		}
	}

	public static interface OptionSet extends org.unix4j.optset.OptionSet<Ls.Option> {
		OptionSet allFiles();

		OptionSet a();

		OptionSet humanReadable();

		OptionSet h();

		OptionSet longFormat();

		OptionSet l();

		OptionSet reverseOrder();

		OptionSet r();

		OptionSet recurseSubdirs();

		OptionSet R();

		OptionSet timeSorted();

		OptionSet t();
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
