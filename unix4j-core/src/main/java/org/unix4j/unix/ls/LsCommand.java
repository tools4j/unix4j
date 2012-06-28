package org.unix4j.unix.ls;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.unix4j.command.AbstractCommand;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.unix.Ls;
import org.unix4j.unix.Ls.Option;
import org.unix4j.util.CompositeComparator;
import org.unix4j.util.ReverseOrderComparator;

/**
 * Ls command implementation.
 */
class LsCommand extends AbstractCommand<LsArgs> {
	public LsCommand(LsArgs arguments) {
		super(Ls.NAME, Type.NoInput, arguments);
	}

	@Override
	public LsCommand withArgs(LsArgs arguments) {
		return new LsCommand(arguments);
	}

	@Override
	public void executeBatch(Input input, Output output) {
		final LsArgs args = getArguments();
		final Comparator<File> comparator = getComparator();
		final List<File> files = args.getFiles();
		final File[] sorted = files.toArray(new File[files.size()]);
		listFiles(null, sorted, comparator, output);
	}

	private void listFiles(File parent, File[] files, Comparator<File> comparator, Output output) {
		final LsArgs args = getArguments();
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
		void writeFormatted(File file, LsArgs args, Output output);
	}

	private static OutputFormatter FORMATTER_SHORT = new OutputFormatter() {
		@Override
		public void writeFormatted(File file, LsArgs args, Output output) {
			output.writeLine(file.getPath());
		}
	};
	private static OutputFormatter FORMATTER_DIRECTORY_HEADER = new OutputFormatter() {
		@Override
		public void writeFormatted(File file, LsArgs args, Output output) {
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
	 * -rw-r--r--@  1 terz  staff   1.6K May  8 23:20 EFMTool Test Case.zip
	 * -rw-r--r--   1 terz  terz    2.6M May  5  2011 EM_FX_RANK_2011-1.pages
	 * -rw-r--r--   1 terz  staff   466K May  5  2011 EM_FX_RANK_2011-1.pdf
	 * -rw-r--r--   1 terz  staff   1.3M May  5  2011 EM_FX_RANK_2011.pdf
	 * -rw-r--r--@  1 terz  staff   118M Apr 11  2011 PS_AIO_07_B110_USW_Full_Mac_WW_11.dmg
	 * drwxrwxrwx   5 terz  staff   170B Mar  6  2011 Private Tax
	 * -rw-r--r--   1 terz  terz     37M Apr 26  2011 Scanned Image 111160000.pdf
	 * -rw-r--r--   1 terz  terz     37M Apr 26  2011 Scanned Image 111160008.pdf
	 * drwxrwxrwx   9 terz  staff   306B Apr 19  2011 aevum
	 * drwxr-xr-x   3 terz  staff   102B Dec 21  2011 baby
	 * drwxr-xr-x   4 terz  staff   136B May 18  2011 bills
	 * drwxrwxrwx  17 terz  staff   578B Jun 15 11:21 cv
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
		public void writeFormatted(File file, LsArgs args, Output output) {
			output.writeLine(
					getFilePermissions(file, args) + ' ' +
							getOwner(file, args) + ' ' +
							getGroup(file, args) + ' ' +
							getSize(file, args) + ' ' +
							getDateTime(file, args) + ' ' +
							getName(file, args)
					);
		}

		private String getFilePermissions(File file, LsArgs args) {
			return
			(file.isDirectory() ? 'd' : '-') +
					"?--?--" +
					(file.canRead() ? 'r' : '-') +
					(file.canWrite() ? 'w' : '-') +
					(file.canExecute() ? 'x' : '-');
		}

		private String getOwner(File file, LsArgs args) {
			return fixSizeString(7, true, "???");
		}

		private String getGroup(File file, LsArgs args) {
			return fixSizeString(7, true, "???");
		}

		private String getSize(File file, LsArgs args) {
			return args.getSizeString(file.length());
		}

		private String getDateTime(File file, LsArgs args) {
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

		private String getName(File file, LsArgs args) {
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
			if (left)
				sb.append(s);
			for (int i = 0; i < size - s.length(); i++) {
				sb.append(filler);
			}
			if (!left)
				sb.append(s);
			return sb.toString();
		} else {
			return left ? s.substring(0, size) : s.substring(s.length() - size, s.length());
		}
	}

}