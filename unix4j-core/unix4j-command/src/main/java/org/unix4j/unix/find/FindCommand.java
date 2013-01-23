package org.unix4j.unix.find;

import static java.lang.String.format;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Find;
import org.unix4j.util.FileUtil;

/**
 * Implementation of the {@link Find find} command.
 */
class FindCommand extends AbstractCommand<FindArguments> {
	
	private final FileFilter fileFilter;
	private final String lineEnding;
	
	public FindCommand(FindArguments args) {
		super(Find.NAME, args);
		this.fileFilter = createFileFilterFromArgs(args);
		this.lineEnding = args.isPrint0() ? String.valueOf(Line.ZERO) : Line.LINE_ENDING;
	}

	private FileFilter createFileFilterFromArgs(FindArguments args) {
		final CompositeFileFilter filter = new CompositeFileFilter();
		if (args.isNameSet()) {
			final String name = args.getName();
			if (args.isRegex()) {
				filter.add(new RegexNameFilter(name, args.isIgnoreCase()));
			} else {
				if (name.contains("*") || name.contains("?")) {
					filter.add(new RegexNameFilter(name.replace('?', '.').replace("*", ".*"), args.isIgnoreCase()));
				} else {
					filter.add(new NameFilter(args.getName(), args.isIgnoreCase()));
				}
			}
		}
		if (args.isSizeSet()) {
			filter.add(new SizeFilter(args.getSize()));
		}
		if (args.isTimeSet()) {
			filter.add(new TimeFilter(args.getTime(), args.getOptions()));
		}
		filter.addIfNotNull(TypeFilter.valueOf(args.getOptions()));
		return filter.simplify();
	}

	private List<File> getArgumentPaths(ExecutionContext context, FindArguments args) {
		if (args.isPathSet()) {
			return FileUtil.expandFiles(context.getCurrentDirectory(), args.getPath());
		} else {
			//no input files or paths ==> use just the current directory
			final ArrayList<File> list = new ArrayList<File>(1);
			list.add(context.getCurrentDirectory());
			return list;
		}
	}

	@Override
	public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
		final FindArguments args = getArguments(context);
		final List<File> paths = getArgumentPaths(context, args);
		return new LineProcessor() {
			@Override
			public boolean processLine(Line line) {
				return false;//we ignore all input
			}
			@Override
			public void finish() {
				for(final File path: paths){
					final boolean keepGoing;

					if(!path.exists()){
						keepGoing = output.processLine(new SimpleLine(format("find: `%s': No such file or directory", path), lineEnding));
					} else if(path.isDirectory()){
						keepGoing = listFiles(path, path, output, args);
					} else {
						keepGoing = outputFileLine(output, path, path);
					}
					if(!keepGoing){
						break;
					}
				}
				output.finish();
			}
		};
	}

	private boolean listFiles(File relativeTo, File parent, LineProcessor output, FindArguments args) {
		//print directory files and recurse
		if (outputFileLine(output, relativeTo, parent)) {
			final List<File> files = FileUtil.toList(parent.listFiles());
			for (File file : files) {
				//System.out.println("Examining file: " + file.getAbsolutePath());
				if (file.isDirectory()) {
					if (!listFiles(relativeTo, file, output, args)) {
						return false;
					}
				} else {
					if (!outputFileLine(output, relativeTo, file)) {
						return false;
					}
				}
			}
		}
		return true;//we want more output
	}
	private boolean outputFileLine(LineProcessor output, File relativeTo, File file) {
		if (fileFilter.accept(file)) {
			final String filePath = FileUtil.getRelativePath(relativeTo, file);
			return output.processLine(new SimpleLine(filePath, lineEnding));
		}
		return true;
	}
}