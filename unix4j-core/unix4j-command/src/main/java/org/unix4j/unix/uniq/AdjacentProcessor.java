package org.unix4j.unix.uniq;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

/**
 * Line processors implementing the actual uniq command execution for the case
 * when the {@link UniqOption#global global} option is NOT selected. The actual
 * processors are member classes of this abstract base class.
 */
abstract class AdjacentProcessor extends UniqProcessor {
	protected Line curLine = null;

	public AdjacentProcessor(UniqCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}

	/**
	 * Line processor implementing the actual uniq command execution for the
	 * case when no option is selected.
	 */
	public static class Normal extends AdjacentProcessor {
		public Normal(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}

		@Override
		protected boolean processLine(Line line, LineProcessor output) {
			if (curLine == null || !curLine.equals(line)) {
				output.processLine(line);
				curLine = line;
			}
			return true;// we want all input
		}

		@Override
		public void finish() {
			curLine = null;
			getOutput().finish();
		}
	}

	/**
	 * Abstract base class for member classes {@link UniqueOnly},
	 * {@link DuplicateOnly} and {@link Count}
	 */
	abstract protected static class UniqueDuplicateCount extends AdjacentProcessor {
		private long curCount = 0;

		public UniqueDuplicateCount(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}

		@Override
		protected boolean processLine(Line line, LineProcessor output) {
			if (curLine == null || !curLine.equals(line)) {
				writeLine(curLine, curCount, output);
				curCount = 1;
				curLine = line;
			} else {
				curCount++;
			}
			return true;// we want all input
		}

		@Override
		public void finish() {
			final LineProcessor output = getOutput();
			writeLine(curLine, curCount, output);
			curCount = 0;
			curLine = null;
			output.finish();
		}

		abstract protected void writeLine(Line line, long count, LineProcessor output);
	}

	/**
	 * Line processor implementing the actual uniq command execution for the
	 * case when only the {@link UniqOption#uniqueOnly uniqueOnly} option is
	 * selected.
	 */
	public static class UniqueOnly extends UniqueDuplicateCount {
		public UniqueOnly(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}

		@Override
		protected void writeLine(Line line, long count, LineProcessor output) {
			if (count == 1) {
				output.processLine(line);
			}
		}
	}

	/**
	 * Line processor implementing the actual uniq command execution for the
	 * case when only the {@link UniqOption#duplicatedOnly duplicatedOnly}
	 * option is selected.
	 */
	public static class DuplicateOnly extends UniqueDuplicateCount {
		public DuplicateOnly(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}

		@Override
		protected void writeLine(Line line, long count, LineProcessor output) {
			if (count > 1) {
				output.processLine(line);
			}
		}
	}

	/**
	 * Line processor implementing the actual uniq command execution for the
	 * case when only the {@link UniqOption#count count} option is selected.
	 */
	public static class Count extends UniqueDuplicateCount {
		public Count(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}

		@Override
		protected void writeLine(Line line, long count, LineProcessor output) {
			if (count > 0) {
				CountUtil.writeCountLine(line, count, 3, output);
			}
		}
	}

}