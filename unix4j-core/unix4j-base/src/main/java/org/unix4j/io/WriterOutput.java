package org.unix4j.io;

import java.io.IOException;
import java.io.Writer;

import org.unix4j.line.Line;
import org.unix4j.line.SimpleLine;


public class WriterOutput implements Output {
	
	private final Writer writer;
	
	private Line lastTerminatedLine = new SimpleLine(""); 
	private boolean lastLineTerminated = true;
	
	public WriterOutput(Writer writer) {
		this.writer = writer;
	}
	
	@Override
	public boolean processLine(Line line) {
		if (!lastLineTerminated) {
			try {
				writer.write(lastTerminatedLine.getLineEnding());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		line.write(writer);
		if (line.getLineEndingLength() > 0) {
			lastTerminatedLine = line;
			lastLineTerminated = true;
		} else {
			lastLineTerminated = false;
		}
		return true;
	}
	
	@Override
	public void finish() {
		try {
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
