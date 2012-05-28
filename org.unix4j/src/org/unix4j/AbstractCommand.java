package org.unix4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.unix4j.io.NullInput;
import org.unix4j.io.StdOutput;

abstract public class AbstractCommand<O extends Enum<O>> implements Command<O> {

	private final String name;
	private final boolean batchable;
	private List<String> args = new ArrayList<String>();
	private Map<O,Object> opts = new LinkedHashMap<O,Object>();
	private Input input = new NullInput();
	private Output output = new StdOutput();

	public AbstractCommand(String name, boolean batchable) {
		this.name = name;
		this.batchable = batchable;
	}

	abstract protected void executeBatch();

	@Override
	public final void execute() {
		do {
			executeBatch();
		} while (getInput().hasMoreLines());
		getOutput().finish();
	}
	
	protected int getArgCount() {
		return args.size();
	}

	protected String getArg(int index) {
		return args.get(index);
	}

	protected Iterable<String> getArgs() {
		return Collections.unmodifiableList(args);
	}
	
	protected String getArgsAsString() {
		final StringBuilder sb = new StringBuilder();
		for (final String arg : args) {
			if (sb.length() > 0)
				sb.append(' ');
			sb.append(arg);
		}
		return sb.toString();
	}

	protected boolean isOptSet(O option) {
		return opts.containsKey(option);
	}
	
	protected Object getOpt(O option) {
		return opts.get(option);
	}

	protected Map<O,Object> getOptions() {
		return Collections.unmodifiableMap(opts);
	}

	protected Input getInput() {
		return input;
	}

	protected Output getOutput() {
		return output;
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
	public Command<O> withArg(String arg) {
		if (arg == null) {
			throw new NullPointerException("arg cannot be null");
		}
		args.add(arg);
		return this;
	}

	@Override
	public Command<O> withArgs(String... args) {
		if (args == null) {
			throw new NullPointerException("args cannot be null");
		}
		for (int i = 0; i < args.length; i++) {
			if (args[i] == null) {
				throw new NullPointerException("args[" + i + "] cannot be null");
			}
			this.args.add(args[i]);
		}
		return this;
	}

	@Override
	public Command<O> withOpt(O option) {
		return withOpt(option, null);
	}
	@Override
	public Command<O> withOpt(O option, Object value) {
		if (option == null) {
			throw new NullPointerException("option cannot be null");
		}
		opts.put(option, value);
		return this;
	}

	@Override
	public Command<O> withOpts(O... options) {
		if (options == null) {
			throw new NullPointerException("option cannot be null");
		}
		for (int i = 0; i < options.length; i++) {
			if (options[i] == null) {
				throw new NullPointerException("options[" + i + "] cannot be null");
			}
			opts.put(options[i], null);
		}
		return this;
	}
	
	@Override
	public Command<O> readFrom(Input input) {
		if (input == null) {
			throw new NullPointerException("input cannot be null");
		}
		this.input = input;
		return this;
	}

	@Override
	public Command<O> writeTo(Output output) {
		if (output == null) {
			throw new NullPointerException("output cannot be null");
		}
		this.output = output;
		return this;
	}

	@Override
	public <O2 extends Enum<O2>> JoinedCommand<O2> join(Command<O2> next) {
		return new JoinedCommand<O2>(this, next);
	}
	
	@Override
	public AbstractCommand<O> clone() {
		try {
			@SuppressWarnings("unchecked")
			final AbstractCommand<O> clone = (AbstractCommand<O>)super.clone();
			clone.args = new ArrayList<String>(this.args);
			clone.opts= new LinkedHashMap<O,Object>(this.opts);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable: " + getClass().getName());
		}
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getName());
		for (final Map.Entry<O,Object> option : opts.entrySet()) {
			sb.append(' ').append('-').append(option.getKey());
			if (option.getValue() != null) {
				sb.append(' ').append(option.getValue());
			}
		}
		for (final String arg : args) {
			sb.append(' ').append(arg);
		}
		return sb.toString();
	}

}
