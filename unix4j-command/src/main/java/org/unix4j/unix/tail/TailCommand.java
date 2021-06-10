package org.unix4j.unix.tail;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.*;
import org.unix4j.unix.Tail;
import org.unix4j.util.FileUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of the {@link Tail tail} command.
 */
class TailCommand extends AbstractCommand<TailArguments> {
	public TailCommand(TailArguments arguments) {
		super(Tail.NAME, arguments);
		if (arguments.isCountSet() && arguments.getCount() < 0) {
			throw new IllegalArgumentException("count cannot be negative: " + arguments);
		}
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final TailArguments args = getArguments(context);
		
		//input from file(s)?
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
		
		//read from standard input
		return getStandardInputProcessor(context, output, args);
	}
	
	private AbstractTailProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output, TailArguments args) {
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
	
	private LineProcessor getFileInputProcessor(List<? extends Input> inputs, final ExecutionContext context, final LineProcessor output, TailArguments args) {
		final AbstractTailProcessor tailProcessor = getStandardInputProcessor(context, output, args);
		if (inputs.size() <= 1 || args.isSuppressHeaders()) {
			return new RedirectInputLineProcessor(inputs, tailProcessor);
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
					final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo(context.getCurrentDirectory()) : input.toString();
					output.processLine(new SimpleLine("==> " + fileInfo + " <=="));
				}

                @Override
                public void finish(Input input, LineProcessor output) {
                    super.finish(input, output);
                    tailProcessor.resetCountersAndFlush();
                }
            };
			return new MultipleInputLineProcessor(inputs, inputProcessor, tailProcessor);
		}
	}

}