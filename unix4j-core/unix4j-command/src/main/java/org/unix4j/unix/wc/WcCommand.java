package org.unix4j.unix.wc;

import java.io.File;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.MultipleInputLineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
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
		final WcArguments args = getArguments();
		
		//input from files?
		final List<FileInput> inputs;
		if (args.isFilesSet()) {
			inputs = FileInput.multiple(args.getFiles());
		} else if (args.isPathsSet()) {
			final List<File> files = FileUtil.expandFiles(args.getPaths());
			inputs = FileInput.multiple(files);
		} else {
			//standard input
			return getStandardInputProcessor(context, output);
		}
		
		if (inputs.size() > 1) {
			//totals line at end
			final WcMultiFileProcessor processor = new WcMultiFileProcessor(this, context, output);
			return new MultipleInputLineProcessor(inputs, processor, output) {
				@Override
				protected void finishMultiple(java.util.List<? extends org.unix4j.io.Input> inputs, LineProcessor output) {
					processor.writeTotalsLine(output);
				}
			};
		} else {
			final LineProcessor standardInputProcessor = getStandardInputProcessor(context, output);
			return new RedirectInputLineProcessor(inputs, standardInputProcessor);
		}
	}
	
	private WcProcessor getStandardInputProcessor(ExecutionContext context, final LineProcessor output) {
		return new WcProcessor(this, context, output);
	}

}
