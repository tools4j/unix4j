package org.unix4j.io;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamOutput extends WriterOutput {
	public StreamOutput(OutputStream out) {
		super(new OutputStreamWriter(out));
	}
}
