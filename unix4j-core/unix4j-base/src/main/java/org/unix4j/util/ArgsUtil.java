package org.unix4j.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides static utility methods to parse options and operands of a command
 * passed to the command as a string vararg parameter.
 */
public class ArgsUtil {

	/**
	 * Returns a map with the options and operands. Operands are found in the
	 * returned map by operand name without the leading "--" prefix; the operand
	 * values are the values in the list. If operand values are provided without
	 * an operand name, they are returned in the map using the specified
	 * {@code defaultKey}.
	 * <p>
	 * Options are stored in the return map under the {@code optionsKey} with
	 * the option long or short names as values in the list. Option long names
	 * are added to the list without the leading "--" and short names without
	 * the leading single dash "-".
	 * <p>
	 * The argument "--" is accepted as a delimiter indicating the end of
	 * options and named operands. Any following arguments are treated as
	 * default operands returned in the map under the {@code defaultKey}, even
	 * if they begin with the '-' character.
	 * <p>
	 * String args that could be passed as arguments to the {@code echo} command, 
	 * assuming {@code optionsKey="options"} and {@code defaultKeys=["default"]}:
	 * 
	 * <pre>
	 * "--message" "hello" "world"        --> {"message":["hello", "world"]}
	 * "-n --message" "hello" "world"     --> {"message":["hello", "world"], "options":["n"]}
	 * "--noNewline --message" "ping"     --> {"message":["ping"], "options":["noNewline"]}
	 * "hello" "world"                    --> {"default":["hello", "world"]}
	 * "-n" "hello" "world"               --> {"default":["hello", "world"], "options":"n"}
	 * "--noNewline" "--" "hello" "world" --> {"default":["hello", "world"], "options":"noNewline"}
	 * "--" "8" "-" "7" "=" "1"           --> {"default":["8", "-", "7", "=", "1"}
	 * "--" "8" "--" "7" "=" "15"         --> {"default":["8", "--", "7", "=", "15"}
	 * </pre>
	 * <p>
	 * String args that could be passed as arguments to the {@code ls} command, 
	 * again with {@code optionsKey="options"} and {@code defaultKeys=["default"]}:
	 * 
	 * <pre>
	 * "-lart"                           --> {"options":["l", "a", "r", "t"]}
	 * "-laR" "--files" "*.txt" "*.log"  --> {"options":["l", "a", "R"], "files":["*.txt", "*.log"]}
	 * "-a" "--longFormat" "--files" "*" --> {"options":["a", "longFormat"], "files":["*"]}
	 * "-laR" "*.txt" "*.log"            --> {"options":["l", "a", "R"], "default":["*.txt", "*.log"]}
	 * "-la" "--" "-*" "--*"             --> {"options":["l", "a"], "default":["-*", "--*"]}
	 * </pre>
	 * <p>
	 * String args that could be passed as arguments to the {@code grep} command
	 * which has two default operands, hence {@code optionsKey="options"} and 
	 * {@code defaultKeys=["pattern","paths"]}:
	 * 
	 * <pre>
	 * "myword" "myfile.txt"                       --> {"pattern":["myword"], paths:["myfile.txt"]}
	 * "-i" "myword" "myfile.txt"                  --> {"options":["i"], "pattern":["myword"], paths:["myfile.txt"]}
	 * "-i" "error" "*.txt" "*.log"                --> {"options":["i"], "pattern":["error"], paths:["*.txt", "*.log"]}
	 * "--ignoreCase" "--" "error" "*"             --> {"options":["ignoreCase"], "pattern":["error"], paths:["*"]}
	 * "--ignoreCase" "--pattern" "error" "--" "*" --> {"options":["ignoreCase"], "pattern":["error"], paths:["*"]}
	 * "-i" "error" "--paths" "*"                  --> {"options":["i"], "pattern":["error"], paths:["*"]}
	 * "--ignoreCase" "--paths" "*" "--" "error"   --> {"options":["ignoreCase"], "pattern":["error"], paths:["*"]}
	 * </pre>
	 * 
	 * @param optionsKey
	 *            the map key to use for options aka no-value operands
	 * @param defaultKeys
	 *            a list of map keys to use for operands when no operand name is
	 *            specified; only the last key can have multiple operand values
	 * @param args
	 *            the arguments to be parsed
	 * @return the operands and options in a map with operand names as keys and
	 *         operand values as values plus the special key "options" with all
	 *         found option short/long names as values
	 */
	public static final Map<String, List<Object>> parseArgs(String optionsKey, List<String> defaultKeys, Object... args) {
		final Map<String, List<Object>> map = new LinkedHashMap<String, List<Object>>();
		boolean allDefaultOperands = false;
		String name = null;
		List<Object> values = null;
		for (int i = 0; i < args.length; i++) {
			final Object arg = args[i];
			if (allDefaultOperands) {
				final String defaultKey = getDefaultKey(map, defaultKeys);
				add(map, defaultKey, arg);
			} else {
				boolean isOperandValue = true;
				if (arg instanceof String) {
					final String sarg = (String)arg;
					if (sarg.startsWith("--")) {
						isOperandValue = false;
						add(optionsKey, map, name, values);
						if (sarg.length() == 2) {
							// delimiter, all coming args are default operands
							allDefaultOperands = true;
							name = null;
							values = null;
						} else {
							// operand name or option long name
							name = sarg.substring(2);// cut off the dashes --
							values = null;
						}
					} else if (sarg.startsWith("-") && !isDigit(sarg, 2)) {
						isOperandValue = false;
						// a short option name string
						add(optionsKey, map, name, values);
						final int len = sarg.length();
						for (int j = 1; j < len; j++) {
							add(map, optionsKey, "" + sarg.charAt(j));
						}
						name = null;
						values = null;
					}
				}
				if (isOperandValue) {	
					// an operand value
					if (name == null) {
						final String defaultKey = getDefaultKey(map, defaultKeys);
						add(map, defaultKey, arg);
					}
					if (values == null) {
						values = new ArrayList<Object>(2);
					}
					values.add(arg);
				}
			}
		}
		add(optionsKey, map, name, values);
		return map;
	}
	
	private static boolean isDigit(String s, int pos) {
		return s.length() > pos && Character.isDigit(s.charAt(pos));
	}

	private static String getDefaultKey(Map<String, List<Object>> map, List<String> defaultKeys) {
		for (final String defaultKey : defaultKeys) {
			if (!map.containsKey(defaultKey)) {
				return defaultKey;
			}
		}
		return defaultKeys.get(defaultKeys.size() - 1);
	}

	/**
	 * Adds the given value to the list in the map if it exist for the specified
	 * key, and to a new list added to the map if it does not exist yet
	 */
	private static void add(Map<String, List<Object>> map, String key, Object value) {
		List<Object> values = map.get(key);
		if (values == null) {
			map.put(key, values = new ArrayList<Object>(2));
		}
		values.add(value);
	}

	/**
	 * If key is null, the method does nothing. Otherwise, if values is null,
	 * the key is an option name and therefore added to the map as an option. If
	 * values is not null, the key is an operand name and the values are added
	 * as operand values --- merged with existing operand values if there are
	 * any.
	 */
	private static void add(String optionsKey, Map<String, List<Object>> map, String key, List<Object> values) {
		if (key != null) {
			if (values == null) {
				// an option long name
				add(map, optionsKey, key);
			} else {
				// an operand
				List<Object> old = map.get(key);
				if (old == null) {
					map.put(key, values);
				} else {
					// merge
					old.addAll(values);
				}
			}
		}
	}

	// no instances
	private ArgsUtil() {
		super();
	}
}
