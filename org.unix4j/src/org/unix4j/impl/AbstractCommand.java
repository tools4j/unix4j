package org.unix4j.impl;

import org.unix4j.Command;
import org.unix4j.CommandInterface;

abstract public class AbstractCommand<I extends CommandInterface<? extends Command<I, A>>, A> implements Command<I, A> {
	private final String name;
	private final boolean batchable;
	private final boolean joinsNext;
	private final I iface;
	private final A arguments;
	public AbstractCommand(String name, boolean batchable, boolean joinsNext, I iface, A arguments) {
		this.name = name;
		this.batchable = batchable;
		this.joinsNext = joinsNext;
		this.iface = iface;;
		this.arguments = arguments;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public boolean isBatchable() {
		return batchable;
	}
	@Override
	public boolean joinsNext() {
		return joinsNext;
	}
	@Override
	public I getInterface() {
		return iface;
	}
	public A getArguments() {
		return arguments;
	}
	@Override
	public String toString() {
		final String args = getArguments().toString();
		return args.isEmpty() ? getName() : getName() + " " + args;
	}
}
