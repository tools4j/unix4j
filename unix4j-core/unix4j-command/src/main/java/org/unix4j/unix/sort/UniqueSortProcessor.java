package org.unix4j.unix.sort;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

class UniqueSortProcessor extends AbstractSortProcessor {
	
	private final NavigableSet<Line> uniqueLines;

	public UniqueSortProcessor(SortCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		this.uniqueLines = new TreeSet<Line>(getComparator());
	}

	@Override
	public boolean processLine(Line line) {
		uniqueLines.add(line);//duplicate lines are not even added
		return true;//we want all lines
	}

	@Override
	public void finish() {
		final LineProcessor output = getOutput();
		final Iterator<Line> it = uniqueLines.iterator();
		while (it.hasNext()) {
			final Line line = it.next();
			if (!output.processLine(line)) {
				break;//they want no more lines
			}
			it.remove();//remove to free some memory
		}
		uniqueLines.clear();
		output.finish();
	}

}
