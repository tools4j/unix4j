package org.unix4j.unix.cat;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
import org.unix4j.unix.Cat;
import org.unix4j.util.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@link Cat cat} command.
 */
class CatCommand extends AbstractCommand<CatArguments> {
	public CatCommand(CatArguments arguments) {
		super(Cat.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final CatArguments args = getArguments(context);

		// input from files?
		if (args.isFilesSet()) {
			final List<FileInput> inputs = FileInput.multiple(args.getFiles());
			return getFileInputProcessor(inputs, context, output, args);
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(context.getCurrentDirectory(), args.getPaths());
			final List<FileInput> inputs = FileInput.multiple(files);
			return getFileInputProcessor(inputs, context, output, args);
		} else if (args.isInputsSet()) {
			final List<Input> inputs = Arrays.asList(args.getInputs());
			return getFileInputProcessor(inputs, context, output, args);
		}

		// standard input
		return getStandardInputProcessor(context, output, args);
	}

	private LineProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output, CatArguments args) {
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

	private LineProcessor getFileInputProcessor(List<? extends Input> inputs, ExecutionContext context, LineProcessor output, CatArguments args) {
		final LineProcessor standardInputProcessor = getStandardInputProcessor(context, output, args); 
		return new RedirectInputLineProcessor(inputs, standardInputProcessor);
	}
}
