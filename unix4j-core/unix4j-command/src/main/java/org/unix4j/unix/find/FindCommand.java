package org.unix4j.unix.find;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Find;
import org.unix4j.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Implementation of the {@link Find find} command.
 */
class FindCommand extends AbstractCommand<FindArguments> {
	public FindCommand(FindArguments arguments) {
		super(Find.NAME, arguments);
	}

	private List<String> getArgumentPaths(ExecutionContext context, FindArguments args) {
		if (args.isPathsSet()) {
			return FileUtil.expandPaths(context.getCurrentDirectory(), args.getPaths());
		} else {
			//no input files or paths ==> use just the current directory
			final ArrayList<String> list = new ArrayList<String>(1);
			list.add(context.getCurrentDirectory().getPath());
			return list;
		}
	}

	@Override
	public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
		final FindArguments args = getArguments(context.getVariableContext());
		final List<String> paths = getArgumentPaths(context, args);
		return new LineProcessor() {
			@Override
			public boolean processLine(Line line) {
				return false;//we ignore all input
			}
			@Override
			public void finish() {
				for(final String path: paths){
					final File file = new File(path);
					final boolean keepGoing;

					if(!file.exists()){
						keepGoing = output.processLine(new SimpleLine(format("find: `%s': No such file or directory", path)));
					} else if(file.isDirectory()){
						keepGoing = listFiles(file, file, output, args);
					} else {
						keepGoing = output.processLine(new SimpleLine(path));
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
		final boolean recurseSubdirs = true;
		final List<File> files = FileUtil.toList(parent.listFiles());
		if(files.size() == 0) return true;

		//print directory files and recurse if necessary
		for (File file : files) {
			//System.out.println("Examining file: " + file.getAbsolutePath());
			if (!file.isHidden()) {
				if (file.isDirectory() && recurseSubdirs) {
					if (!listFiles(relativeTo, file, output, args)) {
						return false;
					}
				} else {
					if(!output.processLine(new SimpleLine(FileUtil.getRelativePath(relativeTo, file)))){
						return false;
					}
				}
			}
		}

		return true;//we want more output
	}
}