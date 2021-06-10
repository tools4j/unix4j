package org.unix4j.unix.tail;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

import java.util.LinkedList;

public class TailCharsFromEndProcessor extends AbstractTailProcessor {
	
	private final Counter counter = new Counter();
	private final LinkedList<Line> tailLines = new LinkedList<Line>();

    @Override
    public void resetCountersAndFlush() {
        final LineProcessor output = getOutput();
        boolean more = true;
        if (counter.getCount() > count) {
            final Line line = tailLines.removeFirst();
            final int offset = (int)(counter.getCount() - count);
            final Line cutLine = SimpleLine.subLine(line, offset, line.length(), false);
            more = output.processLine(cutLine);
        }
        while (!tailLines.isEmpty() && more) {
            more = output.processLine(tailLines.removeFirst());//remove to free memory
        }
        counter.reset();
        tailLines.clear();
    }

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
        resetCountersAndFlush();
		output.finish();
	}
	
}
