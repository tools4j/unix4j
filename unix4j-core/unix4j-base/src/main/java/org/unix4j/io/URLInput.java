package org.unix4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Input read from an {@link URL}.
 */
public class URLInput extends StreamInput {
	
	public URLInput(URL url) {
		super(openStream(url));
	}

	private static InputStream openStream(URL url) {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
