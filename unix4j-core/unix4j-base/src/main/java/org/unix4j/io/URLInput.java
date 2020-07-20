package org.unix4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Input device based on a {@link URL}.
 */
public class URLInput extends StreamInput {

	private final String urlInfo;

	public URLInput(URL url) {
		super(openStream(url));
		this.urlInfo = url.toExternalForm();
	}

	private static InputStream openStream(URL url) {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return urlInfo;
	}
}
