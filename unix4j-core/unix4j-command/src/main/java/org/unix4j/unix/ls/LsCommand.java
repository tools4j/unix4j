package org.unix4j.unix.ls;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.unix.Ls;
import org.unix4j.util.FileUtil;
import org.unix4j.util.ReverseOrderComparator;

/**
 * Ls command implementation.
 */
class LsCommand extends AbstractCommand<LsArgs> {
	public LsCommand(LsArgs arguments) {
		super(Ls.NAME, arguments);
	}

	@Override
	public LsCommand withArgs(LsArgs arguments) {
		return new LsCommand(arguments);
	}

	@Override
	public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
		return new LineProcessor() {
			@Override
			public boolean processLine(Line line) {
				return false;//we ignore all input
			}
			@Override
			public void finish() {
				final LsArgs args = getArguments();
				final List<File> files = args.getFiles();
				final List<File> expanded = FileUtil.expandFiles(files);
				listFiles(context.getCurrentDirectory(), null, expanded, output);
				output.finish();
			}
		};
	}

	private void listFiles(File relativeTo, File parent, List<File> files, LineProcessor output) {
		final LsArgs args = getArguments();
		final Comparator<File> comparator = getComparator(relativeTo);
		final boolean allFiles = args.hasOpt(LsOption.allFiles);
		final boolean longFormat = args.hasOpt(LsOption.longFormat);
		final LsFormatter formatter = longFormat ? LsFormatterLong.FACTORY.create(relativeTo, parent, files, args) : LsFormatterShort.INSTANCE;
		final boolean recurseSubdirs = parent == null || args.hasOpt(LsOption.recurseSubdirs);

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
		final LsArgs args = getArguments();
		final Comparator<File> comparator = args.hasOpt(LsOption.timeSorted) ? FileComparators.timeAndRelativeFileName(relativeTo) : FileComparators.typeAndRelativeFileName(relativeTo);
		return args.hasOpt(LsOption.reverseOrder) ? ReverseOrderComparator.reverse(comparator) : comparator;
	}

}