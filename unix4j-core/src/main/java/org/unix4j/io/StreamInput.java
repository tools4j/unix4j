package org.unix4j.io;

import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamInput extends ReaderInput {
	public StreamInput(InputStream in) {
		super(new InputStreamReader(in));
	}
}
