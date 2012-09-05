package org.unix4j.unix;

import static org.unix4j.util.Assert.assertArgNotNull;
import static org.unix4j.util.Assert.assertArgTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unix4j.command.AbstractArgs;
import org.unix4j.command.AbstractCommand;
import org.unix4j.command.CommandInterface;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.LineProcessor;
import org.unix4j.line.SimpleLine;
import org.unix4j.util.TypedMap;

/**
 * <b>NAME</b>
 * <p>
 * sed - stream editor
 * <p>
 * <b>SYNOPSIS</b>
 *
 * <pre>
 * sed script
 * </pre>
 *
 * <b>DESCRIPTION</b>
 * <p>
 * Sed is a stream editor. A stream editor is used to perform basic text
 * transformations on an input stream (a file or input from a pipeline).
 * While in some ways similar to an editor which permits scripted edits
 * (such as ed), sed works by making only one pass over the input(s),
 * and is consequently more efficient. But it is sed's ability to filter
 * text in a pipeline which particularly distinguishes it from other
 * types of editors.
 * </p>
 * <b>NOTES</b>
 * <p>
 * <ul>
 * <li>Currently only the substitute "s" command is supported.</li>
 * <li>Currently only the forward slash "/" can be used as the script delimiter. e.g. s/search/replace/g</li>
 * <li>A forward slash can be used in the search or replace expression, escaped with a backslash.</li>
 * <li>The g option can be used at the end of the script to denote whether substitute is replace first or replace all per line.</li>
 * </ul>
 * <p>
 * <b>Some examples:</b>
 * <br/>
 * <pre>input day into sed("s/day/night/") This will output "night"</pre>
 * <br/>
 * <pre>input "day and night" into sed("s/\\sand\\s/-to-/") This will output "day-to-night"</pre>
 * (Note the use of \s whitespace character).
 * <br/>
 * Java regular expressions are used for searching and replacing. For an
 * overview of the Java pattern matching, and substition, please see the
 * {@link java.util.regex.Pattern} documentation.
 * </p>
 * <b>OPTIONS</b>
 * <p>
 * No options are currently supported.
 * <p>
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

		/**
		 * An alternate to:
		 * <pre>sed("s/<searchExpression>/<replaceExpression>/g");</pre>
		 *
		 * Will search and replace all occurrences on each line.
		 *
		 * @param searchExpression
		 *            The regular expression to search for.
		 * @param replaceExpression
		 *            The replacement expression.
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
		R sedSubstitute(String searchExpression, String replaceExpression);

		/**
		 * An alternate to:
		 * <pre>sed("s/<searchExpression>/<replaceExpression>/");</pre>
		 *
		 * Will search and replace the first occurrence on each line.
		 *
		 * @param searchExpression
		 *            The regular expression to search for.
		 * @param replaceExpression
		 *            The replacement expression.
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
		R sedSubstituteFirst(String searchExpression, String replaceExpression);
	}

	/**
	 * Option flags for the sed command.
	 */
	public static enum Option implements org.unix4j.option.Option {
		GlobalSearchAndReplace('g');//TODO really an option? not in original sed command
		private final char acronym;

		private Option(char acronym) {
			this.acronym = acronym;
		}

		@Override
		public char acronym() {
			return acronym;
		}
	}

	/**
	 * Arguments and options for the sed command.
	 */
	public static class Args extends AbstractArgs<Option, Args> {
		private static final String SED_REGEX = "s/(.*?)(?<!\\\\)/(.*?)(?<!\\\\)/(g)?";

		private static final TypedMap.Key<String> SCRIPT_KEY = TypedMap.keyFor("script", String.class);
		private static final TypedMap.Key<String> SEARCH_EXPRESSION_KEY = TypedMap.keyFor("search", String.class);
		private static final TypedMap.Key<String> REPLACE_EXPRESSION_KEY = TypedMap.keyFor("replace", String.class);

		public Args(String script) {
			super(Option.class);
			assertArgNotNull("Script cannot be null", script);
			
			final Pattern pattern = Pattern.compile(SED_REGEX);
			assertArgTrue("Invalid sed script, must be in the form s/<search>/<replace>/[g]", script.matches(SED_REGEX));
			final Matcher m = pattern.matcher(script);
			m.find();
			final String search = m.group(1);
			final String replace = m.group(2);
			final String globalSearchAndReplaceStr = m.group(3);

			setArg(SCRIPT_KEY, script);
			setArg(SEARCH_EXPRESSION_KEY, search);
			setArg(REPLACE_EXPRESSION_KEY, replace);
			if (globalSearchAndReplaceStr != null && globalSearchAndReplaceStr.equals("g")){
				setOpt(Option.GlobalSearchAndReplace);
			}
		}

		public Args(String searchExpression, String replaceExpression, boolean isGlobalSearchAndReplace) {
			super(Option.class);
			setArg(SCRIPT_KEY, "s/" + searchExpression + "/" + replaceExpression + "/" + (isGlobalSearchAndReplace ? "g" : ""));
			setArg(SEARCH_EXPRESSION_KEY, searchExpression);
			setArg(REPLACE_EXPRESSION_KEY, replaceExpression);
			if (isGlobalSearchAndReplace) {
				setOpt(Option.GlobalSearchAndReplace);
			}
		}

		public String getScript() {
			return getArg(SCRIPT_KEY);
		}
		public String getSearchExpression() {
			return getArg(SEARCH_EXPRESSION_KEY);
		}
		public String getReplaceExpression() {
			return getArg(REPLACE_EXPRESSION_KEY);
		}
		public boolean isGlobalSearchAndReplace() {
			return hasOpt(Option.GlobalSearchAndReplace);
		}
		@Override
		public String toString() {
			return getScript();
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
			assertArgNotNull("script cannot be null", script);
			return new Command(new Args(script));
		}

		@Override
		public Command sedSubstitute(String searchExpression, String replaceExpression) {
			assertArgNotNull("searchExpression cannot be null", searchExpression);
			assertArgNotNull("replaceExpression cannot be null", replaceExpression);
			return new Command(new Args(searchExpression, replaceExpression, true));
		}

		@Override
		public Command sedSubstituteFirst(String searchExpression, String replaceExpression) {
			assertArgNotNull("searchExpression cannot be null", searchExpression);
			assertArgNotNull("replaceExpression cannot be null", replaceExpression);
			return new Command(new Args(searchExpression, replaceExpression, false));
		}
	};

	/**
	 * sed command implementation.
	 */
	public static class Command extends AbstractCommand<Args> {
		public Command(Args arguments) {
			super(NAME, arguments);
		}

		@Override
		public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
			return new LineProcessor() {
				@Override
				public boolean processLine(Line line) {
					return searchAndReplace(line, output);
				}
				
				@Override
				public void finish() {
					output.finish();
				}
			};
		}
		private boolean searchAndReplace(Line line, LineProcessor output) {
			final Args args = getArguments();
			final String content = line.getContent();//or should sed operate on line with ending?
			final String changed;
			if (args.isGlobalSearchAndReplace()) {
				changed = content.replaceAll(args.getSearchExpression(), args.getReplaceExpression());
			} else {
				changed = content.replaceFirst(args.getSearchExpression(), args.getReplaceExpression());
			}
			return output.processLine(changed == content ? line : new SimpleLine(changed, line.getLineEnding()));
		}
	}

	// no instances
	private Sed() {
		super();
	}
}
