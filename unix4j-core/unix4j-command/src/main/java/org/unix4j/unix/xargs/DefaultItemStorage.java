package org.unix4j.unix.xargs;

import java.util.ArrayList;
import java.util.List;

import org.unix4j.variable.Arg;
import org.unix4j.variable.VariableContext;

class DefaultItemStorage implements ItemStorage {
	
	private final XargsLineProcessor processor;
	private final VariableContext variables;
	
	private final long maxLines;
	private final int maxArgs;
	private final boolean runWithoutItems;

	private int lineCount;
	private final List<String> items = new ArrayList<String>();
	
	public DefaultItemStorage(XargsLineProcessor processor) {
		this.processor = processor;
		this.variables = processor.getVariableContext();
		final XargsArguments args = processor.getArguments();
		this.maxLines = args.isMaxLinesSet() ? args.getMaxLines() : 1; 
		this.maxArgs = args.isMaxArgsSet() ? args.getMaxArgs() : Integer.MAX_VALUE; 
		this.runWithoutItems = !args.isNoRunIfEmpty(); 
	}

	@Override
	public void storeItem(String item) {
		final int index = items.size();
		variables.setValue(Arg.arg(index), item);
		items.add(item);
		if (items.size() >= maxArgs) {
			invokeCommandAndClearAllItems();
			lineCount = 0;
		}
	}

	@Override
	public void incrementLineCount() {
		lineCount++;
		if (lineCount >= maxLines) {
			if (runWithoutItems || !items.isEmpty()) {
				invokeCommandAndClearAllItems();
			}
			lineCount = 0;
		}
	}
	
	protected void flush() {
		if (lineCount > 0 && (runWithoutItems || !items.isEmpty())) {
			invokeCommandAndClearAllItems();
		}
		lineCount = 0;
	}
	
	private void invokeCommandAndClearAllItems() {
		variables.setValue(Arg.args(), items);
		processor.invoke();
		variables.setValue(Arg.args(), null);//set null clears the variable
		final int itemCount = items.size();
		for (int i = 0; i < itemCount; i++) {
			variables.setValue(Arg.arg(i), null);//set null clears the variable
		}
		items.clear();
	}
	
}
