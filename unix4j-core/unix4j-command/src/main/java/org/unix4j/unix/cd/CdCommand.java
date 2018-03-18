package org.unix4j.unix.cd;

import org.unix4j.command.AbstractCommand;
import org.unix4j.command.Command;
import org.unix4j.command.JoinedCommand;
import org.unix4j.context.DerivedExecutionContext;
import org.unix4j.context.ExecutionContext;
import org.unix4j.processor.LineProcessor;
import org.unix4j.unix.Cd;
import org.unix4j.util.FileUtil;

import java.io.File;
import java.util.List;

/**
 * Implementation of the {@link Cd cd} command.
 */
class CdCommand extends AbstractCommand<CdArguments> {
	public CdCommand(CdArguments arguments) {
		super(Cd.NAME, arguments);
	}

	@Override
	public Command<CdArguments> join(Command<?> next) {
		return join(this, next);
	}

	private static Command<CdArguments> join(Command<CdArguments> first, Command<?> second) {
		return new JoinedCommand<CdArguments>(first, second) {
			@Override
			public LineProcessor execute(ExecutionContext context, LineProcessor output) {
				final CdArguments args = getArguments(context);
				final File file;
				if (args.isPathSet()) {
					final String path = args.getPath(context);
					final List<File> files = FileUtil.expandFiles(context.getCurrentDirectory(), path);
					if (files.isEmpty()) {
						throw new IllegalArgumentException("no file found for path argument: " + path);
					}
					file = files.get(0);
				} else if (args.isFileSet()) {
					file = args.getFile();
				} else {
					file = context.getUserHome();
				}
				if (!file.isDirectory()) {
					throw new IllegalArgumentException("not a valid directory: " + file.getAbsolutePath());
				}
				final DerivedExecutionContext currentDirContext = new DerivedExecutionContext(context);
				currentDirContext.setCurrentDirectory(file);
				return super.execute(currentDirContext, output);
			}
			@Override
			public Command<?> join(Command<?> next) {
				return CdCommand.join(getFirst(), getSecond().join(next));
			}
		};
	}

	@Override
	public LineProcessor execute(ExecutionContext context, final LineProcessor output) {
		return output;// pipe through, we don't do anything with the input or
						// output
	}
}
