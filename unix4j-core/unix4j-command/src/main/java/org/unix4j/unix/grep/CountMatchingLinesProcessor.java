package org.unix4j.unix.grep;

import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.DefaultInputProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

/**
 * Counts the matching lines and writes the count and the file name to the
 * output. The matching operation is delegated to the {@link LineMatcher} passed
 * to the constructor.
 */
final class CountMatchingLinesProcessor extends DefaultInputProcessor implements LineProcessor {
	
	private final LineMatcher matcher;
	private final Counter counter = new Counter();
	private final LineProcessor output;

	public CountMatchingLinesProcessor(GrepCommand command, ExecutionContext context, LineProcessor output, LineMatcher matcher) {
		this.matcher = matcher;
		this.output = output;
	}

	@Override
	public void begin(Input input, LineProcessor output) {
		counter.reset();
	}
	
	@Override
	public boolean processLine(Line line) {
		if (matcher.matches(line)) {
			counter.increment();
		}
		return true;// we want to count all the lines
	}
	@Override
	public boolean processLine(Input input, Line line, LineProcessor output) {
		return processLine(line);
	}

	@Override
	public void finish(Input input, LineProcessor output) {
		final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo() : input.toString();
		output.processLine(new SimpleLine(counter.getCount() + ": " + fileInfo));
	}
	
	@Override
	public void finish() {
		output.processLine(new SimpleLine(String.valueOf(counter.getCount())));
	}
}