package org.unix4j.unix.xargs;

import org.unix4j.command.ExecutionContext;
import org.unix4j.line.Line;
import org.unix4j.processor.AbstractLineProcessor;
import org.unix4j.processor.LineProcessor;
import org.unix4j.variable.StackableVariableContext;
import org.unix4j.variable.VariableContext;

class XargsLineProcessor extends AbstractLineProcessor<XargsArguments> {

	private final Itemizer itemizer;
	private final VariableContextItemStorage storage;
	private Line lastLine = null;
	
	public XargsLineProcessor(XargsCommand command, ExecutionContext context, LineProcessor output) {
		super(command, context, new XargsOutput(output));
		final XargsArguments args = getArguments();
		if (args.isDelimiterSet() || args.isDelimiter0()) {
			itemizer = new CharDelimitedItemizer(args);
		} else {
			itemizer = new WhitespaceItemizer(args);
		}
		this.storage = new VariableContextItemStorage(context.getVariableContext());
		pushVariableContext();
	}
	
	@Override
	protected XargsCommand getCommand() {
		return (XargsCommand)super.getCommand();
	}
	
	@Override
	protected XargsOutput getOutput() {
		return (XargsOutput)super.getOutput();
	}
	
	@Override
	public boolean processLine(Line line) {
		itemizeAndInvoke(line);
		return true;//we always want all the lines
	}
	
	@Override
	public void finish() {
		itemizeAndInvoke(null);
		popVariableContext();
		getOutput().finishAll();
	}
	
	private void pushVariableContext() {
		final VariableContext vcontext = getContext().getVariableContext();
		if (vcontext instanceof StackableVariableContext) {
			((StackableVariableContext)vcontext).push();
		}
	}
	private void popVariableContext() {
		final VariableContext vcontext = getContext().getVariableContext();
		if (vcontext instanceof StackableVariableContext) {
			((StackableVariableContext)vcontext).pop();
		}
	}

	private void itemizeAndInvoke(Line line) {
		if (lastLine != null) {
			if (itemizer.itemizeLine(lastLine, line == null, storage)) {
				final XargsOutput output = getOutput();
				final LineProcessor invocation = getCommand().getInvokedCommand().execute(getContext(), output);
				invocation.finish();
				storage.reset();
			}
		}
		lastLine = line;
	}

}
