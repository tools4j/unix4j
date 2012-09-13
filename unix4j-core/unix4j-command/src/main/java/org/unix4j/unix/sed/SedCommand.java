package org.unix4j.unix.sed;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Sed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.unix4j.util.Assert.assertArgTrue;

/**
 * Implementation of the {@code Sed sed} command.
 */
class SedCommand extends AbstractCommand<SedArguments> {

	public SedCommand(SedArguments arguments) {
		super(Sed.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final String SED_REGEX = "s/(.*?)(?<!\\\\)/(.*?)(?<!\\\\)/(g)?";
		final Pattern pattern = Pattern.compile(SED_REGEX);
		final String script = getArguments().getScript();

		assertArgTrue("Invalid sed script, must be in the form s/<search>/<replace>/[g]", script.matches(SED_REGEX));
		final Matcher m = pattern.matcher(script);
		m.find();
		final String search = m.group(1);
		final String replace = m.group(2);
		final String globalSearchAndReplaceStr = m.group(3);
		final boolean isGlobalSearchAndReplace = globalSearchAndReplaceStr != null && globalSearchAndReplaceStr.equals("g");

		return new LineProcessor() {
			@Override
			public boolean processLine(Line line) {
				final String content = line.getContent();//or should sed operate on line with ending?
				final String changed;
				if (isGlobalSearchAndReplace) {
					changed = content.replaceAll(search, replace);
				} else {
					changed = content.replaceFirst(search, replace);
				}
				return output.processLine(changed == content ? line : new SimpleLine(changed, line.getLineEnding()));
			}

			@Override
			public void finish() {
				output.finish();
			}
		};
	}
}
