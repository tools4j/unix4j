package org.unix4j.unix.uniq;

import java.util.LinkedHashMap;
import java.util.Map;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.LineProcessor;

/**
 * Line processors implementing the actual uniq command execution for the case
 * when the {@link UniqOption#global global} option is selected. The actual
 * processors are member classes of this abstract base class.
 */
abstract class GlobalProcessor extends UniqProcessor {
	protected final Map<Line, Long> lineToCount = new LinkedHashMap<Line, Long>();
	public GlobalProcessor(UniqCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, output);
	}
	
	/**
	 * Line processor implementing the actual uniq command execution for the 
	 * case when only the {@link UniqOption#global global} option is selected.
	 */
	public static class Normal extends GlobalProcessor {
		public Normal(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}
		@Override
		protected boolean processLine(Line line, LineProcessor output) {
			lineToCount.put(line, null);//we don't really need the count
			return true;// we want all input
		}
		@Override
		public void finish() {
			final LineProcessor output = getOutput();
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
	abstract protected static class UniqueDuplicateCount extends GlobalProcessor {
		private static final Long ONE = Long.valueOf(1);
		private long maxCount = 0;
		public UniqueDuplicateCount(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}
		@Override
		protected boolean processLine(Line line, LineProcessor output) {
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
			final LineProcessor output = getOutput();
			final int maxDigits = String.valueOf(maxCount).length();
			for (final Map.Entry<Line, Long> e : lineToCount.entrySet()) {
				writeLine(e.getKey(), e.getValue(), maxDigits, output);
			}
			lineToCount.clear();
			maxCount = 0;
			output.finish();
		}
		abstract protected void writeLine(Line line, long count, int maxCountDigits, LineProcessor output);
	}
	/**
	 * Line processor implementing the actual uniq command execution for the 
	 * case when the {@link UniqOption#global global} option is selected along
	 * with the {@link UniqOption#uniqueOnly uniqueOnly} option.
	 */
	public static class UniqueOnly extends UniqueDuplicateCount {
		public UniqueOnly(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}
		@Override
		protected void writeLine(Line line, long count, int maxCountDigits, LineProcessor output) {
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
		public DuplicateOnly(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}
		@Override
		protected void writeLine(Line line, long count, int maxCountDigits, LineProcessor output) {
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
		public Count(UniqCommand command, ExecutionContext context, LineProcessor output) {
			super(command, context, output);
		}
		@Override
		protected void writeLine(Line line, long count, int maxCountDigits, LineProcessor output) {
			CountUtil.writeCountLine(line, count, Math.max(3, maxCountDigits), output);
		}
	}
	
}