package org.unix4j.processor;

import org.unix4j.command.ExitValueException;
import org.unix4j.io.Input;
import org.unix4j.line.Line;

/**
 * A line processor for a single input
 */
public class InputLineProcessor implements LineProcessor {
	private final InputProcessor processor;
	private final LineProcessor output;
    private final Input input;

	public InputLineProcessor(Input input, InputProcessor processor, LineProcessor output) {
		this.input = input;
		this.processor = processor;
		this.output = output;
	}

	@Override
	public boolean processLine(Line line) {
		return false;// we want no input, we have it already
	}

	@Override
	public void finish() {
        try {
            processor.begin(input, output);
            for (final Line line : input) {
                if (!processor.processLine(input, line, output)) {
                    break;// wants no more lines
                }
            }
            processor.finish(input, output);
        } catch (ExitValueException e) {
            e.setInput(input);
            throw e;
        }
	}
}