package org.unix4j.codegen.annotation;


public enum InputMode {
	/**
	 * The command processes no input at all. There is no obligation for the
	 * command to read the input.
	 * <p>
	 * Commands of this type are suitable starting points for command execution.
	 * A static method is generated in {@code Unix4j} if a command interface
	 * method is annotated with this mode.
	 */
	NoInput,
	/**
	 * The command processes its input line by line. It is, however, a
	 * contractual obligation for the command to read the complete input passed
	 * to the execute method.
	 * <p>
	 * The command can process the input line-by-line and write to the output
	 * when processing a single line, but it is a requirement that all available
	 * input lines have been read when returning from the command's execute
	 * method.
	 */
	LineByLine,
	/**
	 * The command processes its input as a whole. It is at the same time a
	 * contractual obligation for the command to read the complete input passed
	 * to the command's execute method.
	 */
	CompleteInput;

	/**
	 * Convenience method returning true if this constant equals
	 * {@link #NoInput}.
	 * 
	 * @return true if {@code this==NoInput}
	 */
	public boolean isNoInput() {
		return NoInput.equals(this);
	}

	/**
	 * Convenience method returning true if this constant equals
	 * {@link #LineByLine}.
	 * 
	 * @return true if {@code this==LineByLine}
	 */
	public boolean isLineByLine() {
		return LineByLine.equals(this);
	}

	/**
	 * Convenience method returning true if this constant equals
	 * {@link #CompleteInput}.
	 * 
	 * @return true if {@code this==CompleteInput}
	 */
	public boolean isCompleteInput() {
		return CompleteInput.equals(this);
	}

}
