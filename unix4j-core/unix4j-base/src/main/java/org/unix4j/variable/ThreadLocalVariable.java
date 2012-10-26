package org.unix4j.variable;

/**
 * Thread-local implementation of a {@link Variable}. A thread-local value can
 * change over time, but the change only affects the same thread. Every thread
 * deals with its own thread-local copy of the variable value.
 * 
 * @param <V>
 *            the type of the value
 *            
 * @see ThreadLocal
 */
public class ThreadLocalVariable<V> extends AbstractNamedValue<V> implements Variable<V> {

	private final ThreadLocal<V> value;

	/**
	 * Constructor with name initializing the variable with a null value.
	 * 
	 * @param name
	 *            the variable name
	 */
	public ThreadLocalVariable(String name) {
		this(name, new ThreadLocal<V>());
	}

	/**
	 * Constructor with name and initial value for the variable. Every thread 
	 * will get the provided value as default initial variable value, 
	 * independent from the thread that creates this variable. 
	 * <p>
	 * If the initial value is not constant, it is recommended to use
	 * {@link #ThreadLocalVariable(String, ThreadLocal)} to create a new
	 * variable instance.
	 * 
	 * @param name
	 *            the variable name
	 * @param value
	 *            the initial value for the variable
	 */
	public ThreadLocalVariable(String name, final V value) {
		this(name, new ThreadLocal<V>() {
			@Override
			protected V initialValue() {
				return value;
			}
		});
	}

	/**
	 * Constructor with name and backing thread-local to hold the value. This 
	 * constructor is for instance useful if the initial value is not a constant 
	 * value, which usually means that the {@code initialValue()} method of the 
	 * given {@code ThreadLocal} value has been overridden.
	 * 
	 * @param name
	 *            the variable name
	 * @param value
	 *            the initial value for the variable
	 */
	public ThreadLocalVariable(String name, ThreadLocal<V> value) {
		super(name);
		this.value = value;
	}

	/**
	 * Returns the value currently associated with this variable.
	 * 
	 * @return the variable value
	 */
	@Override
	public V getValue() {
		return value.get();
	}

	@Override
	public void setValue(V value) {
		this.value.set(value);
	}

}
