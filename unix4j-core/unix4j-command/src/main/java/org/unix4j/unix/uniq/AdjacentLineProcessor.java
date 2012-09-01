package org.unix4j.unix.uniq;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;

/**
 * Line processors implementing the actual uniq command execution for the case
 * when the {@link UniqOption#global global} option is NOT selected. The actual
 * processors are member classes of this abstract base class.
 */
abstract class AdjacentLineProcessor extends AbstractLineProcessor {
	protected Line curLine = null;

	public AdjacentLineProcessor(UniqArguments args, ExecutionContext context, LineProcessor output) {
		super(args, context, output);
	}

	/**
	 * Line processor implementing the actual uniq command execution for the
	 * case when no option is selected.
	 */
	public static class Normal extends AdjacentLineProcessor {
		public Normal(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}

		@Override
		public boolean processLine(Line line) {
			if (curLine == null || !curLine.equals(line)) {
				output.processLine(line);
				curLine = line;
			}
			return true;// we want all input
		}

		@Override
		public void finish() {
			curLine = null;
			output.finish();
		}
	}

	/**
	 * Abstract base class for member classes {@link UniqueOnly},
	 * {@link DuplicateOnly} and {@link Count}
	 */
	abstract protected static class UniqueDuplicateCount extends AdjacentLineProcessor {
		private long curCount = 0;

		public UniqueDuplicateCount(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}

		@Override
		public boolean processLine(Line line) {
			if (curLine == null || !curLine.equals(line)) {
				writeLine(curLine, curCount);
				curCount = 1;
				curLine = line;
			} else {
				curCount++;
			}
			return true;// we want all input
		}

		@Override
		public void finish() {
			writeLine(curLine, curCount);
			curCount = 0;
			curLine = null;
			output.finish();
		}

		abstract protected void writeLine(Line line, long count);
	}

	/**
	 * Line processor implementing the actual uniq command execution for the
	 * case when only the {@link UniqOption#uniqueOnly uniqueOnly} option is
	 * selected.
	 */
	public static class UniqueOnly extends UniqueDuplicateCount {
		public UniqueOnly(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}

		@Override
		protected void writeLine(Line line, long count) {
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
		public DuplicateOnly(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}

		@Override
		protected void writeLine(Line line, long count) {
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
		public Count(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}

		@Override
		protected void writeLine(Line line, long count) {
			if (count > 0) {
				writeCountLine(line, count, 3);
			}
		}
	}

}