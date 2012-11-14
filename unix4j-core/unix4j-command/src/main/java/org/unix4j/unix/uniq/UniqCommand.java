package org.unix4j.unix.uniq;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.unix4j.command.AbstractCommand;
import org.unix4j.context.ExecutionContext;
import org.unix4j.io.FileInput;
import org.unix4j.processor.LineProcessor;
import org.unix4j.processor.RedirectInputLineProcessor;
import org.unix4j.unix.Ls;
import org.unix4j.unix.Uniq;
import org.unix4j.util.FileUtil;

/**
 * Implementation of the {@link Uniq uniq} command.
 */
class UniqCommand extends AbstractCommand<UniqArguments> {
	public UniqCommand(UniqArguments arguments) {
		super(Ls.NAME, arguments);
	}

	@Override
	public LineProcessor execute(final ExecutionContext context, final LineProcessor output) {
		final UniqArguments args = getArguments(context);
		//input from file?
		if (args.isFileSet()) {
			final FileInput input = new FileInput(args.getFile());
			return getFileInputProcessor(Collections.singletonList(input), context, output, args);
		} else if (args.isPathSet()) {
			final List<File> files = FileUtil.expandFiles(args.getPath());
			final List<FileInput> inputs = FileInput.multiple(files);
			return getFileInputProcessor(inputs, context, output, args);
		}
		
		//no, from standard input
		return getStandardInputProcessor(context, output, args);
	}
	
	private LineProcessor getStandardInputProcessor(ExecutionContext context, LineProcessor output, UniqArguments args) {
		if (args.isGlobal()) {
			if (args.isUniqueOnly()) {
				return new GlobalProcessor.UniqueOnly(this, context, output);
			} else if (args.isDuplicatedOnly()) {
				return new GlobalProcessor.DuplicateOnly(this, context, output);
			} else if (args.isCount()) {
				return new GlobalProcessor.Count(this, context, output);
			}
			return new GlobalProcessor.Normal(this, context, output);
		} else {
			if (args.isUniqueOnly()) {
				return new AdjacentProcessor.UniqueOnly(this, context, output);
			} else if (args.isDuplicatedOnly()) {
				return new AdjacentProcessor.DuplicateOnly(this, context, output);
			} else if (args.isCount()) {
				return new AdjacentProcessor.Count(this, context, output);
			}
			return new AdjacentProcessor.Normal(this, context, output);
		}
	}
	private LineProcessor getFileInputProcessor(List<FileInput> inputs, ExecutionContext context, LineProcessor output, UniqArguments args) {
		final LineProcessor standardInputProcessor = getStandardInputProcessor(context, output, args); 
		return new RedirectInputLineProcessor(inputs, standardInputProcessor);
	}

}