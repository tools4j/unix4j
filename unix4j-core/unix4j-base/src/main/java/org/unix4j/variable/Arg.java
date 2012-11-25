package org.unix4j.variable;

/**
 * The Arg class defines variable name constants for command arguments.
 */
public class Arg {
	/**
	 * Name for the variable referring to the first command line argument (zero
	 * based index)
	 */
	public static final String $0 = "$0";
	/**
	 * Name for the variable referring to the second command line argument (zero
	 * based index)
	 */
	public static final String $1 = "$1";
	/**
	 * Name for the variable referring to the third command line argument (zero
	 * based index)
	 */
	public static final String $2 = "$2";
	/**
	 * Name for the variable referring to the fourth command line argument (zero
	 * based index)
	 */
	public static final String $3 = "$3";
	/**
	 * Name for the variable referring to the fifth command line argument (zero
	 * based index)
	 */
	public static final String $4 = "$4";
	/**
	 * Name for the variable referring to the sixth command line argument (zero
	 * based index)
	 */
	public static final String $5 = "$5";
	/**
	 * Name for the variable referring to the seventh command line argument
	 * (zero based index)
	 */
	public static final String $6 = "$6";
	/**
	 * Name for the variable referring to the eighth command line argument (zero
	 * based index)
	 */
	public static final String $7 = "$7";
	/**
	 * Name for the variable referring to the ninth command line argument (zero
	 * based index)
	 */
	public static final String $8 = "$8";
	/**
	 * Name for the variable referring to the tenth command line argument (zero
	 * based index)
	 */
	public static final String $9 = "$9";
	/** 
	 * Name for the variable referring to all command line arguments. 
	 */
	public static final String $args = "$*";

	/** 
	 * Name for the variable referring to all command line arguments.
	 * @return {@link #$args}
	 */
	public static final String args() {
		return $args;
	}

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
		return "$" + i;
	}

	// no instances
	private Arg() {
		super();
	}
}
