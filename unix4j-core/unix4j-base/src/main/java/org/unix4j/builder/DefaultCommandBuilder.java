package org.unix4j.builder;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.unix4j.command.Command;
import org.unix4j.command.ExitValueException;
import org.unix4j.command.NoOp;
import org.unix4j.context.DefaultExecutionContext;
import org.unix4j.io.BufferedOutput;
import org.unix4j.io.FileOutput;
import org.unix4j.io.NullOutput;
import org.unix4j.io.Output;
import org.unix4j.io.StdOutput;
import org.unix4j.io.StreamOutput;
import org.unix4j.io.StringOutput;
import org.unix4j.io.WriterOutput;
import org.unix4j.line.Line;

/**
 * Default implementation for a {@link CommandBuilder}. Builds a {@link NoOp}
 * command if no command is {@link #join(Command) joined} to the command chain
 * of this builder.
 */
public class DefaultCommandBuilder implements CommandBuilder {

	private Command<?> command = NoOp.INSTANCE;

	/**
	 * Default constructor, initialized to build a {@link NoOp} command if no
	 * command is {@link #join(Command) joined} to this builder's command chain.
	 */
	public DefaultCommandBuilder() {
		super();
	}

	@Override
	public CommandBuilder join(Command<?> command) {
		if (command == null) {
			throw new NullPointerException("command argument cannot be null");
		}
		this.command = this.command.join(command);
		return this;
	}

	@Override
	public CommandBuilder reset() {
		command = NoOp.INSTANCE;
		return this;
	}

	@Override
	public Command<?> build() {
		return command;
	}

	@Override
	public String toString() {
		return command.toString();
	}

	@Override
	public void toStdOut() {
		toOutput(new StdOutput());
	}

	@Override
	public List<Line> toLineList() {
		final List<Line> lines = new ArrayList<Line>();
		toOutput(new BufferedOutput(lines));
		return lines;
	}

	@Override
	public List<String> toStringList() {
		final List<String> lines = new ArrayList<String>();
		toOutput(new Output() {
			@Override
			public boolean processLine(Line line) {
				lines.add(line.getContent());
				return true;// we want more lines
			}

			@Override
			public void finish() {
				// no op
			}
		});
		return lines;
	}

	@Override
	public void toOutput(Output output) {
		final Command<?> command = build();
		command.execute(new DefaultExecutionContext(), output).finish();
	}

	@Override
	public void toFile(String file) {
		toFile(new File(file));
	}

	@Override
	public void toFile(File file) {
		toOutput(new FileOutput(file));
	}

	@Override
	public void toOutputStream(OutputStream stream) {
		toOutput(new StreamOutput(stream));
	}

	@Override
	public void toWriter(Writer writer) {
		toOutput(new WriterOutput(writer));
	}

	@Override
	public void toDevNull() {
		toOutput(NullOutput.DEFAULT);
	}

	@Override
	public String toStringResult() {
		final StringOutput out = new StringOutput();
		toOutput(out);
		return out.toString();
	}

	@Override
	public int toExitValue() {
		try {
			toDevNull();
			return 0;
		} catch (ExitValueException e) {
			return e.getExitValue();
		}
	}
}
