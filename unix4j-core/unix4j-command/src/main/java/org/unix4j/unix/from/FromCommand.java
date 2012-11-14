package org.unix4j.unix.from;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.From;

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
		final Input input = getArguments(context).getInput();
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
