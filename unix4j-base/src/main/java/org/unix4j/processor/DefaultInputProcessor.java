package org.unix4j.processor;

import org.unix4j.io.Input;
import org.unix4j.line.Line;

/**
 * The {@code DefaultInputProcessor} simply writes every line passed to
 * {@link #processLine(Input, Line, LineProcessor)} to the output. Subclasses
 * often override some of the methods to enhance or modify this default 
 * behavior.
 */
public class DefaultInputProcessor implements InputProcessor {

	@Override
	public void begin(Input input, LineProcessor output) {
		// default: no op
	}

	@Override
	public boolean processLine(Input input, Line line, LineProcessor output) {
		output.processLine(line);
		return true;// we want all lines for this default implementation
	}

	@Override
	public void finish(Input input, LineProcessor output) {
		output.finish();
	}

}
