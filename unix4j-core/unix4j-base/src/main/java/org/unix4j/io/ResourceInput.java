package org.unix4j.io;

import java.io.InputStream;

/**
 * Input device reading a resource using
 * {@link Class#getResourceAsStream(String)}.
 */
public class ResourceInput extends StreamInput {
	/**
	 * Creates an input object opening the given {@code resource} using
	 * {@link Class#getResourceAsStream(String)}.
	 * 
	 * @param resource
	 *            a path to the file on the classpath; if the file is in the
	 *            root directory, the filename should be prefixed with a forward
	 *            slash, e.g.: {@code "/test-file.txt"}; if the file is in a
	 *            package, then the package should be specified prefixed with a
	 *            forward slash, and with each dot "." replaced with a forward
	 *            slash. e.g.: {@code "/org/company/my/package/test-file.txt"}
	 * @see Class#getResource(String)
	 * @see Class#getResourceAsStream(String)
	 */
	public ResourceInput(String resource) {
		super(openStream(resource));
	}

	private static InputStream openStream(String resource) {
		final InputStream stream = ResourceInput.class.getResourceAsStream(resource);
		if (stream != null) {
			return stream;
		}
		throw new IllegalArgumentException("resource not found: " + resource);
	}
}
