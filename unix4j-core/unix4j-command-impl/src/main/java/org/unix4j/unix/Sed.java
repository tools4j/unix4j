package org.unix4j.unix;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.io.Input;
import org.unix4j.io.Output;
import org.unix4j.util.TypedMap;

/**
 * Non-instantiable module with inner types making up the sed command.
 */
public final class Sed {

	/**
	 * The "sed" command name.
	 */
	public static final String NAME = "sed";

	/**
	 * Interface defining all method signatures for the sed command.
	 *
	 * @param <R>
	 *            the return type for all command signature methods, usually a
	 *            new command instance or a command fromFile providing methods
	 *            for chained invocation of following commands
	 */
	public static interface Interface<R> extends CommandInterface<R> {
		/**
		 * Sed is a stream editor. A stream editor is used to perform basic text
		 * transformations on an input stream (a file or input from a pipeline).
		 * While in some ways similar to an editor which permits scripted edits
		 * (such as ed), sed works by making only one pass over the input(s),
		 * and is consequently more efficient. But it is sed's ability to filter
		 * text in a pipeline which particularly distinguishes it from other
		 * types of editors.
		 * <p>
		 * This command is very much a work-in-progress.
		 * <p>
		 * Currently the only sed command supported is the substition "s"
		 * command.
		 * <p>
		 * e.g. input day into sed("s/day/night/") This will output "night".
		 * <p>
		 * e.g. input "day and night" into sed("s/\\sand\\s/-to-/") This will
		 * output "day-to-night". (Note the use of \s whitespace character).
		 * <p>
		 * Java regular expressions are used for searching and replacing. For an
		 * overview of the Java pattern matching, and substition, please see the
		 * {@link java.util.regex.Pattern} documentation.
		 *
		 * @param script
		 *            the sed script
		 * @return the generic type {@code <R>} defined by the implementing
		 *         class, even if the command itself returns no value and writes
		 *         its result to an {@link org.unix4j.io.Output} object. This
		 *         serves implementing classes like the command {@link Factory}
		 *         to return a new {@link Command} instance for the argument
		 *         values passed to this method.
		 *         {@link org.unix4j.builder.CommandBuilder} extensions also
		 *         implementing this this command interface usually return an
		 *         instance to itself facilitating chained invocation of joined
		 *         commands.
		 */
		R sed(String script);
	}

	/**
	 * Option flags for the sed command.
	 */
	public static enum Option {
		// no options?
	}

	/**
	 * Arguments and options for the sed command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		public static final TypedMap.Key<String> SCRIPT = TypedMap.DefaultKey.keyFor("script", String.class);

		public Args(String script) {
			super(Option.class);
			if (script == null) {
				throw new NullPointerException("script cannot be null");
			}
			setArg(SCRIPT, script);
		}

		public String getScript() {
			return getArg(SCRIPT);
		}
	}

	/**
	 * Singleton {@link Factory} for the sed command.
	 */
	public static final Factory FACTORY = new Factory();

	/**
	 * Factory class returning a new {@link Command} instance from every
	 * signature method.
	 */
	public static final class Factory implements Interface<Command> {
		@Override
		public Command sed(String script) {
			return new Command(new Args(script));
		}
	};

	/**
	 * sed command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, Type.LineByLine, arguments);
		}

		@Override
		public Command withArgs(Args arguments) {
			return new Command(arguments);
		}

		@Override
		public void executeBatch(Input input, Output output) {
			final Args args = getArguments();
			final String SED_REGEX = "s/(.*?)/(.*?)/(g)?";
			final String script = args.getScript();
			if(!script.matches(SED_REGEX)){
				throw new IllegalArgumentException("Invalid sed script, must be in the form s/<search>/<replace>/[g]");
			} else {
				Pattern p = Pattern.compile(SED_REGEX);
				Matcher m = p.matcher(script);
				m.find();
				final String search = m.group(1);
				final String replace = m.group(2);
				final String globalSearchAndReplaceStr = m.group(3);

				boolean globalSearchAndReplace = false;
				if(  globalSearchAndReplaceStr != null && globalSearchAndReplaceStr.equals("g")){
					globalSearchAndReplace = true;
				}
				searchAndReplace(input, output, search, replace, globalSearchAndReplace);
			}
		}

		private void searchAndReplace(Input input, Output output, final String search, final String replace, final boolean globalSearchAndReplace) {
			while (input.hasMoreLines()) {
				String line = input.readLine();

				if(globalSearchAndReplace){
					line = line.replaceAll(search, replace);
				} else {
					line = line.replaceFirst(search, replace);
				}
				output.writeLine(line);
			}
		}
	}

	// no instances
	private Sed() {
		super();
	}
}
