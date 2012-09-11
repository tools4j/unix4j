package org.unix4j.unix.cat;

import org.unix4j.command.Command;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;

class SqueezeEmptyLinesProcessor extends AbstractLineProcessor<CatArguments> {
	
	private boolean wasEmpty = false;

	public SqueezeEmptyLinesProcessor(Command<CatArguments> command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	@Override
	public boolean processLine(Line line) {
		if (line.getContentLength() > 0) {
			if (wasEmpty) {
				wasEmpty = false;
			}
			return getOutput().processLine(line);
		}
		//empty line
		if (!wasEmpty) {
			//print first empty line
			wasEmpty = true;
			return getOutput().processLine(line);
		}
		//suppress repeated empty liens
		return true;//we still want the next line
	}

	@Override
	public void finish() {
		getOutput().finish();
	}
}