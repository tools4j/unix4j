package org.unix4j.unix.uniq;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.line.SimpleLine;

abstract class AbstractLineProcessor implements LineProcessor {
		protected final UniqArguments args;
//		protected final ExecutionContext context;
		protected LineProcessor output;

		public AbstractLineProcessor(UniqArguments args, ExecutionContext context, LineProcessor output) {
			this.args = args;
//			this.context = context;
			this.output = output;
		}
		
		protected int writeCountLine(Line line, long count, int maxDigitsForCount) {
			final String countString = UniqCommand.formatCount(count, maxDigitsForCount);
			final Line outputLine = new SimpleLine(" " + countString + " " + line.getContent(), line.getLineEnding());
			output.processLine(outputLine);
			return countString.length();
		}
	}