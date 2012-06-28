package org.unix4j.unix.ls;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.unix.Ls;
import org.unix4j.unix.Ls.Option;
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
		final LsFormatter formatter = args.hasOpt(Option.longFormat) ? LsFormatter.LONG : LsFormatter.SHORT;
		final boolean allFiles = args.hasOpt(Option.allFiles);
		final boolean recurseSubdirs = parent == null || args.hasOpt(Option.recurseSubdirs);
		Arrays.sort(files, comparator);
		for (File file : files) {
			if (allFiles || !file.isHidden()) {
				if (file.isDirectory() && recurseSubdirs) {
					LsFormatter.DIRECTORY_HEADER.writeFormatted(file, getArguments(), output);
					listFiles(file, file.listFiles(), comparator, output);
				} else {
					formatter.writeFormatted(file, getArguments(), output);
				}
			}
		}
	}

	private Comparator<File> getComparator() {
		final LsArgs args = getArguments();
		final Comparator<File> comparator = args.hasOpt(Option.timeSorted) ? FileComparators.TIME : FileComparators.DEFAULT;
		return args.hasOpt(Option.reverseOrder) ? ReverseOrderComparator.reverse(comparator) : comparator;
	}

}