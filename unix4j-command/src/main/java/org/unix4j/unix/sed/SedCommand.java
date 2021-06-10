package org.unix4j.unix.sed;

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
			//command specified in script
			final String script = args.getScript();
			final Command command = Command.fromScript(script);
			if (command == null) {
				throw new IllegalArgumentException("command missing or invalid in sed script: " + script);
			}
			return command.createProcessorFor(script, args, output);
		}

		//command from args, or default if not specified
		Command command = Command.fromArgs(args);
		if (command == null) {
			//default command
			if (args.isReplacementSet() || args.isString2Set()) {
				command = Command.substitute;
			} else {
				command = Command.print;
			}
		}
		return command.createProcessorFor(args, output);
	}
}
