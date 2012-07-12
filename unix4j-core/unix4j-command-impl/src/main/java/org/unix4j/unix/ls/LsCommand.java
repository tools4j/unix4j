package org.unix4j.unix.ls;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.unix.Ls;
import org.unix4j.unix.Ls.Option;
import org.unix4j.util.FileUtil;
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
		final List<File> files = args.getFiles();
		final List<File> expanded = FileUtil.expandFiles(files);
		listFiles(FileUtil.getUserDir(), null, expanded, output);
	}


	private void listFiles(File relativeTo, File parent, List<File> files, Output output) {
		final LsArgs args = getArguments();
		final Comparator<File> comparator = getComparator(relativeTo);
		final boolean allFiles = args.hasOpt(Option.allFiles);
		final boolean longFormat = args.hasOpt(Option.longFormat);
		final LsFormatter formatter = longFormat ? LsFormatterLong.FACTORY.create(relativeTo, parent, files, args) : LsFormatterShort.INSTANCE;
		final boolean recurseSubdirs = parent == null || args.hasOpt(Option.recurseSubdirs);

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
		final Comparator<File> comparator = args.hasOpt(Option.timeSorted) ? FileComparators.timeAndRelativeFileName(relativeTo) : FileComparators.typeAndRelativeFileName(relativeTo);
		return args.hasOpt(Option.reverseOrder) ? ReverseOrderComparator.reverse(comparator) : comparator;
	}

}