package org.unix4j.unix.wc;

import java.util.EnumMap;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;
import org.unix4j.util.StringUtil;

public class Counters {
	
	private final static int MIN_COUNT_PADDING = 2;

	private final EnumMap<CounterType, Counter> counters = new EnumMap<CounterType, Counter>(CounterType.class);
	private boolean lastLineWasEmpty;
	
	public Counters(WcArguments args) {
		for (final CounterType type: CounterType.values()) {
			if (type.isOptionSet(args)) {
				counters.put(type, new Counter());
			}
		}
		if (counters.isEmpty()) {
			//no option is set, count everything
			for (final CounterType type: CounterType.values()) {
				counters.put(type, new Counter());
			}
		}
	}
	
	public void update(Line line) {
		for (final CounterType type : counters.keySet()) {
			final Counter counter = counters.get(type);
			if (counter != null) {
				counter.increment(type.count(line));
			}
		}
		lastLineWasEmpty = line.getContentLength() == 0;
	}
	
	public void updateTotal(Counters counters) {
		for (final CounterType type : this.counters.keySet()) {
			final Counter total = this.counters.get(type);
			final Counter update = counters.counters.get(type);
			if (total != null && update != null) {
				total.increment(update.getCount());
			}
		}
	}

	public Counter getCounter(CounterType type) {
		return counters.get(type);
	}
	
	public boolean isLastLineEmpty() {
		return lastLineWasEmpty;
	}
	
	private int getWidestCount() {
		int max = 0;
		for (final Counter counter : counters.values()) {
			max = Math.max(0, String.valueOf(counter.getCount()).length());
		}
		return max;
	}
	
	public void writeCountsLine(LineProcessor output) {
		final CharSequence countString;
		if (counters.size() > 1) {
			final StringBuilder sb = new StringBuilder();
			final int widestCount = getWidestCount();
			final int countDigits = widestCount + MIN_COUNT_PADDING;
			for (final Counter counter : counters.values()) {
				final String formattedCount = StringUtil.fixSizeString(countDigits, false, ' ', counter.getCount());
				sb.append(formattedCount);
			}
			countString = sb;
		} else {
			final Counter counter = counters.values().iterator().next();
			countString = String.valueOf(counter.getCount());
		}
		output.processLine(new SimpleLine(countString));
	}

}