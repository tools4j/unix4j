package org.unix4j;

import java.io.File;
import java.io.InputStream;

import org.unix4j.builder.Unix4jCommandBuilder;
import org.unix4j.builder.Unix4jCommandBuilderImpl;
import org.unix4j.io.FileInput;
import org.unix4j.io.Input;
import org.unix4j.io.StreamInput;

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
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder builder(File file) {
		return builder(new FileInput(file));
	}

	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified input stream.
	 * 
	 * @param in
	 *            the input stream redirected to the input of the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder builder(InputStream in) {
		return builder(new StreamInput(in));
	}

	/**
	 * Returns a builder to create a command or command chain reading from the
	 * specified input object.
	 * 
	 * @param in
	 *            the input passed to the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder builder(Input in) {
		return new Unix4jCommandBuilderImpl(in);
	}

	/**
	 * Returns a builder to create a command or command chain providing no
	 * input.
	 * 
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder builder() {
		return new Unix4jCommandBuilderImpl();
	}

	// no instances
	private Unix4j() {
		super();
	}
}
