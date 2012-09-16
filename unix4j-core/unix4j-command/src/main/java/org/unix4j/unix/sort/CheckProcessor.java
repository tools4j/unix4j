package org.unix4j.unix.sort;

import org.unix4j.command.ExecutionContext;
import org.unix4j.command.ExitValueException;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

/**
 * Checks whether a file is sorted or not, throws an {@link ExitValueException}
 * if the file is not sorted.
 */
class CheckProcessor extends AbstractSortProcessor {

	private Line lastLine = null;
	
	public CheckProcessor(SortCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		if (lastLine != null) {
			if (getComparator().compare(lastLine, line) > 0) {
				throw new ExitValueException("file is not sorted", 1);
			}
		}
		lastLine = line;
		return true;//we want all lines
	}

	@Override
	public void finish() {
		getOutput().finish();
	}

}
