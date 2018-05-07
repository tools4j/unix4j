package org.unix4j.unix.wc;

import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Input processor for line, word and char count for multiple input files.
 */
class WcMultipleFilesProcessor implements LineProcessor {
	private final ExecutionContext context;
	private final NavigableMap<String, Counters> inputCounters;
	private final Counters totals;
    private final WcArguments args;
    private final List<FileInput> inputs;
    private final LineProcessor output;
    private Counters currentInputCounter;

    public WcMultipleFilesProcessor(final ExecutionContext context, final List<FileInput> inputs, final LineProcessor output, WcArguments args) {
    	this.context = context;
    	this.inputCounters = new TreeMap<String, Counters>();
    	this.currentInputCounter = new Counters(args);
    	this.totals = new Counters(args);
        this.args = args;
        this.inputs = inputs;
        this.output = output;
	}

    @Override
    public boolean processLine(Line line) {
        return false; // we want no input, we have it already
    }

    @Override
    public void finish() {
        for (final Input input: inputs) {
            for (final Line line : input) {
                currentInputCounter.update(line);
            }
            finishSingleInput(input);
        }
        writeOutput();
        closeInputs();
    }

	private void finishSingleInput(Input input) {
		final String fileInfo = input instanceof FileInput ? ((FileInput)input).getFileInfo(context.getCurrentDirectory()) : input.toString();
		totals.updateTotal(currentInputCounter);
        inputCounters.put(fileInfo, currentInputCounter);
        currentInputCounter = new Counters(args);
	}

	private void writeOutput() {
        int fixedWidthOfColumnsInOutput = totals.getFixedWidthOfColumnsInOutput();
        for(final String fileInfo: inputCounters.keySet()){
            final Counters inputCounter = inputCounters.get(fileInfo);
            inputCounter.writeCountsLineWithFileInfo(output, fileInfo, fixedWidthOfColumnsInOutput);
        }
        totals.writeCountsLineWithFileInfo(output, "total", fixedWidthOfColumnsInOutput);
	}

	private void closeInputs() {
        for (final Input input : inputs) {
            input.close();
        }
    }
}
