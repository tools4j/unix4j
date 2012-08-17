package org.unix4j;

import java.io.File;
import java.io.InputStream;

import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.builder.Unix4jCommandBuilderImpl;
import org.unix4j.io.Input;

/**
 * Utility class with static methods serving as starting point to create a
 * command or build a command chain joining several commands.
 */
public class Unix4j {

	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified file.
	 * 
	 * @param file
	 *            the file redirected to the input of the first command
	 * @return the fromFile to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromFile(File file) {
		return create().from(file);
	}

	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified resource relative to the classpath. The resource is
	 * usually a file or URL on the classpath. The resource is read using
	 * {@link Class#getResourceAsStream(String)}.
	 * 
	 * @param resource
	 *            a path to the file to to redirect to the next command The will
	 *            need to be on the classpath. If the file is in the root
	 *            directory, the filename should be prefixed with a forward
	 *            slash. e.g.:
	 * 
	 *            <pre>
	 * /test-file.txt
	 * </pre>
	 * 
	 *            If the file is in a package, then the package should be
	 *            specified prefixed with a forward slash, and with each dot "."
	 *            replaced with a forward slash. e.g.:
	 * 
	 *            <pre>
	 * /org/company/my/package/test-file.txt
	 * </pre>
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromResource(String resource) {
		return create().fromResource(resource);
	}

	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified input stream.
	 * 
	 * @param input
	 *            the input stream redirected to the input of the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder from(InputStream input) {
		return create().from(input);
	}

	/**
	 * Returns a builder to create a command or command chain reading from the
	 * specified input object.
	 * 
	 * @param input
	 *            the input passed to the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder from(Input input) {
		return create().from(input);
	}

	/**
	 * Returns a builder to create a command or command chain reading from the
	 * specified input string.
	 * 
	 * @param input
	 *            the String written to the input of the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromString(String input) {
		return create().fromString(input);
	}

	/**
	 * Returns a builder to create a command or command chain providing no
	 * input.
	 * 
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder create() {
		return new Unix4jCommandBuilderImpl();
	}

	// no instances
	private Unix4j() {
		super();
	}
}
