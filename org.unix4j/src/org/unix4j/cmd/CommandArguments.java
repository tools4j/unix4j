package org.unix4j.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommandArguments<O extends Enum<O>> implements Cloneable {
	private Map<O, CommandArgument<O>> arguments;

	public CommandArguments() {
		this.arguments = new HashMap<O, CommandArgument<O>>();
	}

	public void addArgument(final CommandArgument<O> arg) {
		arguments.put(arg.getOption(), arg);
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
