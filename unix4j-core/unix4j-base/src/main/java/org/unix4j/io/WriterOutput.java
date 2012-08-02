package org.unix4j.io;

import java.io.IOException;
import java.io.Writer;

import org.unix4j.line.Line;

public class WriterOutput implements Output {

	private final Writer writer;

	private Line lastTerminatedLine;
	private Line lastLine;

	public WriterOutput(Writer writer) {
		this.writer = writer;
		init();
	}

	private void init() {
		lastTerminatedLine = Line.EMPTY_LINE;
		lastLine = null;
	}

	/**
	 * Returns the underlying writer that was passed to the constructor.
	 * 
	 * @return the writer that was passed to the constructor.
	 */
	protected Writer getWriter() {
		return writer;
	}

	@Override
	public boolean processLine(Line line) {
		try {
			if (lastLine != null) {
				writer.write(lastTerminatedLine.getLineEnding());
			}
			writer.write(line.getContent());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		lastLine = line;
		if (line.getLineEndingLength() > 0) {
			lastTerminatedLine = line;
		}
		return true;
	}

	@Override
	public void finish() {
		try {
			if (lastLine != null && writeLastLineEnding()) {
				writer.write(lastLine.getLineEnding());
			}
			writer.flush();
			init();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns true if the last line ending should be written, a
	 * 
	 * @return
	 */
	protected boolean writeLastLineEnding() {
		return true;
	}

}
