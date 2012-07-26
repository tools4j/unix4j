package org.unix4j.command;

import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.TypedMap;

/**
 * The {@code ExecutionContext} is passed to the {@link Command}'s
 * {@link Command#execute(Input, Output) execute(..)} method providing context
 * information for the current execution of the command or command chain. The
 * same context object instance is passed to multiple {@code execute(..)} calls
 * if they belong to the same command execution.
 * 
 * @param <L>
 *            the parameter defining the type of the command specific local
 *            variable accessible throughout the multiple invocations of
 *            {@link Command#execute(ExecutionContext, Input, Output)
 *            Command.execute(..)}
 */
public interface ExecutionContext<L> {
	/**
	 * Returns true if this is the first call of the {@link Command}'s
	 * {@link Command#execute(Input, Output) execute(..)} method. There is one
	 * and only one initial invocation of {@code execute(..)} during the life of
	 * an {@code ExecutionContext}; all subsequent invocations (if any) will
	 * return false if they belong to the same command execution.
	 * <p>
	 * Note that an invocation can be {@code initial} and {@link #isTerminal()
	 * terminal} at the same time if the whole command execution consists of a
	 * single {@code execute(..)} method call.
	 * 
	 * @return true if this the first invocation of the
	 *         {@link Command#execute(Input, Output) execute(..)} method in the
	 *         context of one command execution, and false otherwise
	 */
	boolean isInitial();

	/**
	 * Returns true if this is the last invocation of the {@link Command}'s
	 * {@link Command#execute(Input, Output) execute(..)} method during command
	 * execution. Every command execution has at most one terminal invocation of
	 * {@code execute(..)}; all previous invocations (if any) must return false
	 * if they belong to the same command execution. A command can request early
	 * termination of the command execution by returning false from the
	 * {@link Command#execute(Input, Output) execute(..)} method; in this case,
	 * no terminal invocation exists.
	 * <p>
	 * Note that an invocation can be {@link #isInitial() initial} and
	 * {@code terminal} at the same time if the whole command execution consists
	 * of a single {@code execute(..)} method call.
	 * 
	 * @return true if this the terminal invocation of the
	 *         {@link Command#execute(Input, Output) execute(..)} method in the
	 *         context of one command execution, and false otherwise
	 */
	boolean isTerminal();

	/**
	 * Returns a typed map that can be used as storage for intermediary results
	 * accessible by all commands. Values stored in the map are persisting
	 * multiple calls of the {@link Command}'s
	 * {@link Command#execute(Input, Output) execute(..)} method as long as they
	 * belong to the same command execution.
	 * 
	 * @return a typed storage map for values accessible by all commands
	 */
	TypedMap getStorage();

	/**
	 * Returns the context local variable of the {@link Command}. This variable
	 * is initialized through {@link Command#initializeLocal()} when this
	 * context object is created and before the first invocation of
	 * {@link Command#execute(ExecutionContext, Input, Output)
	 * Command.execute(..)}. The variable is preserved during the command
	 * execution possibly consisting of multiple {@code execute(..)}
	 * invocations.
	 * 
	 * @return the execution context local variable of the command
	 */
	L getLocal();
}
