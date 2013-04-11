package org.unix4j.operation;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.Arguments;
import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.operation.LineOperationCommand.Args;
import org.unix4j.processor.LineProcessor;

/**
 * A command based on a {@link LineOperation}.
 */
public class LineOperationCommand extends AbstractCommand<Args> {

	/**
	 * The "lineop" name for this command.
	 */
	public static final String NAME = "lineop";

	public LineOperationCommand(LineOperation operation) {
		super(NAME, new Args(operation));
	}

	/**
	 * Arguments for LineOperationCommand.
	 */
	public static final class Args implements Arguments<Args> {
		private final LineOperation operation;

		public Args(LineOperation operation) {
			if (operation == null) {
				throw new NullPointerException("operation cannot be null");
			}
			this.operation = operation;
		}

		public final LineOperation getOperation() {
			return operation;
		}

		@Override
		public Args getForContext(ExecutionContext context) {
			return this;// no variable args, hence the same for all contexts
		}

		@Override
		public String toString() {
			return "--operation " + operation;
		}
	}

	@Override
	public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
		return new LineProcessor() {
			final OperationOutput operationOutput = new OperationOutput(output);
			final LineOperation operation = getArguments(context).getOperation();

			@Override
			public boolean processLine(Line line) {
				operation.operate(context, line, operationOutput);
				return operationOutput.isOpen();
			}

			@Override
			public void finish() {
				operationOutput.close();
			}
		};
	}

	private static class OperationOutput implements LineProcessor {
		private final LineProcessor output;
		private boolean open = true;

		public OperationOutput(LineProcessor output) {
			this.output = output;
		}

		@Override
		public boolean processLine(Line line) {
			if (open) {
				open = output.processLine(line);
				return open;
			}
			return false;
		}

		@Override
		public void finish() {
			open = false;
		}

		public boolean isOpen() {
			return open;
		}

		public void close() {
			finish();
			output.finish();
		}
	}

}
