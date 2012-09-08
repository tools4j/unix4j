package org.unix4j.unix.tail;

import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.DefaultInputProcessor;
import org.unix4j.processor.InputProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.MultipleInputLineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
import org.unix4j.unix.Tail;
import org.unix4j.util.FileUtil;

/**
 * Tail command implementation.
 */
public class TailCommand extends AbstractCommand<TailArguments> {
	public TailCommand(TailArguments arguments) {
		super(Tail.NAME, arguments);
		if (arguments.isCountSet() && arguments.getCount() < 0) {
			throw new IllegalArgumentException("count cannot be negative: " + arguments);
		}
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final TailArguments args = getArguments();
		
		//input from file(s)?
		if (args.isFilesSet()) {
			final List<FileInput> inputs = FileInput.multiple(args.getFiles());
			return getFileInputProcessor(inputs, context, output);
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(args.getPaths());
			final List<FileInput> inputs = FileInput.multiple(files);
			return getFileInputProcessor(inputs, context, output);
		}
		
		//read from standard input
		return getStandardInputProcessor(context, output);
	}
	
	private LineProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output) {
		final TailArguments args = getArguments();
		if (args.isChars()) {
			if (args.isCountFromStart()) {
				return new TailCharsFromStartProcessor(this, context, output);
			} else {
				return new TailCharsFromEndProcessor(this, context, output);
			}
		} else {
			if (args.isCountFromStart()) {
				return new TailLinesFromStartProcessor(this, context, output);
			} else {
				return new TailLinesFromEndProcessor(this, context, output);
			}
		}
	}
	
	private LineProcessor getFileInputProcessor(List<FileInput> inputs, ExecutionContext context, LineProcessor output) {
		final LineProcessor standardInputProcessor = getStandardInputProcessor(context, output);
		if (inputs.size() <= 1 || getArguments().isSuppressHeaders()) {
			return new RedirectInputLineProcessor(inputs, standardInputProcessor);
		} else {
			//write header line per file
			final InputProcessor inputProcessor = new DefaultInputProcessor() {
				@Override
				public void begin(Input input, LineProcessor output) {
					final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo() : input.toString();
					output.processLine(new SimpleLine("==> " + fileInfo + " <=="));
				}
			};
			return new MultipleInputLineProcessor(inputs, inputProcessor, standardInputProcessor);
		}
	}

}