package org.unix4j.unix.cat;

import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
import org.unix4j.unix.Cat;
import org.unix4j.unix.Echo;
import org.unix4j.util.FileUtil;

/**
 * Implementation of the {@link Cat cat} command.
 */
class CatCommand extends AbstractCommand<CatArguments> {
	public CatCommand(CatArguments arguments) {
		super(Echo.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final CatArguments args = getArguments();

		// input from files?
		if (args.isFilesSet()) {
			final List<FileInput> inputs = FileInput.multiple(args.getFiles());
			return getFileInputProcessor(inputs, context, output);
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(args.getPaths());
			final List<FileInput> inputs = FileInput.multiple(files);
			return getFileInputProcessor(inputs, context, output);
		}

		// standard input
		return getStandardInputProcessor(context, output);
	}

	private LineProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output) {
		final CatArguments args = getArguments();
		final LineProcessor printer;
		if (args.isNumberLines() || args.isNumberNonBlankLines()) {
			printer = new NumberLinesProcessor(this, context, output);
		} else {
			printer = output;
		}
		if (args.isSqueezeEmptyLines()) {
			return new SqueezeEmptyLinesProcessor(this, context, printer);
		}
		return new CatProcessor(this, context, printer);
	}

	private LineProcessor getFileInputProcessor(List<FileInput> inputs, ExecutionContext context, LineProcessor output) {
		final LineProcessor standardInputProcessor = getStandardInputProcessor(context, output); 
		return new RedirectInputLineProcessor(inputs, standardInputProcessor);
	}
}
