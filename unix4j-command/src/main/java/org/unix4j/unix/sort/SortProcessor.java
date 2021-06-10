package org.unix4j.unix.sort;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Line processor for normal in-memory sort using an {@link ArrayList} to cache
 * and sort the lines.
 */
class SortProcessor extends AbstractSortProcessor {
	
	private final ArrayList<Line> lineBuffer = new ArrayList<Line>();

	public SortProcessor(SortCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		lineBuffer.add(line);
		return true;//we want all the lines
	}

	@Override
	public void finish() {
		final LineProcessor output = getOutput();
		Collections.sort(lineBuffer, getComparator());
		final int size = lineBuffer.size();
		for (int i = 0; i < size; i++) {
			final Line line = lineBuffer.set(i, null);//clear the line in the buffer
			if (!output.processLine(line)) {
				break;//they want no more lines
			}
		}
		lineBuffer.clear();
		output.finish();
	}

}
