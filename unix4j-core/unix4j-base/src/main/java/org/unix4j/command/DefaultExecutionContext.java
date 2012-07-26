package org.unix4j.command;

import org.unix4j.util.TypedMap;

public class DefaultExecutionContext<L> implements ExecutionContext<L> {
	
	private final boolean initial;
	private final boolean terminal;
	private final TypedMap globalStorage;
	private final L commandLocal;
	
	private DefaultExecutionContext(boolean initial, boolean terminal, TypedMap globalStorage, L commandLocal) {
		this.initial = initial;
		this.terminal = terminal;
		this.globalStorage = globalStorage;
		this.commandLocal = commandLocal;
	}
	
	public static <L> DefaultExecutionContext<L> start(Command<?, L> command, boolean terminal) {
		return new DefaultExecutionContext<L>(true, terminal, new TypedMap(), command.initializeLocal());
	}
	
	public static <L> ExecutionContext<L> deriveNextForSameCommand(ExecutionContext<L> context, boolean terminal) {
		if (terminal) {
			throw new IllegalStateException("cannot derive next context for same command from terminal context");
		}
		if (!context.isInitial() && !terminal) {
			return context;
		}
		return new DefaultExecutionContext<L>(false, terminal, context.getStorage(), context.getLocal());
	}
	
	public static <L> DefaultExecutionContext<L> deriveForNextCommand(ExecutionContext<?> context, Command<?, L> command, boolean terminal) {
		return new DefaultExecutionContext<L>(true, terminal, context.getStorage(), command.initializeLocal());
	}
	
	@Override
	public boolean isInitial() {
		return initial;
	}

	@Override
	public boolean isTerminal() {
		return terminal;
	}

	@Override
	public TypedMap getStorage() {
		return globalStorage;
	}

	@Override
	public L getLocal() {
		return commandLocal;
	}

}
