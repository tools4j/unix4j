package org.unix4j.unix.wc;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Wc;
import org.unix4j.util.Counter;

/**
 * User: ben
 */
public class WcCommand extends AbstractCommand<WcArguments> {
	private final static int MIN_COUNT_PADDING = 2;

	public WcCommand(WcArguments arguments) {
		super(Wc.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
//		assertArgFalse("No count type specified. At least one count type required.", getArguments().getOptions().size() == 0);
		return new LineProcessor() {
			private final boolean noOptionSet = !getArguments().isLines() && !getArguments().isWords() && !getArguments().isChars();
			private final Counter lineCounter = new Counter();
			private final Counter wordCounter = noOptionSet || getArguments().isWords() ? new Counter() : null;
			private final Counter charCounter = new Counter();
			@Override
			public boolean processLine(Line line) {
				lineCounter.increment();
				if (wordCounter != null) {
					wordCounter.increment(wordCount(line));
				}
				charCounter.increment(line.length());
				return true;//we want more lines
			}

			@Override
			public void finish() {
				if (lineCounter.getCount() == 1 && charCounter.getCount() == 0) {
					lineCounter.reset();
				}

				final WcArguments args = getArguments();
				final List<Long> counts = new ArrayList<Long>();
				if (noOptionSet || args.isLines())
					counts.add(lineCounter.getCount());
				if (noOptionSet || args.isWords())
					counts.add(wordCounter.getCount());
				if (noOptionSet || args.isChars())
					counts.add(charCounter.getCount());

				output.processLine(formatCounts(counts));
				output.finish();
			}
		};
	}

	private int wordCount(Line line) {
		final String[] words = line.getContent().split("\\s+");
		int wordCount = 0;
		for (final String word : words) {
			if (word.length() > 0) {
				wordCount += 1;
			}
		}
		return wordCount;
	}

	private Line formatCounts(List<Long> counts) {
		final StringBuilder format = new StringBuilder();

		if (counts.size() > 1) {
			int widestCount = getWidestCount(counts);
			int fixedWidth = widestCount + MIN_COUNT_PADDING;
			for (int i = 0; i < counts.size(); i++) {
				format.append("%").append(fixedWidth).append("d");
			}
		} else {
			format.append("%d");
		}

		final Formatter formatter = new Formatter().format(format.toString(), counts.toArray());
		return new SimpleLine(formatter.toString());
	}

	private int getWidestCount(List<Long> counts) {
		int widestCount = 0;
		for (final long count : counts) {
			final int width = String.valueOf(count).length();
			if (width > widestCount) {
				widestCount = width;
			}
		}
		return widestCount;
	}
}
