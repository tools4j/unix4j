package org.unix4j.unix.from;

import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.From;
import org.unix4j.util.FileUtil;

/**
 * Implementation of the pseudo {@link From from} command used for input
 * redirection.
 */
class FromCommand extends AbstractCommand<FromArguments> {
	public FromCommand(FromArguments arguments) {
		super(From.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final FromArguments args = getArguments(context);
		final Input input;
		
		if (args.isPathSet()) {
			final String path = args.getPath();
			final List<File> files = FileUtil.expandFiles(context.getCurrentDirectory(), path);
			if (files.size() == 1) {
				input = new FileInput(files.get(0));
			} else {
				throw new IllegalArgumentException("expected one file, but found " + files.size() + " for path: " + path);
			}
		} else if (args.isInputSet()) {
			input = args.getInput();
		} else {
			throw new IllegalStateException("neither path nor input argument has been specified");
		}
		return new LineProcessor() {
			@Override
			public boolean processLine(Line line) {
				return false;// we ARE the input, so we don't want any more
								// lines
			}

			@Override
			public void finish() {
				for (final Line line : input) {
					if (!output.processLine(line)) {
						break;
					}
				}
				output.finish();
			}
		};
	}
}
