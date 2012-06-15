package org.unix4j.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.unix4j.CommandInterface;
import org.unix4j.Input;
import org.unix4j.Output;
import org.unix4j.util.TypedMap;

public final class Echo {
	public static final String NAME = "echo";
	public static interface Interface<R> extends CommandInterface<R> { 
		R echo(String message);
		R echo(String... messages);
	}
	public static enum Option {
		//no options?
	}
	
	public static class Args extends AbstractArgs<Option> {
		public static final TypedMap.Key<List<String>> MESSAGES = TypedMap.DefaultKey.keyForListOf("messages", String.class);
		public Args(String message) {
			this(Collections.singletonList(message));
		}
		public Args(String... messages) {
			this(Arrays.asList(messages));
		}
		public Args(List<String> messages) {
			super(Option.class);
			setArg(MESSAGES, messages);
		}
		public List<String> getMessages() {
			return getArg(MESSAGES);
		}
	}
	
	public static final Interface<Command> FACTORY = new Interface<Command>() {
		@Override
		public Command echo(String message) {
			return new Command(new Args(message));
		}

		@Override
		public Command echo(String... messages) {
			return new Command(new Args(messages));
		}
	};
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.NoInput, arguments);
		}
		@Override
		public void executeBatch(Input input, Output output) {
			final List<String> messages = getArguments().getMessages();
			final StringBuilder sb = new StringBuilder();
			if (!messages.isEmpty()) {
				sb.append(messages.get(0));
				for (int i = 1; i < messages.size(); i++) {
					sb.append(' ').append(messages.get(i));
				}
			}
			output.writeLine(sb.toString());
		}
	}

	// no instances
	private Echo() {
		super();
	}
}
