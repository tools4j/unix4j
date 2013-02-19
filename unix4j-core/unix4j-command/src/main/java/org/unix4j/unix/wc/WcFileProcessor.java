package org.unix4j.unix.wc;

import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.processor.DefaultInputProcessor;
import org.unix4j.processor.LineProcessor;

/**
 * Input processor for line, word and char count for a single file.
 */
class WcFileProcessor extends DefaultInputProcessor {
	private final Counters current;

	public WcFileProcessor(WcArguments args) {
		current = new Counters(args);
	}

	@Override
	public boolean processLine(Input input, Line line, LineProcessor output) {
		current.update(line);
		return true;//we want to count all lines
	}

	@Override
	public void finish(Input input, LineProcessor output) {
		final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo() : input.toString();
		current.writeCountsLineWithFileInfo(output, fileInfo);
		current.reset();
	}
}
