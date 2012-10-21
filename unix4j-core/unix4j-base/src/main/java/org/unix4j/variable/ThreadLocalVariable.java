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
	 * Constructor with name and type, initializing the variable with a null
	 * value.
	 * 
	 * @param name
	 *            the variable name
	 * @param type
	 *            the variable and value type
	 */
	public ThreadLocalVariable(String name, Class<V> type) {
		this(name, type, new ThreadLocal<V>());
	}

	/**
	 * Constructor with name, type and initial value for the variable. Every
	 * thread will get the provided value as default initial variable value,
	 * independent from the thread that creates this variable. 
	 * <p>
	 * If the initial value is not constant, it is recommended to use
	 * {@link #ThreadLocalVariable(String, Class, ThreadLocal)} to create a new
	 * variable instance.
	 * 
	 * @param name
	 *            the variable name
	 * @param type
	 *            the variable and value type
	 * @param value
	 *            the initial value for the variable
	 */
	public ThreadLocalVariable(String name, Class<V> type, final V value) {
		this(name, type, new ThreadLocal<V>() {
			@Override
			protected V initialValue() {
				return value;
			}
		});
	}

	/**
	 * Constructor with name, type and backing thread-local to hold the value.
	 * This constructor is for instance useful if the initial value is not a 
	 * constant value, which usually means that the {@code initialValue()} 
	 * method of the given {@code ThreadLocal} value has been overridden.
	 * 
	 * @param name
	 *            the variable name
	 * @param type
	 *            the variable and value type
	 * @param value
	 *            the initial value for the variable
	 */
	public ThreadLocalVariable(String name, Class<V> type, ThreadLocal<V> value) {
		super(name, type);
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
