package org.unix4j.unix.tail;

import java.util.LinkedList;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

public class TailCharsFromEndProcessor extends AbstractTailProcessor {
	
	private final Counter counter = new Counter();
	private LinkedList<Line> tailLines = new LinkedList<Line>();

	public TailCharsFromEndProcessor(TailCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		tailLines.add(line);
		if (counter.increment(line.length()) > count) {
			long firstLen = tailLines.getFirst().length();
			while (counter.getCount() - firstLen >= count) {
				tailLines.removeFirst();
				counter.decrement(firstLen);
				firstLen = tailLines.isEmpty() ? counter.getCount() : tailLines.getFirst().length();
			}
		}
		return true;//we want all lines
	}

	@Override
	public void finish() {
		final LineProcessor output = getOutput();
		boolean more = true;
		if (counter.getCount() > count) {
			final Line line = tailLines.removeFirst();
			final int offset = (int)(counter.getCount() - count);
			final Line cutLine = SimpleLine.subLine(line, offset, line.length(), false);
			more = output.processLine(cutLine);
		}
		while (!tailLines.isEmpty() && more) {
			more = output.processLine(tailLines.removeFirst());//remove lines to free space for GC 
		}
		tailLines = null;//free for GC
		output.finish();
	}

}
