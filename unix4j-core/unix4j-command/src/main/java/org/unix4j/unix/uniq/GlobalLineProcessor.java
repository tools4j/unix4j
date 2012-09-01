package org.unix4j.unix.uniq;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;

/**
 * Line processors implementing the actual uniq command execution for the case
 * when the {@link UniqOption#global global} option is selected. The actual
 * processors are member classes of this abstract base class.
 */
abstract class GlobalLineProcessor extends AbstractLineProcessor {
	protected final Map<Line, Long> lineToCount = new LinkedHashMap<Line, Long>();
	public GlobalLineProcessor(UniqArguments args, ExecutionContext context, LineProcessor output) {
		super(args, context, output);
	}
	
	/**
	 * Line processor implementing the actual uniq command execution for the 
	 * case when only the {@link UniqOption#global global} option is selected.
	 */
	public static class Normal extends GlobalLineProcessor {
		public Normal(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}
		@Override
		public boolean processLine(Line line) {
			lineToCount.put(line, null);//we don't really need the count
			return true;// we want all input
		}
		@Override
		public void finish() {
			for (final Line line : lineToCount.keySet()) {
				output.processLine(line);
			}
			lineToCount.clear();
			output.finish();
		}
	}
	/**
	 * Abstract base class for member classes {@link UniqueOnly}, 
	 * {@link DuplicateOnly} and {@link Count} 
	 */
	abstract protected static class UniqueDuplicateCount extends GlobalLineProcessor {
		private static final Long ONE = Long.valueOf(1);
		private long maxCount = 0;
		public UniqueDuplicateCount(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}
		@Override
		public boolean processLine(Line line) {
			Long count = lineToCount.put(line, ONE);
			if (count != null) {
				count++;
				lineToCount.put(line, count);
				maxCount = Math.max(maxCount, count);
			} else {
				maxCount = Math.max(maxCount, 1);
			}
			return true;// we want all input
		}
		@Override
		public void finish() {
			final int maxDigits = String.valueOf(maxCount).length();
			for (final Map.Entry<Line, Long> e : lineToCount.entrySet()) {
				writeLine(e.getKey(), e.getValue(), maxDigits);
			}
			lineToCount.clear();
			maxCount = 0;
			output.finish();
		}
		abstract protected void writeLine(Line line, long count, int maxCountDigits);
	}
	/**
	 * Line processor implementing the actual uniq command execution for the 
	 * case when the {@link UniqOption#global global} option is selected along
	 * with the {@link UniqOption#uniqueOnly uniqueOnly} option.
	 */
	public static class UniqueOnly extends UniqueDuplicateCount {
		public UniqueOnly(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}
		@Override
		protected void writeLine(Line line, long count, int maxCountDigits) {
			if (count == 1) {
				output.processLine(line);
			}
		}
	}
	/**
	 * Line processor implementing the actual uniq command execution for the 
	 * case when the {@link UniqOption#global global} option is selected along
	 * with the {@link UniqOption#duplicatedOnly duplicatedOnly} option.
	 */
	public static class DuplicateOnly extends UniqueDuplicateCount {
		public DuplicateOnly(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}
		@Override
		protected void writeLine(Line line, long count, int maxCountDigits) {
			if (count > 1) {
				output.processLine(line);
			}
		}
	}
	/**
	 * Line processor implementing the actual uniq command execution for the 
	 * case when the {@link UniqOption#global global} option is selected along
	 * with the {@link UniqOption#count count} option.
	 */
	public static class Count extends UniqueDuplicateCount {
		public Count(UniqArguments args, ExecutionContext context, LineProcessor output) {
			super(args, context, output);
		}
		@Override
		protected void writeLine(Line line, long count, int maxCountDigits) {
			writeCountLine(line, count, Math.max(3, maxCountDigits));
		}
	}
	
}