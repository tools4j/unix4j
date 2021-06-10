package org.unix4j.variable;

/**
 * The Arg class defines variable name constants for command arguments.
 */
public class Arg {
	private static final String PREFIX = "$";
	private static final String ALL = "$@";
	private static final String FROM_PREFIX = "$@+";

	/**
	 * Name for the variable referring to the first command line argument (zero
	 * based index)
	 */
	public static final String $0 = arg(0);
	/**
	 * Name for the variable referring to the second command line argument (zero
	 * based index)
	 */
	public static final String $1 = arg(1);
	/**
	 * Name for the variable referring to the third command line argument (zero
	 * based index)
	 */
	public static final String $2 = arg(2);
	/**
	 * Name for the variable referring to the fourth command line argument (zero
	 * based index)
	 */
	public static final String $3 = arg(3);
	/**
	 * Name for the variable referring to the fifth command line argument (zero
	 * based index)
	 */
	public static final String $4 = arg(4);
	/**
	 * Name for the variable referring to the sixth command line argument (zero
	 * based index)
	 */
	public static final String $5 = arg(5);
	/**
	 * Name for the variable referring to the seventh command line argument
	 * (zero based index)
	 */
	public static final String $6 = arg(6);
	/**
	 * Name for the variable referring to the eighth command line argument (zero
	 * based index)
	 */
	public static final String $7 = arg(7);
	/**
	 * Name for the variable referring to the ninth command line argument (zero
	 * based index)
	 */
	public static final String $8 = arg(8);
	/**
	 * Name for the variable referring to the tenth command line argument (zero
	 * based index)
	 */
	public static final String $9 = arg(9);
	/**
	 * Name for the variable referring to all command line arguments.
	 */
	public static final String $all = ALL;

	/**
	 * Name for the variable referring to the <code>(i+1)<sup>th</sup></code>
	 * command line argument (the index {@code i} is zero based).
	 * 
	 * @param i
	 *            zero based argument index
	 * @return variable name for the <code>(i+1)<sup>th</sup></code> command
	 *         line argument
	 */
	public static final String arg(int i) {
		return PREFIX + i;
	}

	/**
	 * Returns true if and only if the given name starts with the "$" prefix.
	 * 
	 * @param expression
	 *            the potential variable name to check
	 * @return if the first character of expression is a "$" character
	 */
	public static boolean isVariable(String expression) {
		return expression.startsWith(PREFIX);
	}

	/**
	 * Returns the zero based index of an arg variable given the name of the
	 * variable. This method is the antipode of the {@code #arg(int)} method, it
	 * returns -1 if the given name is not compatible with the name returned by
	 * {@link #arg(int)}.
	 * 
	 * @param name
	 *            the name of the variable, such as "$0", "$1" etc.
	 * @return the index of the argument referenced by the variable, for
	 *         instance 0 for the name "$0"
	 */
	public static int argIndex(String name) {
		if (name.startsWith(PREFIX)) {
			try {
				return Integer.parseInt(name.substring(PREFIX.length()));
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		return -1;
	}

	/**
	 * Returns the zero based start index of an args variable given the name of
	 * the variable. This method is the antipode of the {@code #argsFrom(int)}
	 * method, it returns -1 if the given name is not compatible with the name
	 * returned by {@link #argsFrom(int)}.
	 * 
	 * @param name
	 *            the name of the variable, such as "$@+1", "$@+3" etc.
	 * @return the index of the argument referenced by the variable, for
	 *         instance 1 for the name "$@+1"
	 */
	public static int argsFromIndex(String name) {
		if (name.startsWith(FROM_PREFIX)) {
			try {
				return Integer.parseInt(name.substring(FROM_PREFIX.length()));
			} catch (NumberFormatException e) {
				return -1;
			}
		}
		return -1;
	}

	/**
	 * Name for the variable referring to all command line arguments.
	 * 
	 * @return {@link #$all}
	 */
	public static final String args() {
		return $all;
	}

	/**
	 * Name for the variable referring to all command line arguments starting
	 * from the specified {@code index}.
	 * 
	 * @param index
	 *            zero based index of first argument in the args list
	 * @return variable name for the <code>(index+1)<sup>th</sup></code> command
	 *         line argument followed by all other arguments
	 */
	public static final String argsFrom(int index) {
		if (index < 0)
			throw new IllegalArgumentException("from cannot be negative: " + index);
		return index == 0 ? $all : (FROM_PREFIX + index);
	}

	// no instances
	private Arg() {
		super();
	}
}
