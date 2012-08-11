package org.unix4j.io;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Output device writing to an {@link OutputStream}.
 */
public class StreamOutput extends WriterOutput {
	/**
	 * Constructor with stream to be written to.
	 * 
	 * @param out
	 *            the output stream to write to
	 */
	public StreamOutput(OutputStream out) {
		super(new OutputStreamWriter(out));
	}
}
