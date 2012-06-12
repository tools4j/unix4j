package org.unix4j;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.unix4j.impl.AbstractCommand;
import org.unix4j.impl.Echo;
import org.unix4j.impl.Grep;
import org.unix4j.impl.Ls;
import org.unix4j.impl.Sort;
import org.unix4j.impl.Xargs;
import org.unix4j.io.BufferedInput;
import org.unix4j.io.BufferedOutput;

public class CommandBuilderImpl implements CommandBuilder {
	private List<Command<?, ?>> commands = new ArrayList<Command<?, ?>>();

	public CommandBuilderImpl() {
		super();
	}

	@Override
	public CommandBuilder ls() {
		commands.add(Ls.FACTORY.ls());
		return this;
	}

	@Override
	public CommandBuilder ls(File... files) {
		commands.add(Ls.FACTORY.ls(files));
		return this;
	}

	@Override
	public CommandBuilder ls(Ls.Option... options) {
		commands.add(Ls.FACTORY.ls(options));
		return this;
	}

	@Override
	public CommandBuilder ls(List<File> files, Ls.Option... options) {
		commands.add(Ls.FACTORY.ls(files, options));
		return this;
	}

	@Override
	public CommandBuilder grep(String matchString) {
		commands.add(Grep.FACTORY.grep(matchString));
		return this;
	}

	@Override
	public CommandBuilder grep(String matchString, Grep.Option... options) {
		commands.add(Grep.FACTORY.grep(matchString, options));
		return this;
	}

	@Override
	public CommandBuilder echo(String message) {
		commands.add(Echo.FACTORY.echo(message));
		return this;
	}

	@Override
	public CommandBuilder echo(String... messages) {
		commands.add(Echo.FACTORY.echo(messages));
		return this;
	}

	@Override
	public CommandBuilder sort() {
		commands.add(Sort.FACTORY.sort());
		return this;
	}

	@Override
	public CommandBuilder sort(Sort.Option... options) {
		commands.add(Sort.FACTORY.sort(options));
		return this;
	}

	@Override
	public CommandBuilder xargs() {
		commands.add(Xargs.FACTORY.xargs());
		return this;
	}

	@Override
	public Command<?, Void> build() {
		return new Cmd(new ArrayList<Command<?, ?>>(commands));
	}

	@Override
	public void execute(Input input, Output output) {
		build().execute(input, output);
	}

	private interface Interface extends CommandInterface<Cmd> {
		void execute(Input input, Output output);
	}

	private class Impl implements Interface {
		private final List<Command<?, ?>> commands;

		public Impl(List<Command<?, ?>> commands) {
			this.commands = commands;
		}

		@Override
		public void execute(final Input input, Output output) {
			if (commands.size() == 1) {
				executeLast(input, output);
			} else if (commands.size() > 1) {
				executeRecursive(0, input, output, new BufferedOutput());
			}
		}
		private void executeRecursive(int index, final Input input, final Output output, final BufferedOutput buffer) {
			if (index + 1 < commands.size()) {
				final Command<?,?> command = commands.get(index);
				do {
					command.execute(input, buffer);
					final BufferedInput nextInput = buffer.asInput();
					buffer.clear();
					if (command.joinsNext()) {
						if (index + 2 < commands.size()) {
							executeRecursive(index + 2, nextInput, output, buffer);
						}
					} else {
						executeRecursive(index + 1, nextInput, output, buffer);
					}
				} while (input.hasMoreLines() && command.isBatchable());
			} else {
				executeLast(input, output);
				output.finish();
			}
		}
		private void executeLast(final Input input, final Output output) {
			final Command<?, ?> command = commands.get(commands.size() - 1);
			if (command.joinsNext()) {
				throw new IllegalStateException("command.joinsNext() is true but " + command.getName() + " is the last command in: " + commandString(commands));
			}
			if (command.isBatchable() && input.hasMoreLines()) {
				final LinkedList<String> lines = new LinkedList<String>();
				final BufferedInput buffer = new BufferedInput(lines);
				do {
					lines.add(input.readLine());
					command.execute(buffer, output);
					lines.clear();
				} while (input.hasMoreLines());
				
			} else {
				command.execute(input, output);
			}
		}
	}

	private class Cmd extends AbstractCommand<Interface, Void> {
		public Cmd(List<Command<?, ?>> commands) {
			super(commandName(commands), commandIsBatchable(commands), commandJoinsNext(commands), new Impl(commands), null);
		}

		@Override
		public void execute(Input input, Output output) {
			getInterface().execute(input, output);
		}

		@Override
		public String toString() {
			return commandString(commands);
		}
	}

	private static String commandString(List<Command<?, ?>> commands) {
		final StringBuilder sb = new StringBuilder();
		for (Command<?, ?> command : commands) {
			sb.append(sb.length() > 0 ? " | " : "").append(command);
		}
		return sb.toString();
	}
	private static String commandName(List<Command<?, ?>> commands) {
		final StringBuilder sb = new StringBuilder();
		for (Command<?, ?> command : commands) {
			sb.append(sb.length() > 0 ? " | " : "").append(command.getName());
		}
		return sb.toString();
	}

	private static boolean commandIsBatchable(List<Command<?, ?>> commands) {
		return commands.isEmpty() ? true : commands.get(0).isBatchable();
	}
	private static boolean commandJoinsNext(List<Command<?, ?>> commands) {
		return commands.isEmpty() ? false : commands.get(0).joinsNext();
	}

}
