package org.unix4j.unix.grep;

import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.InputProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

/**
 * Counts the matching lines and writes the count and the file name to the
 * output. The matching operation is delegated to the {@link LineMatcher} passed
 * to the constructor.
 */
final class WriteMatchingLinesInputProcessor implements InputProcessor {

	private final ExecutionContext context;
	private final LineMatcher matcher;
	private final Counter lineCounter = new Counter();

	public WriteMatchingLinesInputProcessor(GrepCommand command, ExecutionContext context, LineMatcher matcher) {
		this.context = context;
		this.matcher = matcher;
	}

	@Override
	public void begin(Input input, LineProcessor output) {
		lineCounter.reset();
	}
	
	@Override
	public boolean processLine(Input input, Line line, LineProcessor output) {
		lineCounter.increment();
		if (matcher.matches(line)) {
			final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo(context.getCurrentDirectory()) : input.toString();
			return output.processLine(new SimpleLine(
					fileInfo + ":" + lineCounter.getCount() + ":" + line.getContent(), line.getLineEnding()
			));
		}
		return true;//this line is not a match, but we still want the next line
	}

	@Override
	public void finish(Input input, LineProcessor output) {
		lineCounter.reset();
		output.finish();
	}
}