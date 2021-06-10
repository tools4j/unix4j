package org.unix4j.unix.ls;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Ls;
import org.unix4j.util.FileUtil;
import org.unix4j.util.sort.ReverseOrderComparator;

import java.io.File;
import java.util.*;

/**
 * Implementation of the {@link Ls ls} command.
 */
class LsCommand extends AbstractCommand<LsArguments> {
	public LsCommand(LsArguments arguments) {
		super(Ls.NAME, arguments);
	}
	
	private List<File> getArgumentFiles(ExecutionContext context, LsArguments args) {
		if (args.isFilesSet()) {
			return new ArrayList<File>(Arrays.asList(args.getFiles()));
		} else if (args.isPathsSet()) {
			return FileUtil.expandFiles(context.getCurrentDirectory(), args.getPaths());
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
		final LsArguments args = getArguments(context);
		final List<File> files = getArgumentFiles(context, args);
		return new LineProcessor() {
			@Override
			public boolean processLine(Line line) {
				return false;//we ignore all input
			}
			@Override
			public void finish() {
				listFiles(context.getCurrentDirectory(), null, files, output, args);
				output.finish();
			}
		};
	}

	private boolean listFiles(File relativeTo, File parent, List<File> files, LineProcessor output, LsArguments args) {
		final Comparator<File> comparator = getComparator(parent == null ? relativeTo : parent, args);
		final boolean allFiles = args.isAllFiles();
		final boolean longFormat = args.isLongFormat();
		final LsFormatter formatter = longFormat ? new LsFormatterLong(files, args) : LsFormatterShort.INSTANCE;

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
			if (!dirHeaderFmt.writeFormatted(relativeTo, parent, args, output)) {
				return false;
			}
		}
		//print directory files and recurse if necessary
		for (File file : files) {
			if (allFiles || !file.isHidden()) {
				if (file.isDirectory() && parent == null && !args.isRecurseSubdirs()) {
					if (!listFiles(relativeTo, file, FileUtil.toList(file.listFiles()), output, args)) {
						return false;
					}
				} else {
					if (!file.isDirectory() || parent != null) {
						if (!formatter.writeFormatted(parent == null ? relativeTo : parent, file, args, output)) {
							return false;
						}
					}
				}
			}
		}
		
		//recurse subdirs now
		if (args.isRecurseSubdirs()) {
			for (File file : files) {
				if (allFiles || !file.isHidden()) {
					if (file.isDirectory() && (parent == null || !file.equals(parent) && !file.equals(parent.getParentFile()))) {
						if (!listFiles(relativeTo, file, FileUtil.toList(file.listFiles()), output, args)) {
							return false;
						}
					}
				}
			}
		}
		
		return true;//we want more output
	}

	private Comparator<File> getComparator(File relativeTo, LsArguments args) {
		final Comparator<File> comparator = args.isTimeSorted() ? FileComparators.timeAndRelativeFileName(relativeTo) : FileComparators.typeAndRelativeFileName(relativeTo);
		return args.isReverseOrder() ? ReverseOrderComparator.reverse(comparator) : comparator;
	}

}