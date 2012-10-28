package org.unix4j.unix.head;

import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.DefaultInputProcessor;
import org.unix4j.processor.InputProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.MultipleInputLineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
import org.unix4j.unix.Head;
import org.unix4j.util.FileUtil;

/**
 * Implementation of the {@link Head head} command.
 */
class HeadCommand extends AbstractCommand<HeadArguments> {
	public HeadCommand(HeadArguments arguments) {
		super(Head.NAME, arguments);
		if (arguments.isCountSet() && arguments.getCount() < 0) {
			throw new IllegalArgumentException("count cannot be negative: " + arguments);
		}
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final HeadArguments args = getArguments(context.getVariableContext());
		
		//input from file(s)?
		if (args.isFilesSet()) {
			final List<FileInput> inputs = FileInput.multiple(args.getFiles());
			return getFileInputProcessor(inputs, context, output, args);
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(args.getPaths());
			final List<FileInput> inputs = FileInput.multiple(files);
			return getFileInputProcessor(inputs, context, output, args);
		}
		
		//read from standard input
		return getStandardInputProcessor(context, output, args);
	}
	
	private LineProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output, HeadArguments args) {
		if (args.isChars()) {
			//count chars
			return new HeadCharsProcessor(this, context, output);
		} else {
			//count lines
			return new HeadLinesProcessor(this, context, output);
		}
	}
	
	private LineProcessor getFileInputProcessor(List<FileInput> inputs, ExecutionContext context, final LineProcessor output, HeadArguments args) {
		final LineProcessor standardInputProcessor = getStandardInputProcessor(context, output, args);
		if (inputs.size() <= 1 || args.isSuppressHeaders()) {
			return new RedirectInputLineProcessor(inputs, standardInputProcessor);
		} else {
			//write header line per file
			final InputProcessor inputProcessor = new DefaultInputProcessor() {
				private boolean firstFile = true;
				@Override
				public void begin(Input input, LineProcessor standardInputProcessor) {
					if (firstFile) {
						firstFile = false;
					} else {
						output.processLine(Line.EMPTY_LINE);
					}
					final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo() : input.toString();
					output.processLine(new SimpleLine("==> " + fileInfo + " <=="));
				}
			};
			return new MultipleInputLineProcessor(inputs, inputProcessor, standardInputProcessor);
		}
	}
}