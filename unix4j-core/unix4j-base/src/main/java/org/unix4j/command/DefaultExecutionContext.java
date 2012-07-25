package org.unix4j.command;

import org.unix4j.util.TypedMap;

public class DefaultExecutionContext implements ExecutionContext {
	
	private final boolean initial;
	private final boolean terminal;
	private final TypedMap globalStorage;
	private final TypedMap commandStorage;
	
	private DefaultExecutionContext(boolean initial, boolean terminal, TypedMap globalStorage, TypedMap commandStorage) {
		this.initial = initial;
		this.terminal = terminal;
		this.globalStorage = globalStorage;
		this.commandStorage = commandStorage;
	}
	
	public static DefaultExecutionContext start(boolean terminal) {
		return new DefaultExecutionContext(true, terminal, new TypedMap(), new TypedMap());
	}
	
	public static ExecutionContext deriveNextForSameCommand(ExecutionContext context, boolean terminal) {
		if (terminal) {
			throw new IllegalStateException("cannot derive next context for same command from terminal context");
		}
		if (!context.isInitialInvocation() && !terminal) {
			return context;
		}
		return new DefaultExecutionContext(false, terminal, context.getGlobalStorage(), context.getCommandStorage());
	}
	public static DefaultExecutionContext deriveForNextCommand(ExecutionContext context, boolean terminal) {
		return new DefaultExecutionContext(true, terminal, context.getGlobalStorage(), new TypedMap());
	}
	
	@Override
	public boolean isInitialInvocation() {
		return initial;
	}

	@Override
	public boolean isTerminalInvocation() {
		return terminal;
	}

	@Override
	public TypedMap getGlobalStorage() {
		return globalStorage;
	}

	@Override
	public TypedMap getCommandStorage() {
		return commandStorage;
	}

}
