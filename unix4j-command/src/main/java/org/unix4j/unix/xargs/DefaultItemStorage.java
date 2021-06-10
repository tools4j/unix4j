package org.unix4j.unix.xargs;

import org.unix4j.variable.Arg;
import org.unix4j.variable.VariableResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class DefaultItemStorage implements ItemStorage, VariableResolver {
	
	private final XargsLineProcessor processor;
//	private final VariableContext variables;
	
	private final long maxLines;
	private final int maxArgs;
	private final boolean isNoRunIfEmpty;

	private int lineCount;
	private final List<String> items = new ArrayList<String>();
	private int runCountForLine = 0;
	private int runCountOverall = 0;
	
	public DefaultItemStorage(XargsLineProcessor processor) {
		this.processor = processor;
		final XargsArguments args = processor.getArguments();
		this.maxLines = args.isMaxLinesSet() ? args.getMaxLines() : 1; 
		this.maxArgs = args.isMaxArgsSet() ? args.getMaxArgs() : Integer.MAX_VALUE;
		this.isNoRunIfEmpty = args.isNoRunIfEmpty();
	}

	@Override
	public void storeItem(String item) {
		items.add(item);
		if (items.size() >= maxArgs) {
			invokeCommandAndClearAllItems();
		}
	}

	@Override
	public void incrementLineCount() {
		lineCount++;
		if ((lineCount % maxLines) == 0 && (runCountForLine == 0 || !items.isEmpty())) {
			invokeCommandAndClearAllItems();
			runCountForLine = 0;
		}
	}
	
	protected void flush() {
		if ((lineCount % maxLines) != 0 && (runCountForLine == 0 || !items.isEmpty())) {
			invokeCommandAndClearAllItems();
		}
		if (runCountOverall == 0 && !isNoRunIfEmpty) {
			invokeCommandAndClearAllItems();
		}
		lineCount = 0;
		runCountForLine = 0;
		runCountOverall = 0;
	}
	
	private void invokeCommandAndClearAllItems() {
		processor.invoke();
		runCountForLine++;
		runCountOverall++;
		items.clear();
	}
	
	@Override
	public Object getValue(String name) {
		//(a) is variable a reference to all items?
		if (Arg.$all.equals(name)) {
			return Collections.unmodifiableList(items);
		}
		//(b) is variable a reference to one specific item?
		final int index = Arg.argIndex(name);
		if (index >= 0) {
			return index < items.size() ? items.get(index) : null;
		}
		//(c) is variable a reference to all items from a given index?
		final int from = Arg.argsFromIndex(name);
		if (from >= 0) {
			if (from < items.size()) {
				return Collections.unmodifiableList(from == 0 ? items : items.subList(from, items.size()));
			}
			return Collections.EMPTY_LIST;
		}
		//no, not a variable that we know
		return null;
	}
	
}
