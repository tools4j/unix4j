package org.unix4j.unix.tail;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;

public class TailCharsFromStartProcessor extends AbstractTailProcessor {

	private final Counter counter = new Counter();

	public TailCharsFromStartProcessor(TailCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		final long before = counter.getCount();
		if (before >= count) {
			return getOutput().processLine(line);
		} else {
			final long after = counter.increment(line.length());
			if (after >= count) {
				final int lineLen = line.length();
				final int charsFromEnd = (int)(after - count + 1);
				final Line cutLine = SimpleLine.subLine(line, lineLen - charsFromEnd, lineLen, false);
				return getOutput().processLine(cutLine);
			} else {
				return true;//we want more lines
			}
		}
	}

	@Override
	public void finish() {
		counter.reset();
		getOutput().finish();
	}
}
