package org.unix4j.unix.wc;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.util.Counter;
import org.unix4j.util.StringUtil;

import java.util.EnumMap;

/**
 * Counters for lines, words and characters with {@link Counter} objects in a
 * map by {@link CounterType}. It depends on the argument options which counters
 * are actually maintained. The {@link #update(Line)} method updates the
 * relevant counters based on a line.
 */
class Counters {

	public final static int MIN_COUNT_PADDING = 2;

	private final EnumMap<CounterType, Counter> counters = new EnumMap<CounterType, Counter>(CounterType.class);
	private boolean lastLineWasEmpty;

	/**
	 * Constructor initialising all relevant counters depending on the options
	 * set in {@code args}.
	 * 
	 * @param args
	 *            the arguments with the options indicating which counts are
	 *            desired
	 */
	public Counters(WcArguments args) {
		for (final CounterType type : CounterType.values()) {
			if (type.isOptionSet(args)) {
				counters.put(type, new Counter());
			}
		}
		if (counters.isEmpty()) {
			// no option is set, count everything
			for (final CounterType type : CounterType.values()) {
				counters.put(type, new Counter());
			}
		}
	}

	/**
	 * Updates all the relevant counters based on the specified {@code line}.
	 * 
	 * @param line
	 *            the line to incorporate into the counts
	 */
	public void update(Line line) {
		for (final CounterType type : counters.keySet()) {
			final Counter counter = counters.get(type);
			if (counter != null) {
				counter.increment(type.count(line));
			}
		}
		lastLineWasEmpty = line.getContentLength() == 0;
	}

	/**
	 * Updates the totals counter based on some other counter. Adds the counts
	 * of the specified {@code counters} to the total counts of {@code this}
	 * counter.
	 * 
	 * @param counters
	 *            the counters with per-file counts
	 */
	public void updateTotal(Counters counters) {
		for (final CounterType type : this.counters.keySet()) {
			final Counter total = this.counters.get(type);
			final Counter update = counters.counters.get(type);
			if (total != null && update != null) {
				total.increment(update.getCount());
			}
		}
	}

	/**
	 * Resets all counts to zero.
	 */
	public void reset() {
		for (final Counter counter : counters.values()) {
			counter.reset();
		}
	}

	private int getWidestCount() {
		int max = 0;
		for (final Counter counter : counters.values()) {
			max = Math.max(max, counter.getWidth());
		}
		return max;
	}

    public int getFixedWidthOfColumnsInOutput() {
        if(counters.size() == 1){
            return counters.values().iterator().next().getWidth();
        } else {
            return getWidestCount() + MIN_COUNT_PADDING;
        }
    }

	/**
	 * Writes the counts line to the specified {@code output}.
	 * 
	 * @param output
	 *            the output destination
	 */
	public void writeCountsLine(LineProcessor output) {
		writeCountsLineWithFileInfo(output, null);
	}


    /**
     * Writes the counts line to the specified {@code output} appending the
     * specified file information.
     *
     * @param output
     *            the output destination
     * @param fileInfo
     *            the file information, usually a file name or path
     */
    public void writeCountsLineWithFileInfo(LineProcessor output, String fileInfo) {
        writeCountsLineWithFileInfo(output, fileInfo, getFixedWidthOfColumnsInOutput());
    }

	/**
	 * Writes the counts line to the specified {@code output} appending the
	 * specified file information.
	 * 
	 * @param output
	 *            the output destination
	 * @param fileInfo
	 *            the file information, usually a file name or path
  	 * @param fixedWidthOfColumnsInOutput
     *        the fixed width of the outputted counts.  Will usually be the width
     *        of the widest count, plus two characters
	 */
	public void writeCountsLineWithFileInfo(LineProcessor output, String fileInfo, int fixedWidthOfColumnsInOutput) {
		dontCountSingleEmptyLine();
		final CharSequence countString;
        final StringBuilder sb = new StringBuilder();

        for (final Counter counter : counters.values()) {
            final String formattedCount = StringUtil.fixSizeString(fixedWidthOfColumnsInOutput, false, ' ', counter.getCount());
            sb.append(formattedCount);
        }
        countString = sb;

		if (fileInfo == null) {
			output.processLine(new SimpleLine(countString));
		} else {
			output.processLine(new SimpleLine(countString + " " + fileInfo));
		}
	}

	private void dontCountSingleEmptyLine() {
		if (lastLineWasEmpty) {
			final Counter lineCounter = counters.get(CounterType.Lines);
			if (lineCounter != null && lineCounter.getCount() == 1) {
				lineCounter.reset();
			}
		}
	}
}