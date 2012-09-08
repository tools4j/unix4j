package org.unix4j.processor;

import java.util.Collections;
import java.util.List;

import org.unix4j.io.Input;

/**
 * A line processor redirects a given {@link Input} stream to the standard input
 * of a command. The command here is another {@link LineProcessor} reading the
 * lines passed to it. If a command supports both (a) reading from the standard
 * input and (b) input from a file, it implements a processor for (a) and reuses
 * this processor for (b) nested in a {@code RedirectInputLineProcessor}.
 * <p>
 * The (standard) input is not read by this processor. See superclass
 * {@link MultipleInputLineProcessor} for more information.
 */
public class RedirectInputLineProcessor extends MultipleInputLineProcessor {

	/**
	 * Constructor with input object (usually a file operand of the command) and
	 * the command processor that reads from the standard input.
	 * 
	 * @param input
	 *            the input device, usually a file operand of the command
	 * @param standardInputProcessor
	 *            the processor performing the command operation based on lines
	 *            from the standard input
	 */
	public RedirectInputLineProcessor(Input input, LineProcessor standardInputProcessor) {
		this(Collections.singletonList(input), standardInputProcessor);
	}

	/**
	 * Constructor with multiple input objects (usually file operands of the
	 * command) and the command processor that reads from the standard input.
	 * The given input objects are processed in the given sequence; the
	 * {@code finish()} method of the {@code standardInputProcessor} is called
	 * after completing the last line of the last input.
	 * 
	 * @param inputs
	 *            the input devices, usually file operands of the command
	 * @param standardInputProcessor
	 *            the processor performing the command operation based on lines
	 *            from the standard input
	 */
	public RedirectInputLineProcessor(List<? extends Input> inputs, LineProcessor standardInputProcessor) {
		super(inputs, new DefaultInputProcessor(), standardInputProcessor);
	}

}