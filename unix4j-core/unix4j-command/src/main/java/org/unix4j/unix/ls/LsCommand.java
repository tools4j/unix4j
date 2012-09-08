package org.unix4j.unix.ls;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Ls;
import org.unix4j.util.FileUtil;
import org.unix4j.util.ReverseOrderComparator;

/**
 * Ls command implementation.
 */
class LsCommand extends AbstractCommand<LsArguments> {
	public LsCommand(LsArguments arguments) {
		super(Ls.NAME, arguments);
	}
	
	private List<File> getArgumentFiles(ExecutionContext context) {
		final LsArguments args = getArguments();
		if (args.isFilesSet()) {
			return new ArrayList<File>(Arrays.asList(args.getFiles()));
		} else if (args.isPathsSet()) {
			return FileUtil.expandFiles(args.getPaths());
		}
		//no input files or paths ==> use just the current directory
		final ArrayList<File> list = new ArrayList<File>(1);
		list.add(context.getCurrentDirectory());
		return list;
	}

	/*package*/ static String getSizeString(LsArguments args, long bytes) {
		if (args.isHumanReadable()) {
			final String units = "BKMG";
			int unit = 0;
			int fraction = 0;
			while (bytes > 1000 && (unit + 1) < units.length()) {
				bytes /= 100;
				fraction = (int) (bytes % 10);
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
	
	@Override
	public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
		final List<File> files = getArgumentFiles(context);
		return new LineProcessor() {
			@Override
			public boolean processLine(Line line) {
				return false;//we ignore all input
			}
			@Override
			public void finish() {
				listFiles(context.getCurrentDirectory(), null, files, output);
				output.finish();
			}
		};
	}

	private void listFiles(File relativeTo, File parent, List<File> files, LineProcessor output) {
		final LsArguments args = getArguments();
		final Comparator<File> comparator = getComparator(relativeTo);
		final boolean allFiles = args.isAllFiles();
		final boolean longFormat = args.isLongFormat();
		final LsFormatter formatter = longFormat ? LsFormatterLong.FACTORY.create(relativeTo, parent, files, args) : LsFormatterShort.INSTANCE;
		final boolean recurseSubdirs = parent == null || args.isRecurseSubdirs();

		//add special directories . and ..
		if (parent != null && allFiles) {
			//add special directories . and ..
			files.add(parent);
			final File grandParent = parent.getAbsoluteFile().getParentFile();
			if (grandParent != null) {
				files.add(grandParent);
			}
		}
		
		//sort files
		Collections.sort(files, comparator);
		
		//print directory header
		if (parent != null) {
			final LsFormatter dirHeaderFmt = LsFormatterDirectoryHeader.FACTORY.create(relativeTo, parent, files, args);
			dirHeaderFmt.writeFormatted(relativeTo, parent, args, output);
		}
		//print directory files and recurse if necessary
		for (File file : files) {
			if (allFiles || !file.isHidden()) {
				if (file.isDirectory() && recurseSubdirs) {
					listFiles(file, file, FileUtil.toList(file.listFiles()), output);
				} else {
					formatter.writeFormatted(relativeTo, file, getArguments(), output);
				}
			}
		}
	}

	private Comparator<File> getComparator(File relativeTo) {
		final LsArguments args = getArguments();
		final Comparator<File> comparator = args.isTimeSorted() ? FileComparators.timeAndRelativeFileName(relativeTo) : FileComparators.typeAndRelativeFileName(relativeTo);
		return args.isReverseOrder() ? ReverseOrderComparator.reverse(comparator) : comparator;
	}

}