package org.unix4j.unix.cat;

import org.unix4j.command.Command;
import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;

class NumberLinesProcessor extends AbstractLineProcessor<CatArguments> {
	
	private final StringBuilder spaces = new StringBuilder("     ");//5 digits for numbers by default 
	private final boolean numberBlankLines;
	private long count = 0;

	public NumberLinesProcessor(Command<CatArguments> command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
		this.numberBlankLines = getArguments().isNumberLines();
	}

	@Override
	public boolean processLine(Line line) {
		if (numberBlankLines || line.getContentLength() > 0) {
			count++;
			int digitsForCount = spaces.length();
			final StringBuilder countLine = new StringBuilder(line.getContentLength() + digitsForCount + 2);
			countLine.append(count);
			if (countLine.length() > digitsForCount) {
				//append more spaces for next numbers
				while (countLine.length() > spaces.length()) {
					spaces.append(' ');
				}
			} else {
				countLine.insert(0, spaces, 0, digitsForCount - countLine.length());
			}
			//now the line itself
			countLine.append("  ").append(line, 0, line.getContentLength());
			final Line numberedLine = new SimpleLine(countLine, line.getLineEnding());
			return getOutput().processLine(numberedLine);
		}
		//unnumbered blank line
		return getOutput().processLine(line);
	}

	@Override
	public void finish() {
		getOutput().finish();
	}
}