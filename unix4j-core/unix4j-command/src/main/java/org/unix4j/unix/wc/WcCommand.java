package org.unix4j.unix.wc;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Wc;

/**
 * User: ben
 */
public class WcCommand extends AbstractCommand<WcArguments> {
	public WcCommand(WcArguments arguments) {
		super(Wc.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		return new WcProcessor(this, context, output);
	}

}
