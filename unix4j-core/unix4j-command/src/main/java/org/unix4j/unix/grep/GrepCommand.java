package org.unix4j.unix.grep;

import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
import org.unix4j.unix.Grep;
import org.unix4j.util.FileUtil;

/**
 * User: ben
 */
public class GrepCommand extends AbstractCommand<GrepArguments> {
	public GrepCommand(GrepArguments arguments) {
		super(Grep.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		final GrepArguments args = getArguments();
		
		//from file?
		if (args.isFilesSet()) {
			final List<FileInput> inputs = FileInput.multiple(args.getFiles());
			return getFileInputProcessor(inputs, context, output);
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(args.getPaths());
			final List<FileInput> inputs = FileInput.multiple(files);
			return getFileInputProcessor(inputs, context, output);
		}
		
		//from standard input
		return getStandardInputProcessor(context, output);
	}
	
	private LineProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output) {
		if (getArguments().isFixedStrings()) {
			return new FixedStringsProcessor(this, context, output);
		} else {
			return new RegexpProcessor(this, context, output);
		}
	}
	private LineProcessor getFileInputProcessor(List<FileInput> inputs, ExecutionContext context, LineProcessor output) {
		final LineProcessor standardInputProcessor = getStandardInputProcessor(context, output); 
		return new RedirectInputLineProcessor(inputs, standardInputProcessor);
	}
}
