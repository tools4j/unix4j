package org.unix4j.command;

import org.unix4j.io.FileInput;
import org.unix4j.io.Input;

/**
 * An exception thrown when a command terminates with an error; in Unix, the
 * command would return a value different from zero. Note that the Java VM does
 * NOT terminate and exit if such an exception occurs.
 */
public class ExitValueException extends RuntimeException {

	private static final long serialVersionUID = 2153738224794893827L;

	private final int exitValue;
	private Input input;

	/**
	 * Constructor with message and non-zero exit value.
	 * 
	 * @param message
	 *            the error message
	 * @param exitValue
	 *            the exit value, must be different from zero
	 * @throws IllegalArgumentException
	 *             if {@code exitValue==0}
	 */
	public ExitValueException(String message, int exitValue) {
		super(message);
		if (exitValue == 0) {
			throw new IllegalArgumentException("exit value must be a non-zero value");
		}
		this.exitValue = exitValue;
	}

	/**
	 * Constructor with message, a non-zero exit value and a causing exception.
	 * 
	 * @param message
	 *            the error message
	 * @param exitValue
	 *            the exit value, must be different from zero
	 * @param cause
	 *            the exception that caused the error termination of the command
	 * @throws IllegalArgumentException
	 *             if {@code exitValue==0}
	 */
	public ExitValueException(String message, int exitValue, Throwable cause) {
		super(message, cause);
		if (exitValue == 0) {
			throw new IllegalArgumentException("exit value must be a non-zero value");
		}
		this.exitValue = exitValue;
	}

	/**
	 * Returns the exit value, an int value or error code different from zero
	 * that was passed to the constructor of this exception.
	 * 
	 * @return the non-zero exit or error code value
	 */
	public int getExitValue() {
		return exitValue;
	}

	@Override
	public String getMessage() {
		if (input == null) {
			return super.getMessage();
		}
		return super.getMessage() + "[input:" + (input instanceof FileInput ? ((FileInput) input).getFileInfo() : input.toString()) + "]";
	}

	/**
	 * Sets the input source that was causing this exception, for instance an
	 * input file that was passed to a command.
	 * 
	 * @param input
	 *            the input source or file that was causing the exception
	 */
	public void setInput(Input input) {
		this.input = input;
	}

	/**
	 * Returns the input source that was causing this exception, for instance an
	 * input file that was passed to a command. Returns null if it is unknown.
	 * 
	 * @return the input source or file that was causing the exception, or null
	 *         if it is not known
	 */
	public Input getInput() {
		return input;
	}
}
