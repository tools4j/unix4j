package org.unix4j.unix.sort;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;

class MergeProcessor extends AbstractLineProcessor<SortArguments> {

	public MergeProcessor(SortCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		throw new RuntimeException("not impelemented");
	}

	@Override
	public boolean processLine(Line line) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

}
