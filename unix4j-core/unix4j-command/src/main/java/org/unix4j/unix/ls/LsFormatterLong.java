package org.unix4j.unix.ls;

import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.FileUtil;
import org.unix4j.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
final class LsFormatterLong implements LsFormatter {

	private final ThreadLocal<Calendar> calendar = ThreadLocal.withInitial(Calendar::getInstance);
	private final ThreadLocal<Integer> maxSizeStringLength = ThreadLocal.withInitial(() -> Integer.valueOf(0));

	@Override
	public boolean writeFormatted(File relativeTo, File file, LsArguments args, LineProcessor output) {
		return output.processLine(
				new SimpleLine(
						getFilePermissions(file, args) + ' ' +
								getOwner(file, args) + ' ' +
								getGroup(file, args) + ' ' +
								getSize(file, args) + ' ' +
								getDateTime(file, args) + ' ' +
								getName(relativeTo, file, args)
				)
		);
	}

	LsFormatterLong(List<File> directoryFiles, LsArguments args) {
		maxSizeStringLength.set(calculateMaxSizeStringLength(directoryFiles, args));
	}

	private String getOwner(File file, LsArguments args) {
		try {
			final String owner = Files.getOwner(file.toPath()).getName();
			return StringUtil.fixSizeString(7, true, owner);
		} catch (IOException e) {
			return StringUtil.fixSizeString(7, true, "???");
		}
	}

	private String getGroup(File file, LsArguments args) {
		try {
			final PosixFileAttributeView view = Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class);
			final String group = view.readAttributes().group().getName();
			return StringUtil.fixSizeString(7, true, group);
		} catch (Exception e) {
			return StringUtil.fixSizeString(7, true, "???");
		}
	}

	private String getFilePermissions(File file, LsArguments args) {
		try {
			final Set<PosixFilePermission> perms = Files.getPosixFilePermissions(file.toPath());
			return (file.isDirectory() ? "d" : "-") +
				(perms.contains(PosixFilePermission.OWNER_READ) ? 'r' : '-') +
				(perms.contains(PosixFilePermission.OWNER_WRITE) ? 'w' : '-') +
				(perms.contains(PosixFilePermission.OWNER_EXECUTE) ? 'x' : '-') +
				(perms.contains(PosixFilePermission.GROUP_READ) ? 'r' : '-') +
				(perms.contains(PosixFilePermission.GROUP_WRITE) ? 'w' : '-') +
				(perms.contains(PosixFilePermission.GROUP_EXECUTE) ? 'x' : '-') +
				(perms.contains(PosixFilePermission.OTHERS_READ) ? 'r' : '-') +
				(perms.contains(PosixFilePermission.OTHERS_WRITE) ? 'w' : '-') +
				(perms.contains(PosixFilePermission.OTHERS_EXECUTE) ? 'x' : '-');
		} catch (Exception e) {
			final boolean r = file.canRead();
			final boolean w = file.canWrite();
			final boolean x = file.canExecute();
			return (file.isDirectory() ? "d" : "-") +
					//owner
					(r ? 'r' : '-') +
					(w ? 'w' : '-') +
					(x ? 'x' : '-') +
					//group
					(r ? '.' : '-') +
					(w ? '.' : '-') +
					(x ? '.' : '-') +
					//other
					(r ? '.' : '-') +
					(w ? '.' : '-') +
					(x ? '.' : '-');
		}
	}

	private long getLastModifiedMS(File file, LsArguments args) {
		try {
			return Files.getLastModifiedTime(file.toPath()).toMillis();
		} catch (Exception e) {
			return file.lastModified();
		}
	}

	private String getDateTime(File file, LsArguments args) {
		final Calendar cal = calendar.get();
		cal.setTimeInMillis(System.currentTimeMillis());//set current date to get current year below
		final int curYear = cal.get(Calendar.YEAR);
		cal.setTimeInMillis(getLastModifiedMS(file, args));
		final int fileYear = cal.get(Calendar.YEAR);
		final String month = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());

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

	protected String getName(File relativeTo, File file, LsArguments args) {
		return FileUtil.getRelativePath(relativeTo, file);
	}

	private String getSize(File file, LsArguments args) {
		long size;
		try {
			size = Files.size(file.toPath());
		} catch (Exception e) {
			size = file.length();
		}
		final String sizeString = LsCommand.getSizeString(args, size);
		return StringUtil.fixSizeString(maxSizeStringLength.get(), false, sizeString);
	}

	private static int calculateMaxSizeStringLength(List<File> directoryFiles, LsArguments args) {
		int maxSizeStringLength = 0;
		for (final File f : directoryFiles) {
			if (f.isFile()) {
				if (f.isFile()) {
					final String sizeString = LsCommand.getSizeString(args, f.length());
					maxSizeStringLength = Math.max(maxSizeStringLength, sizeString.length());
				}
			}
		}
		return maxSizeStringLength;
	}
}
