package org.unix4j.unix.wc;

import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.processor.DefaultInputProcessor;
import org.unix4j.processor.LineProcessor;

/**
 * Input processor for line, word and char count for multiple input files. The
 * totals line can be printed by {@link #writeTotalsLine(LineProcessor)}.
 */
class WcMultiFileProcessor extends DefaultInputProcessor {

	private final Counters current;
	private final Counters totals;
	
	public WcMultiFileProcessor(WcArguments args) {
		current = new Counters(args);
		totals = new Counters(args);
	}

	@Override
	public boolean processLine(Input input, Line line, LineProcessor output) {
		current.update(line);
		return true;//we want to count all lines
	}

	@Override
	public void finish(Input input, LineProcessor output) {
		final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo() : input.toString();
		totals.updateTotal(current);
		current.writeCountsLineWithFileInfo(output, fileInfo);
		current.reset();
	}
	
	public void writeTotalsLine(LineProcessor output) {
		totals.writeCountsLineWithFileInfo(output, "total");
	}

}
