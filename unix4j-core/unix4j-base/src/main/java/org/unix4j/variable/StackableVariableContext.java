package org.unix4j.variable;

import java.util.LinkedList;

import org.unix4j.convert.ValueConverter;

public class StackableVariableContext implements VariableContext {
	
	private final LinkedList<VariableContext> stack = new LinkedList<VariableContext>();
	
	public StackableVariableContext push() {
		stack.add(new DefaultVariableContext());
		return this;
	}
	
	public StackableVariableContext pop() {
		if (!stack.isEmpty()) {
			stack.removeLast();
			return this;
		}
		throw new IllegalStateException("stack is empty");
	}
	
	public VariableContext peek() {
		if (!stack.isEmpty()) {
			return stack.getLast();
		}
		throw new IllegalStateException("stack is empty");
	}
	
	public VariableContext removeAllStackFrames() {
		stack.clear();
		return this;
	}
	
	@Override
	public Object setValue(String name, Object value) {
		return peek().setValue(name, value);
	}

	@Override
	public Object getValue(String name) {
		return peek().getValue(name);
	}

	@Override
	public <V> V getValue(String name, ValueConverter<V> converter) throws IllegalArgumentException {
		return peek().getValue(name, converter);
	}

}
