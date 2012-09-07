package org.unix4j.command;

import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;

/**
 * A line processor that can be returned by a command when the input is not read
 * from the standard input, but from an argument such as a file operand of the
 * command.
 * <p>
 * The {@link #processLine(Line)} method does nothing and returns false
 * indicating that no (standard) input is read by this processor. The
 * {@link #finish()} method reads the lines from the {@link Input} object passed
 * to the constructor and passes them as input to the delegate processor
 * performing the real work.
 */
public class InputArgumentLineProcessor implements LineProcessor {

	private final Input input;
	private final LineProcessor inputProcessor;

	public InputArgumentLineProcessor(Input input, LineProcessor inputProcessor) {
		this.input = input;
		this.inputProcessor = inputProcessor;
	}

	@Override
	public boolean processLine(Line line) {
		return false;// we want no input, we have it already
	}

	@Override
	public void finish() {
		for (final Line line : input) {
			if (!inputProcessor.processLine(line)) {
				break;// wants no more lines
			}
		}
		inputProcessor.finish();
	}
}