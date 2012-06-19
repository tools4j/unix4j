package org.unix4j;

import java.util.List;

import org.unix4j.arg.Arg;
import org.unix4j.arg.ArgList;
import org.unix4j.arg.ArgVals;
import org.unix4j.arg.Opt;
import org.unix4j.arg.OptMap;
import org.unix4j.io.NullInput;
import org.unix4j.io.StdOutput;

/**
 * Abstract base implementation suitable for most common commands. Command
 * implementations usually only define arguments and options and implement the
 * {@link #executeBatch()} method.
 *
 * @param <E>
 *            enum defining argument and option keywords for this command
 */
abstract public class AbstractCommand<E extends Enum<E>> implements Command<E> {

	private final String name;
	private final boolean batchable;
	private OptMap<E> opts = new OptMap<E>();
	private Input input = new NullInput();
	private Output output = new StdOutput();

	/**
	 * Constructor with command name and batchable flag.
	 *
	 * @param name
	 *            the name of the command, usually a lower case word such as
	 *            "echo", "ls" or "grep"
	 * @param batchable
	 *            true if the command supports execution in batches, as defined
	 *            by {@link Command#isBatchable()}
	 */
	public AbstractCommand(String name, boolean batchable) {
		this.name = name;
		this.batchable = batchable;
	}

	/**
	 * Executes a single batch. For non-batchable commands, this method is
	 * called only once with the complete input. For batchable commands, the
	 * method might be called once or multiple times, possibly with a single
	 * line in the input.
	 * <p>
	 * In either case, implementors should read to the end of the input and
	 * process the input. If a command expects no input at all, it is not
	 * required to read the input (such commands may throw an exception in the
	 * {@link #readFrom(Input)} method instead).
	 */
	abstract protected void executeBatch();

	@Override
	public final void execute() {
		do {
			executeBatch();
		} while (getInput().hasMoreLines());
		getOutput().finish();
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

	public <V> Command<E> withArg(Arg<E, V> arg, V value) {
		opts.addArg(arg, value);
		return this;
	}

	public <V> Command<E> withArgs(Arg<E, V> arg, V... values) {
		opts.addArgs(arg, values);
		return this;
	}

	public <V> Command<E> withArgs(Arg<E, V> arg, List<? extends V> values) {
		opts.addArgs(arg, values);
		return this;
	}

	@Override
	public Command<E> withOpt(Opt<E> opt) {
		opts.setOpt(opt);
		return this;
	}

	@Override
	public Command<E> withOpts(Opt<E> ... opts){
		this.opts.setOpts(opts);
		return this;
	}

	@Override
	public <V> ArgList<E, V> getArgs(Arg<E, V> arg) {
		return opts.getArgList(arg, true);
	}

	@Override
	public boolean isOptSet(Opt<E> opt) {
		return opts.isOptSet(opt);
	}

	@Override
	public boolean isArgSet(Arg<E, ?> arg) {
		return opts.isArgSet(arg);
	}

	@Override
	public <V> Command<E> withArgVals(ArgVals<E,V> argVals) {
		if (argVals.getArg() instanceof Opt) {
			@SuppressWarnings("unchecked")
			final Opt<E> opt = (Opt<E>)argVals.getArg();
			return withOpt(opt);
		} else {
			return withArgs(argVals.getArg(), argVals.getValues());
		}
	}
	//avoid generic array warning when using withArgVals(ArgVals<E,?>... argVals) in command builder
	@SuppressWarnings("unchecked")
	public Command<E> withArgVals(ArgVals<E,?> argVals1, ArgVals<E,?> argVals2) {
		return withArgVals(new ArgVals[]{argVals1, argVals2});
	}
	//avoid generic array warning when using withArgVals(ArgVals<E,?>... argVals) in command builder
	@SuppressWarnings("unchecked")
	public Command<E> withArgVals(ArgVals<E,?> argVals1, ArgVals<E,?> argVals2, ArgVals<E,?> argVals3) {
		return withArgVals(new ArgVals[]{argVals1, argVals2, argVals3});
	}
	//avoid generic array warning when using withArgVals(ArgVals<E,?>... argVals) in command builder
	@SuppressWarnings("unchecked")
	public Command<E> withArgVals(ArgVals<E,?> argVals1, ArgVals<E,?> argVals2, ArgVals<E,?> argVals3, ArgVals<E,?> argVals4) {
		return withArgVals(new ArgVals[]{argVals1, argVals2, argVals3, argVals4});
	}
	//avoid generic array warning when using withArgVals(ArgVals<E,?>... argVals) in command builder
	@SuppressWarnings("unchecked")
	public Command<E> withArgVals(ArgVals<E,?> argVals1, ArgVals<E,?> argVals2, ArgVals<E,?> argVals3, ArgVals<E,?> argVals4, ArgVals<E,?> argVals5) {
		return withArgVals(new ArgVals[]{argVals1, argVals2, argVals3, argVals4, argVals5});
	}
	@Override
	public Command<E> withArgVals(ArgVals<E,?>... argVals) {
		for (ArgVals<E, ?> argVal : argVals) {
			withArgVals(argVal);
		}
		return this;
	}

	@Override
	public Command<E> readFrom(Input input) {
		if (input == null) {
			throw new NullPointerException("input cannot be null");
		}
		this.input = input;
		return this;
	}

	@Override
	public Command<E> writeTo(Output output) {
		if (output == null) {
			throw new NullPointerException("output cannot be null");
		}
		this.output = output;
		return this;
	}

	@Override
	public <O2 extends Enum<O2>> Command<O2> join(Command<O2> next) {
		return new JoinedCommand<O2>(this, next);
	}

	@Override
	public AbstractCommand<E> clone() {
		try {
			@SuppressWarnings("unchecked")
			final AbstractCommand<E> clone = (AbstractCommand<E>) super.clone();
			clone.opts = this.opts.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException("should be cloneable: " + getClass().getName());
		}
	}

	@Override
	public String toString() {
		return getName() + " " + opts;
	}
}
