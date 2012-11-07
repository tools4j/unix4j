package org.unix4j.unix.xargs;

import org.unix4j.variable.VariableContext;

public class VariableContextItemStorage implements ItemStorage {
	
	private final VariableContext context;
	private int itemCount;
	
	public VariableContextItemStorage(VariableContext context) {
		this.context = context;
	}

	@Override
	public void storeItem(String item) {
		context.setValue(Xarg.arg(itemCount), item);
		itemCount++;
	}

	@Override
	public int size() {
		return itemCount;
	}
	
	public void reset() {
		while (itemCount > 0) {
			itemCount--;
			context.setValue(Xarg.arg(itemCount), null);//set null clears the variable
		}
	}

}
