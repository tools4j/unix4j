package org.unix4j.variable;

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
	
	private static final VariableContext EMPTY_VARIABLE_CONTEXT = new VariableContext() {
		@Override
		public Object setValue(String name, Object value) {
			//should never be called
			throw new IllegalStateException("empty variable constant is not modifiable");
		}
		@Override
		public <V> V getValue(String name, ValueConverter<V> converter) throws IllegalArgumentException {
			return null;
		}
		@Override
		public Object getValue(String name) {
			return null;
		}
	};
	
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
	
	public VariableContext peek(boolean fail) {
		if (!stack.isEmpty()) {
			return stack.getLast();
		}
		if (fail) {
			throw new IllegalStateException("stack is empty [hint: push a new variable context before storing variables]");
		}
		return EMPTY_VARIABLE_CONTEXT;
	}
	
	public VariableContext removeAllStackFrames() {
		stack.clear();
		return this;
	}
	
	@Override
	public Object setValue(String name, Object value) {
		return peek(true).setValue(name, value);
	}

	@Override
	public Object getValue(String name) {
		return peek(false).getValue(name);
	}

	@Override
	public <V> V getValue(String name, ValueConverter<V> converter) throws IllegalArgumentException {
		return peek(false).getValue(name, converter);
	}

}
