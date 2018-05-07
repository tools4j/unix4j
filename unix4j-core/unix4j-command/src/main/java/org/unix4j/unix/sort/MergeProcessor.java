package org.unix4j.unix.sort;

import java.util.Comparator;
import java.util.List;

import org.unix4j.context.ExecutionContext;
import org.unix4j.io.Input;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

class MergeProcessor extends AbstractSortProcessor {

	private final List<? extends Input> inputs;
	
	public MergeProcessor(SortCommand command, ExecutionContext context, LineProcessor output, List<? extends Input> inputs) {
		super(command, context, output);
		this.inputs = inputs;
	}

	@Override
	public boolean processLine(Line line) {
		//if lines come from standard input, there is nothing to merge
		return getOutput().processLine(line);
	}

	@Override
	public void finish() {
		final int len = inputs.size();
		final Line[] lines = new Line[len];
		for (int i = 0; i < len; i++) {
			final Input input = inputs.get(i); 
			lines[i] = input.hasMoreLines() ? input.readLine() : null;
		}
		final LineProcessor output = getOutput();
		final Comparator<? super Line> comparator = getComparator();
		while (true) {
			Line line = null;
			int inputIndexOfLine = -1;
			for (int i = 0; i < len; i++) {
				final Line cur = lines[i];
				if (cur != null) {
					if (inputIndexOfLine < 0 || 0 < comparator.compare(line, cur)) {
						line = cur;
						inputIndexOfLine = i;
					}
				}
			}
			if (line != null) {
				//write the winner line
				if (!output.processLine(line)) {
					output.finish();
					closeInputs();
					return;
				}
				//move to next line for winner input
				final Input input = inputs.get(inputIndexOfLine); 
				lines[inputIndexOfLine] = input.hasMoreLines() ? input.readLine() : null;
			} else {
				//no more lines
				output.finish();
				closeInputs();
				return;
			}
		}
	}

	private void closeInputs() {
		for (final Input input : inputs) {
			input.close();
		}
	}

}
