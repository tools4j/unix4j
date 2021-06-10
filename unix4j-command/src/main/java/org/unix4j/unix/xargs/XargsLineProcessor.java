package org.unix4j.unix.xargs;

import org.unix4j.context.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.variable.VariableContext;

class XargsLineProcessor extends AbstractLineProcessor<XargsArguments> {

	private final Itemizer itemizer;
	
	private final DefaultItemStorage storage;
	
	public XargsLineProcessor(XargsCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, new XargsOutput(output));
		final XargsArguments args = getArguments();
		if (args.isDelimiterSet()) {
			final String delimiter = args.getDelimiter();
			if (delimiter.length() == 1) {
				itemizer = new CharDelimitedItemizer(delimiter.charAt(0));
			} else {
				//FIXME support length>1 delimiters
				throw new IllegalArgumentException("unsupported delimiter: " + delimiter);
			}
		} else if (args.isDelimiter0()) {
			itemizer = new CharDelimitedItemizer(Line.ZERO);
		} else {
			itemizer = new WhitespaceItemizer();
		}
		this.storage = new DefaultItemStorage(this);
		getVariableContext().addVariableResolver(storage);
	}
	
	@Override
	protected XargsCommand getCommand() {
		return (XargsCommand)super.getCommand();
	}
	
	@Override
	protected XargsArguments getArguments() {
		return super.getArguments();
	}
	
	@Override
	protected XargsOutput getOutput() {
		return (XargsOutput)super.getOutput();
	}
	
	protected VariableContext getVariableContext() {
		return getContext().getVariableContext();
	}
	
	protected void invoke() {
		final XargsOutput output = getOutput();
		final LineProcessor invocation = getCommand().getInvokedCommand().execute(getContext(), output);
		invocation.finish();
	}

	@Override
	public boolean processLine(Line line) {
		itemizer.itemizeLine(line, storage);
		return true;//we always want all the lines
	}
	
	@Override
	public void finish() {
		itemizer.finish(storage);
		storage.flush();
		getVariableContext().removeVariableResolver(storage);
		getOutput().finishAll();
	}
}
