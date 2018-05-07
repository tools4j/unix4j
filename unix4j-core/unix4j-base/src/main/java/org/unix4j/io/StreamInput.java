package org.unix4j.io;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Input device reading from an {@link InputStream}.
 */
public class StreamInput extends ReaderInput {
	/**
	 * Constructor with stream forming the basis of this input device.
	 * 
	 * @param in
	 *            the input stream to read from
	 */
	public StreamInput(InputStream in) {
		super(new InputStreamReader(in), false);
	}
}
