package org.unix4j.processor;

import java.util.List;

import org.unix4j.io.Input;
import org.unix4j.line.Line;

/**
 * A line processor for multiple inputs processing the same operation for each
 * input object individually. An operation here is another {@link LineProcessor}
 * reading the lines passed to it from the standard input.
 * <p>
 * The {@link #processLine(Line)} method does nothing and returns false
 * indicating that the (standard) input is not read by this processor. The
 * {@link #finish()} method reads the lines from the {@link Input} object passed
 * to the constructor and passes them as input to the delegate processor
 * performing the real work.
 */
public class MultipleInputLineProcessor implements LineProcessor {

	private final List<? extends Input> inputs;
	private final InputProcessor processor;
	private final LineProcessor output;

	/**
	 * Constructor with input objects (usually file operands of the command) and
	 * the input processor of the command that reads from the standard input.
	 * 
	 * @param inputs
	 *            the input devices, usually file operands of the command
	 * @param processor
	 *            the operation applied to every input in the given
	 *            {@code inputs} list
	 */
	public MultipleInputLineProcessor(List<? extends Input> inputs, InputProcessor processor, LineProcessor output) {
		this.inputs = inputs;
		this.processor = processor;
		this.output = output;
	}

	@Override
	public boolean processLine(Line line) {
		return false;// we want no input, we have it already
	}

	/**
	 * Performs the following operations to process all {@code Input} objects
	 * that have been passed to the constructor:
	 * <ol>
	 * <li>Calls {@link #beginMultiple(List, LineProcessor) beginMultiple(..)}</li>
	 * <li>Iterates over all input objects in sequence</li>
	 * <li>Calls {@link InputProcessor#begin(Input, LineProcessor)}</li>
	 * <li>Calls {@link InputProcessor#processLine(Input, Line, LineProcessor)}
	 * for every line in the current input</li>
	 * <li>Calls {@link InputProcessor#finish(Input, LineProcessor)}</li>
	 * <li>Calls {@link #finishMultiple(List, LineProcessor) finishMultiple(..)}
	 * </li>
	 * </ol>
	 */
	@Override
	public void finish() {
		beginMultiple(inputs, output);
		for (int i = 0; i < inputs.size(); i++) {
			final Input input = inputs.get(i);
			processor.begin(input, output);
			for (final Line line : input) {
				if (!processor.processLine(input, line, output)) {
					break;// wants no more lines
				}
			}
			processor.finish(input, output);
		}
		finishMultiple(inputs, output);
	}

	/**
	 * Called once at the beginning before iterating over the {@link Input}
	 * objects in the given {@code inputs} list.
	 * <p>
	 * The DEFAULT implementation performs no operation.
	 * 
	 * @param inputs
	 *            the input object being iterated next
	 * @param output
	 *            the output to write to
	 */
	protected void beginMultiple(List<? extends Input> inputs, LineProcessor output) {
		// default: no op
	}

	/**
	 * Called once at the end after iterating over the {@link Input} objects in
	 * the given {@code inputs} list.
	 * <p>
	 * The DEFAULT implementation calls {@link LineProcessor#finish()
	 * output.finish()}.
	 * 
	 * @param inputs
	 *            the input object being iterated next
	 * @param output
	 *            the output to write to
	 */
	protected void finishMultiple(List<? extends Input> inputs, LineProcessor output) {
		output.finish();
	}

}