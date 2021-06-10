package org.unix4j.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class with static utility methods to load package or file resources.
 */
public class ResourceUtil {

	/**
	 * Returns the specified resource as input stream. Uses
	 * {@link Class#getResourceAsStream(String)} falling back to {@link File} if
	 * the resource is not found. If neither method succeeds, an exception is
	 * thrown.
	 * 
	 * @param name
	 *            the resource name, a file name or a package relative resource
	 * 
	 * @return the resource as input stream, never null
	 * @throws IllegalArgumentException
	 *             if the resource cannot be found or read
	 */
	public static InputStream getResource(Class<?> base, String name) {
		final InputStream in = base.getResourceAsStream(name);
		if (in != null) {
			return in;
		}
		try {
			return new FileInputStream(name);
		} catch (IOException e) {
			throw new IllegalArgumentException("resource '" + name + "' not found");
		}
	}

	// no instances
	private ResourceUtil() {
		super();
	}
}
