package org.unix4j.command.unix;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.CompositeComparator;
import org.unix4j.util.ReverseOrderComparator;
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
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link Factory} to
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
		 *         implementing classes like the command {@link Factory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R ls(File... files);

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
		 *         implementing classes like the command {@link Factory} to
		 *         return a new {@link Command} instance for the argument values
		 *         passed to this method. {@link CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
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
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link Output} object. This serves
		 *         implementing classes like the command {@link Factory} to
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
	 * 
	 * -l long format, displaying Unix file types, permissions, number of hard
	 * links, owner, group, size, date, and filename -F appends a character
	 * revealing the nature of a file, for example, * for an executable, or /
	 * for a directory. Regular files have no suffix. -a lists all files in the
	 * given directory, including those whose names start with "." (which are
	 * hidden files in Unix). By default, these files are excluded from the
	 * list. -R recursively lists subdirectories. The command ls -R / would
	 * therefore list all files. -d shows information about a symbolic link or
	 * directory, rather than about the link's target or listing the contents of
	 * a directory. -t sort the list of files by modification time. -h print
	 * sizes in human readable format. (e.g., 1K, 234M, 2G, etc.)
	 * 
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

		@Override
		public boolean hasOpt(Option opt) {
			return super.hasOpt(opt) || super.hasOpt(opt.alias);
		}

		public String getSizeString(long bytes) {
			if (hasOpt(Option.humanReadable)) {
				final String units = "BKMG";
				int unit = 0;
				int fraction = 0;
				while (bytes > 1000 && (unit + 1) < units.length()) {
					bytes /= 100;
					fraction = (int)(bytes % 10);
					bytes /= 10;
					unit++;
				}
				if (bytes < 10) {
					return bytes + "." + fraction + units.charAt(unit);
				} else {
					return (bytes < 100 ? " " : "") + bytes + units.charAt(unit);
				}
			}
			return String.valueOf(bytes);
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
			final Args args = getArguments();
			final Comparator<File> comparator = getComparator();
			final List<File> files = args.getFiles();
			final File[] sorted = files.toArray(new File[files.size()]);
			listFiles(null, sorted, comparator, output);
		}

		private void listFiles(File parent, File[] files, Comparator<File> comparator, Output output) {
			final Args args = getArguments();
			final OutputFormatter formatter = args.hasOpt(Option.longFormat) ? FORMATTER_LONG : FORMATTER_SHORT;
			final boolean allFiles = args.hasOpt(Option.allFiles);
			final boolean recurseSubdirs = parent == null || args.hasOpt(Option.recurseSubdirs);
			Arrays.sort(files, comparator);
			for (File file : files) {
				if (allFiles || !file.isHidden()) {
					if (file.isDirectory() && recurseSubdirs) {
						FORMATTER_DIRECTORY_HEADER.writeFormatted(file, getArguments(), output);
						listFiles(file, file.listFiles(), comparator, output);
					} else {
						formatter.writeFormatted(file, getArguments(), output);
					}
				}
			}
		}
		private Comparator<File> getComparator() {
			return getArguments().hasOpt(Option.reverseOrder) ? ReverseOrderComparator.reverse(DEFAULT_COMPARATOR) : DEFAULT_COMPARATOR;
		}

	}

	private static Comparator<File> FILE_TYPE_COMPARATOR = new Comparator<File>() {
		@Override
		public int compare(File f1, File f2) {
			if (f1.isHidden()) {
				return f2.isHidden() ? 0 : -1;
			} else if (f2.isHidden()) {
				return 1;
			}
			if (f1.isFile()) {
				return f2.isFile() ? 0 : -1;
			} else if (f2.isFile()) {
				return 1;
			}
			if (!f1.isDirectory()) {
				return !f2.isDirectory() ? 0 : -1;
			} else if (!f2.isDirectory()) {
				return 1;
			}
			return 0;// both directories
		}
	};

	private static Comparator<File> FILE_NAME_COMPARATOR = new Comparator<File>() {
		@Override
		public int compare(File f1, File f2) {
			return f1.getName().compareTo(f2.getName());
		}
	};

	/**
	 * Sorts files according to file type and then by name. Hidden files are
	 * listed first, then normal files, all other files and finally the
	 * directories.
	 */
	private static Comparator<File> DEFAULT_COMPARATOR = new CompositeComparator<File>(FILE_TYPE_COMPARATOR, FILE_NAME_COMPARATOR);

	private static interface OutputFormatter {
		void writeFormatted(File file, Args args, Output output);
	}

	private static OutputFormatter FORMATTER_SHORT = new OutputFormatter() {
		@Override
		public void writeFormatted(File file, Args args, Output output) {
			output.writeLine(file.getPath());
		}
	};
	private static OutputFormatter FORMATTER_DIRECTORY_HEADER = new OutputFormatter() {
		@Override
		public void writeFormatted(File file, Args args, Output output) {
			output.writeLine(file.getPath());
			long totalBytes = 0;
			for (final File f : file.listFiles()) {
				if (f.isFile()) {
					totalBytes += file.length();
				}
			}
			output.writeLine("total: " + args.getSizeString(totalBytes));
		}
	};
	
	/**
	 * <pre>
-rw-r--r--@  1 terz  staff   1.6K May  8 23:20 EFMTool Test Case.zip
-rw-r--r--   1 terz  terz    2.6M May  5  2011 EM_FX_RANK_2011-1.pages
-rw-r--r--   1 terz  staff   466K May  5  2011 EM_FX_RANK_2011-1.pdf
-rw-r--r--   1 terz  staff   1.3M May  5  2011 EM_FX_RANK_2011.pdf
-rw-r--r--@  1 terz  staff   118M Apr 11  2011 PS_AIO_07_B110_USW_Full_Mac_WW_11.dmg
drwxrwxrwx   5 terz  staff   170B Mar  6  2011 Private Tax
-rw-r--r--   1 terz  terz     37M Apr 26  2011 Scanned Image 111160000.pdf
-rw-r--r--   1 terz  terz     37M Apr 26  2011 Scanned Image 111160008.pdf
drwxrwxrwx   9 terz  staff   306B Apr 19  2011 aevum
drwxr-xr-x   3 terz  staff   102B Dec 21  2011 baby
drwxr-xr-x   4 terz  staff   136B May 18  2011 bills
drwxrwxrwx  17 terz  staff   578B Jun 15 11:21 cv
	 * </pre>
	 */
	private static OutputFormatter FORMATTER_LONG = new OutputFormatter() {
		private final ThreadLocal<Calendar> calendar = new ThreadLocal<Calendar>() {
			@Override
			protected Calendar initialValue() {
				return Calendar.getInstance();
			}
		};
		@Override
		public void writeFormatted(File file, Args args, Output output) {
			output.writeLine(
					getFilePermissions(file, args) + ' ' +
					getOwner(file, args) + ' ' +
					getGroup(file, args) + ' ' +
					getSize(file, args) + ' ' +
					getDateTime(file, args) + ' ' +
					getName(file, args)
				);
		}

		private String getFilePermissions(File file, Args args) {
			return 
			(file.isDirectory() ? 'd' : '-') +
			"?--?--" + 
			(file.canRead() ? 'r' : '-') + 
			(file.canWrite() ? 'w' : '-') + 
			(file.canExecute() ? 'x' : '-'); 
		}
		private String getOwner(File file, Args args) {
			return fixSizeString(7, true, "???");
		}
		private String getGroup(File file, Args args) {
			return fixSizeString(7, true, "???");
		}
		private String getSize(File file, Args args) {
			return args.getSizeString(file.length());
		}
		private String getDateTime(File file, Args args) {
			final Calendar cal = calendar.get();
			cal.setTimeInMillis(System.currentTimeMillis());
			final int curYear = cal.get(Calendar.YEAR);
			cal.setTimeInMillis(file.lastModified());
			final int fileYear = cal.get(Calendar.YEAR);
			final String month = cal.getDisplayName(Calendar.MONDAY, Calendar.SHORT, Locale.getDefault());
			
			final String sM = fixSizeString(3, true, month);
			final String sD = fixSizeString(2, false, ' ', cal.get(Calendar.DAY_OF_MONTH));
			if (curYear == fileYear) {
				final String sHou = fixSizeString(2, false, '0', cal.get(Calendar.HOUR_OF_DAY));
				final String sMin = fixSizeString(2, false, '0', cal.get(Calendar.MINUTE));
				return sM + ' ' + sD + ' ' + sHou + ':' + sMin;
			} else {
				final String sY = fixSizeString(5, false, ' ', fileYear + 1900);
				return sM + ' ' + sD + ' ' + sY;
			}
		}
		private String getName(File file, Args args) {
			return file.getName();
		}
	};
	
	private static final String fixSizeString(int size, boolean left, char filler, int value) {
		return fixSizeString(size, left, filler, String.valueOf(value));
	}
	private static final String fixSizeString(int size, boolean left, String s) {
		return fixSizeString(size, left, ' ', s);
	}
	private static final String fixSizeString(int size, boolean left, char filler, String s) {
		if (s.length() < size) {
			final StringBuilder sb = new StringBuilder(size);
			if (left) sb.append(s);
			for (int i = 0; i < size - s.length(); i++) {
				sb.append(filler);
			}
			if (!left) sb.append(s);
			return sb.toString();
		} else {
			return left ? s.substring(0, size) : s.substring(s.length() - size, s.length());
		}
	}
	
	// no instances
	private Ls() {
		super();
	}
}
