package org.unix4j.unix.wc;

import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.processor.InputLineProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Wc;
import org.unix4j.util.FileUtil;

/**
 * Implementation of the {@link Wc wc} command.
 */
class WcCommand extends AbstractCommand<WcArguments> {
	public WcCommand(WcArguments arguments) {
		super(Wc.NAME, arguments);
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		final WcArguments args = getArguments(context);
		
		//input from files?
		final List<FileInput> inputs;
		if (args.isFilesSet()) {
			inputs = FileInput.multiple(args.getFiles());
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(context.getCurrentDirectory(), args.getPaths());
			inputs = FileInput.multiple(files);
		} else {
			//standard input
			return getStandardInputProcessor(context, output);
		}
		
        if(inputs.size() == 1){
			return new InputLineProcessor(inputs.get(0), new WcFileProcessor(args), output);

        } else if(inputs.size() > 1){
            return new WcMultipleFilesProcessor(inputs, output, args);

		} else {
			throw new IllegalStateException("No inputs specified");
		}
	}
	
	private WcProcessor getStandardInputProcessor(ExecutionContext context, final LineProcessor output) {
		return new WcProcessor(this, context, output);
	}
}
