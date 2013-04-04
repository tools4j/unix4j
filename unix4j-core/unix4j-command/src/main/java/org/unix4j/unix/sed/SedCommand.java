package org.unix4j.unix.sed;

import static org.unix4j.util.Assert.assertArgTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Sed;

/**
 * Implementation of the {@link Sed sed} command.
 */
class SedCommand extends AbstractCommand<SedArguments> {
	public SedCommand(SedArguments arguments) {
		super(Sed.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final SedArguments args = getArguments(context);
		if (args.isScriptSet()) {
			return executeScript(args, output);
		}
		if (args.isSubstitute()) {
			return new SubstituteProcessor(args, output);
		}
		if (args.isAppend()) {
			return new AppendProcessor(args, output);
		}
		if (args.isInsert()) {
			return new InsertProcessor(args, output);
		}
		if (args.isChange()) {
			return new ChangeProcessor(args, output);
		}
		if (args.isDelete()) {
			return new DeleteProcessor(args, output);
		}
		if (args.isTranslate()) {
			return new TranslateProcessor(args, output);
		}

		//default command
		if (args.isReplacementSet() || args.isString2Set()) {
			return new SubstituteProcessor(args, output);
		}
		return new PrintProcessor(args, output);
	}
	
	private LineProcessor executeScript(SedArguments args, final LineProcessor output) {
		final String SED_REGEX = "s/(.*?)(?<!\\\\)/(.*?)(?<!\\\\)/(g)?";
		final Pattern pattern = Pattern.compile(SED_REGEX);
		final String script = args.getScript();

		assertArgTrue("Invalid sed script, must be in the form s/<search>/<replace>/[g]", script.matches(SED_REGEX));
		final Matcher m = pattern.matcher(script);
		m.find();
		final String regexp = m.group(1);
		final String replacement = m.group(2);
		final String globalSearchAndReplaceStr = m.group(3);
		final boolean isGlobal = globalSearchAndReplaceStr != null && globalSearchAndReplaceStr.equals("g");

		return new SubstituteProcessor(regexp, replacement, -1, isGlobal, args, output);
	}
}
