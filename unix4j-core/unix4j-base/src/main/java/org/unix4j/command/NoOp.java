package org.unix4j.command;

import org.unix4j.builder.CommandBuilder;
import org.unix4j.io.NullOutput;
import org.unix4j.processor.LineProcessor;
import org.unix4j.variable.VariableContext;

/**
 * A command that performs no operation. Useful as initial command in a
 * {@link CommandBuilder}. The {@link #join(Command)} method returns the joined
 * command only, that is, this {@code nop} command is eliminated.
 */
public class NoOp extends AbstractCommand<NoOp.Args> {

	/**
	 * Arguments for NoOp.
	 */
	public static final class Args implements Arguments<Args> {
		@Override
		public Args getForContext(VariableContext context) {
			return this;//no arguments, hence the same for all contexts
		}
	}

	/**
	 * The "nop" command name. 
	 */
	public static final String NAME = "nop";
	/**
	 * The singleton instance.
	 */
	public static final NoOp INSTANCE = new NoOp();
	
	/**
	 * Private constructor for singleton {@link #INSTANCE}.
	 */
	private NoOp() {
		super(NAME, new Args());
	}

	/**
	 * Returns the given {@code next} command eliminating this {@code NoOp} 
	 * command in a join.
	 * 
	 * @return the {@code next} command argument
	 */
	@Override
	public Command<?> join(Command<?> next) {
		return next;
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		return NullOutput.ABORT;
	}
}
