package org.unix4j.command;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.unix4j.util.TypedMap;
import org.unix4j.util.TypedMap.Key;
import org.unix4j.util.Variables;

/**
 * Abstract {@link Arguments} object that may be subclassed by command specific
 * arguments. This class provides a {@link TypedMap} suitable to store value
 * arguments based on a key that defines the corresponding value type. It also
 * provides utility methods to set and query enum-based options.
 * 
 * @param <O>
 *            the enum defining the options (aka flags) for the command
 * @param <A>
 *            the concrete command specific arguments type
 */
abstract public class AbstractArgs<O extends Enum<O>, A extends AbstractArgs<O, A>> implements Arguments<A>, Cloneable {
	private TypedMap args;
	private EnumSet<O> opts;

	/**
	 * Constructor with enum class defining the options for the command
	 * 
	 * @param optClass
	 *            the enum type defining the options for the command
	 */
	public AbstractArgs(Class<O> optClass) {
		args = new TypedMap();
		opts = EnumSet.noneOf(optClass);
	}

	/**
	 * Sets the argument value for the specified key.
	 * 
	 * @param <T>
	 *            the argument value type
	 * @param key
	 *            the key (or name) for this argument value
	 * @param value
	 *            the value
	 */
	public <T> void setArg(TypedMap.Key<T> key, T value) {
		args.put(key, value);
	}

	/**
	 * Returns the argument value for the specified key, or null of no value has
	 * been set for the given key.
	 * 
	 * @param <T>
	 *            the argument value type
	 * @param key
	 *            the key (or name) for the queried argument value
	 * @return the value for the given key
	 */
	public <T> T getArg(TypedMap.Key<T> key) {
		return args.get(key);
	}

	/**
	 * Returns true if an argument value for the specified key has been set.
	 * 
	 * @param key
	 *            the key (or name) for the queried argument value
	 * @return true if a value for the given key has been set
	 */
	public boolean hasArg(TypedMap.Key<?> key) {
		return args.containsKey(key);
	}

	/**
	 * Returns the underlying modifiable typed map with the key/value pairs for
	 * all arguments.
	 * 
	 * @return the arguments as modifiable typed map
	 */
	public TypedMap getArgs() {
		return args;
	}

	/**
	 * Sets the specified option.
	 * 
	 * @param opt
	 *            the option to be set
	 */
	public void setOpt(O opt) {
		opts.add(opt);
	}

	public void setOpts(O... opts) {
		for (final O opt : opts) {
			setOpt(opt);
		}
	}

	/**
	 * Returns true if the given option is set.
	 * 
	 * @param opt
	 *            the option of interest
	 * @return true if the given option is set
	 */
	public boolean hasOpt(O opt) {
		return opts.contains(opt);
	}

	/**
	 * Returns the modifiable set with the option flags.
	 * 
	 * @return the modifiable options set.
	 */
	public Set<O> getOpts() {
		return opts;
	}

	@Override
	public void resolve(Map<String, String> variables) {
		for (final Map.Entry<Key<?>, ?> e : args.asMap().entrySet()) {
			if (String.class.equals(e.getKey().getValueType())) {
				@SuppressWarnings("unchecked")
				final Map.Entry<Key<String>, String> se = (Map.Entry<Key<String>, String>) (Object) e;
				final String value = se.getValue();
				if (Variables.isVariable(value)) {
					args.put(se.getKey(), Variables.resolve(value, variables));
				}
			} else if (List.class.equals(e.getKey().getValueType())) {
				final List<?> list = (List<?>) e.getValue();
				for (int i = 0; i < list.size(); i++) {
					final Object value = list.get(i);
					if (value instanceof String && Variables.isVariable(value.toString())) {
						@SuppressWarnings("unchecked")
						final List<String> stringList = (List<String>) list;
						stringList.set(i, Variables.resolve(value.toString(), variables));
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public A clone(boolean cloneDeep) {
		try {
			final A clone = (A) super.clone();
			clone.args = clone.args.clone(cloneDeep);
			clone.opts = clone.opts.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(getClass().getName() + " should be cloneable", e);
		}
	}

	@Override
	public String toString() {
		if (args.isEmpty() && opts.isEmpty()) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		for (Map.Entry<TypedMap.Key<?>, ?> e : args.asMap().entrySet()) {
			if (sb.length() > 0)
				sb.append(' ');
			sb.append("-").append(e.getKey().getKey());
			sb.append(" ").append(e.getValue());
		}
		for (final O opt : opts) {
			if (sb.length() > 0)
				sb.append(' ');
			sb.append("-").append(opt);
		}
		return sb.toString();
	}
}
