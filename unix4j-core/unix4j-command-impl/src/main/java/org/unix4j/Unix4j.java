package org.unix4j;

import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.builder.Unix4jCommandBuilderImpl;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.io.StreamInput;
import org.unix4j.util.Assert;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

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
		return fromInput(new FileInput(file));
	}

	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified file on the classpath.
	 *
	 * @param filePathAndName
	 *            a path to the file to to redirect to the first command
	 *            The will need to be on the classpath.  If the file is in the
	 *            root directory, the filename should be prefixed with a forward
	 *            slash.  e.g.:
	 *            <pre>/test-file.txt</pre>
	 *            If the file is in a package, then the package should be specified
	 *            prefixed with a forward slash, and with each dot "." replaced
	 *            with a forward slash.  e.g.:
	 *            <pre>/org/company/my/package/test-file.txt</pre>
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromFileOnClasspath(String filePathAndName) {
		Assert.assertArgNotNull("filePathAndName must not be null", filePathAndName);
		URL url = filePathAndName.getClass().getResource(filePathAndName);
		if(url==null){
			throw new IllegalArgumentException("filePathAndName:" + filePathAndName + " references a file which does not exist.");
		}
		File file = new File(url.getFile());
		return fromFile(file);
	}

	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified input stream.
	 *
	 * @param in
	 *            the input stream redirected to the input of the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromInputStream(InputStream in) {
		return fromInput(new StreamInput(in));
	}

	/**
	 * Returns a builder to create a command or command chain reading from the
	 * specified input object.
	 *
	 * @param in
	 *            the input passed to the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromInput(Input in) {
		return new Unix4jCommandBuilderImpl(in);
	}

	/**
	 * Returns a builder to create a command or command chain reading from the
	 * specified input object.
	 *
	 * @param input
	 *            the String written to the input of the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromString(String input) {
		return (new Unix4jCommandBuilderImpl()).echo(input);
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
