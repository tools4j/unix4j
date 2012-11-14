package org.unix4j.context;

import java.util.LinkedList;

import org.unix4j.convert.ValueConverter;

/**
 * A stackable variable context supports variables with different scopes. A new
 * scope can by {@link #push() pushed} to the stack hiding all previous 
 * variables. {@link #pop() Popping} that layer later on removes all variables
 * of that context and the variables of the underlying layer become visible
 * again. 
 * <p>
 * A stackable variable context can be compared to the variables on the
 * execution stack of nested method invocations. It is useful when a command 
 * wants to use an own variable context but still preserve the variables of
 * previously executed commands. 
 */
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
