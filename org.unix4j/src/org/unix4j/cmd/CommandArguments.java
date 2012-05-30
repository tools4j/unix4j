package org.unix4j.cmd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommandArguments<O extends Enum<O>> implements Cloneable {
	private Map<O, CommandArgument<O>> arguments;

	public CommandArguments() {
		this.arguments = new HashMap<O, CommandArgument<O>>();
	}

	public void addArg(final CommandArgument<O> arg) {
		arguments.put(arg.getOption(), arg);
	}

	public void addArgs(O option, String ... values) {
		if (option == null) {
			throw new NullPointerException("option cannot be null");
		}
		addArg(new CommandArgument<O>(option, values));
	}

	public void addArg(O option) {
		addArgs(option, new String[0]);
	}

	public void addArg(O option, String value) {
		addArgs(option, value);
	}

	public void addArgs(O ... options) {
		if (options == null) {
			throw new NullPointerException("options cannot be null");
		}
		for (int i = 0; i < options.length; i++) {
			if (options[i] == null) {
				throw new NullPointerException("options[" + i + "] cannot be null");
			}
			addArg(options[i]);
		}
	}

	public boolean isOptSet(O option) {
		return arguments.containsKey(option);
	}

	public String getArgumentValue(final O option) {
		if(!arguments.containsKey(option)){
			throw new IllegalArgumentException("Expecting single argument of option: " + option.toString() + " but have none.");
		}

		CommandArgument<O> arg = arguments.get(option);
		if (arg.getValues().size() != 1) {
			throw new IllegalArgumentException("Expecting single argument of option: " + option.toString() + " but have: " + arg.getValues().size());
		}
		return arguments.get(option).getValues().get(0);
	}

	public CommandArgument<O> getArgumentOfOption(O option) {
		return arguments.get(option);
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		final Iterator<CommandArgument<O>> i = arguments.values().iterator();

		while (i.hasNext()) {
			sb.append(i.next());
			if (i.hasNext()) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	@Override
	public CommandArguments<O> clone() {
		final CommandArguments<O> clone;
		try {
			clone = (CommandArguments<O>) super.clone();
			clone.arguments = new HashMap<O, CommandArgument<O>>();
			for (CommandArgument<O> arg : this.arguments.values()) {
				clone.arguments.put(arg.getOption(), arg.clone());
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable: " + getClass().getName());
		}
	}
}
