package org.unix4j.unix.ls;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import org.unix4j.io.Output;

/**
 * Interface used by the different output formats of the ls command.
 */
interface LsFormatter {
	/**
	 * Writes information of the given file to the {@code output}.
	 * 
	 * @param file
	 *            the file whose name or other information is written to
	 *            {@code output}
	 * @param args
	 *            arguments possibly used to lookup some formatting options
	 * @param output
	 *            the output object to write to
	 */
	void writeFormatted(File file, LsArgs args, Output output);
	
	LsFormatter SHORT = new LsFormatter() {
		@Override
		public void writeFormatted(File file, LsArgs args, Output output) {
			output.writeLine(file.getPath());
		}
	};
	
	LsFormatter DIRECTORY_HEADER = new LsFormatter() {
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
	LsFormatter LONG = new LsFormatter() {
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
			return StringUtil.fixSizeString(7, true, "???");
		}

		private String getGroup(File file, LsArgs args) {
			return StringUtil.fixSizeString(7, true, "???");
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

			final String sM = StringUtil.fixSizeString(3, true, month);
			final String sD = StringUtil.fixSizeString(2, false, ' ', cal.get(Calendar.DAY_OF_MONTH));
			if (curYear == fileYear) {
				final String sHou = StringUtil.fixSizeString(2, false, '0', cal.get(Calendar.HOUR_OF_DAY));
				final String sMin = StringUtil.fixSizeString(2, false, '0', cal.get(Calendar.MINUTE));
				return sM + ' ' + sD + ' ' + sHou + ':' + sMin;
			} else {
				final String sY = StringUtil.fixSizeString(5, false, ' ', fileYear);
				return sM + ' ' + sD + ' ' + sY;
			}
		}

		private String getName(File file, LsArgs args) {
			return file.getName();
		}
	};
	
}