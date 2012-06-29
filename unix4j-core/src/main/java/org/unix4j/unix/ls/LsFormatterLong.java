package org.unix4j.unix.ls;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import org.unix4j.io.Output;

/**
 * Formatter for long file output including permissions, owner, size etc. An
 * example output with long format looks like this:
 * 
 * <pre>
 * -rw-r--r--@  1 myself  geeks   1.6K May  8 23:20 EFMTool Test Case.zip
 * -rw-r--r--   1 myself  myself  2.6M May  5  2011 EM_FX_RANK_2011-1.pages
 * -rw-r--r--   1 myself  geeks   466K May  5  2011 EM_FX_RANK_2011-1.pdf
 * -rw-r--r--   1 myself  geeks   1.3M May  5  2011 EM_FX_RANK_2011.pdf
 * -rw-r--r--@  1 myself  geeks   118M Apr 11  2011 PS_AIO_07_B110_USW_Full_Mac_WW_11.dmg
 * drwxrwxrwx   5 myself  geeks   170B Mar  6  2011 Private Tax
 * -rw-r--r--   1 myself  myself   37M Apr 26  2011 Scanned Image 111160000.pdf
 * -rw-r--r--   1 myself  mysel    37M Apr 26  2011 Scanned Image 111160008.pdf
 * drwxrwxrwx   9 myself  geeks   306B Apr 19  2011 aevum
 * drwxr-xr-x   3 myself  geeks   102B Dec 21  2011 baby
 * drwxr-xr-x   4 myself  geeks   136B May 18  2011 bills
 * drwxrwxrwx  17 myself  geeks   578B Jun 15 11:21 cv
 * </pre>
 */
public class LsFormatterLong implements LsFormatter {

	private final ThreadLocal<Calendar> calendar = new ThreadLocal<Calendar>() {
		@Override
		protected Calendar initialValue() {
			return Calendar.getInstance();
		}
	};

	protected Calendar getCalendar() {
		return calendar.get();
	}

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

	protected String getFilePermissions(File file, LsArgs args) {
		return (file.isDirectory() ? 'd' : '-') +
					"?--?--" +
					(file.canRead() ? 'r' : '-') +
					(file.canWrite() ? 'w' : '-') +
					(file.canExecute() ? 'x' : '-');
	}

	protected String getOwner(File file, LsArgs args) {
		return StringUtil.fixSizeString(7, true, "???");
	}

	protected String getGroup(File file, LsArgs args) {
		return StringUtil.fixSizeString(7, true, "???");
	}

	protected String getSize(File file, LsArgs args) {
		return args.getSizeString(file.length());
	}

	protected long getLastModifiedMS(File file, LsArgs args) {
		return file.lastModified();
	}
	protected String getDateTime(File file, LsArgs args) {
		final Calendar cal = calendar.get();
		cal.setTimeInMillis(System.currentTimeMillis());
		final int curYear = cal.get(Calendar.YEAR);
		cal.setTimeInMillis(getLastModifiedMS(file, args));
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

	protected String getName(File file, LsArgs args) {
		return file.getName();
	}

	static LsFormatter getInstance() {
		try {
			return new LsFormatterLong7();
		} catch (NoClassDefFoundError e) {
			return new LsFormatterLong();
		}
	}

}
