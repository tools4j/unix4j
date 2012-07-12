package org.unix4j.io;

import java.io.PrintWriter;
import java.io.Writer;


public class WriterOutput implements Output {
	
	private final PrintWriter writer;
	
	public WriterOutput(Writer writer) {
		this.writer = writer instanceof PrintWriter ? (PrintWriter)writer : new PrintWriter(writer);
	}
	
	@Override
	public void writeLine(String line) {
		writer.println(line);
	}
	
	@Override
	public void finish() {
		writer.flush();
	}
	
}
