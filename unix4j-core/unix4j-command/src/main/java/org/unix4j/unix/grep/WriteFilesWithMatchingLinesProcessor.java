package org.unix4j.unix.grep;

import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.DefaultInputProcessor;
import org.unix4j.processor.LineProcessor;

/**
 * Counts the matching lines and writes the count and the file name to the
 * output. The matching operation is delegated to the {@link LineMatcher} passed
 * to the constructor.
 */
final class WriteFilesWithMatchingLinesProcessor extends DefaultInputProcessor implements LineProcessor {
	
	private final LineMatcher matcher;
	private boolean matches = false;
	private final LineProcessor output;

	public WriteFilesWithMatchingLinesProcessor(GrepCommand command, ExecutionContext context, LineProcessor output, LineMatcher matcher) {
		this.matcher = matcher;
		this.output = output;
	}

	@Override
	public void begin(Input input, LineProcessor output) {
		matches = false;
	}
	
	@Override
	public boolean processLine(Line line) {
		if (matcher.matches(line)) {
			matches = true;
			return false;//the first match is good enough
		}
		return true;// we want more lines, maybe another one matches
	}
	@Override
	public boolean processLine(Input input, Line line, LineProcessor output) {
		return processLine(line);
	}

	@Override
	public void finish(Input input, LineProcessor output) {
		if (matches) {
			final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo() : input.toString();
			output.processLine(new SimpleLine(fileInfo));
		}
	}
	
	@Override
	public void finish() {
		if (matches) {
			output.processLine(new SimpleLine("(standard input)"));
		}
	}
}