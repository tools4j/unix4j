package org.unix4j.unix.grep;

import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Grep;

/**
 * User: ben
 */
public class GrepCommand extends AbstractCommand<GrepArguments> {
	public GrepCommand(GrepArguments arguments) {
		super(Grep.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, LineProcessor output) {
		return getStandardInputProcessor(context, output);
	}
	
	private LineProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output) {
		if (getArguments().isFixedStrings()) {
			return new FixedStringsProcessor(this, context, output);
		} else {
			return new RegexpProcessor(this, context, output);
		}
	}
	private LineProcessor getFileInputProcessor(List<FileInput> inputs, ExecutionContext context, LineProcessor output) {
		return null;//FIXME
	}
}
