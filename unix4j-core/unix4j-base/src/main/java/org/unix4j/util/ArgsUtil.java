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

	public static final String KEY_OPTIONS = "options";
	public static final String KEY_DEFAULT_OPERAND = "default";

	/**
	 * Returns a map with the options and operands. Operands are found in the
	 * returned map by operand name without the leading "--" prefix; the operand
	 * values are the values in the list. If operand values are provided without
	 * an operand name, they are returned in the map under the
	 * {@link #KEY_DEFAULT_OPERAND} key.
	 * <p>
	 * Options are stored in the return map under the {@link #KEY_OPTIONS} key
	 * with the option long or short names as values in the list. Option long
	 * names are added to the list without the leading "--" and short names
	 * without the leading single dash "-".
	 * <p>
	 * The argument "--" is accepted as a delimiter indicating the end of
	 * options and named operands. Any following arguments are treated as
	 * default operands returned in the map under the
	 * {@link #KEY_DEFAULT_OPERAND} key, even if they begin with the '-'
	 * character.
	 * <p>
	 * Sample strings that could be passed as arguments to the echo command:
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
	 * Sample strings that could be passed as arguments to the ls command:
	 * 
	 * <pre>
	 * "-lart"                           --> {"options":["l", "a", "r", "t"]}
	 * "-laR" "--files" "*.txt" "*.log"  --> {"options":["l", "a", "R"], "files":["*.txt", "*.log"]}
	 * "-a" "--longFormat" "--files" "*" --> {"options":["a", "longFormat"], "files":["*"]}
	 * "-laR" "*.txt" "*.log"            --> {"options":["l", "a", "R"], "default":["*.txt", "*.log"]}
	 * "-la" "--" "-*" "--*"             --> {"options":["l", "a"], "default":["-*", "--*"]}
	 * </pre>
	 * 
	 * @param args
	 *            the arguments to be parsed
	 * @return the operands and options in a map with operand names as keys and
	 *         operand values as values plus the special key "options" with all
	 *         found option short/long names as values
	 */
	public static final Map<String, List<String>> parseArgs(String... args) {
		final Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		boolean allOperands = false;
		String name = null;
		List<String> values = null;
		for (int i = 0; i < args.length; i++) {
			final String arg = args[i];
			if (allOperands) {
				add(map, KEY_DEFAULT_OPERAND, arg);
			} else {
				if (arg.startsWith("--")) {
					add(map, name, values);
					if (arg.length() == 2) {
						// delimiter, all coming args are default operands
						allOperands = true;
						name = null;
						values = null;
					} else {
						// operand name or option long name
						name = arg.substring(2);// cut off the dashes --
						values = null;
					}
				} else if (arg.startsWith("-")) {
					// a short option name string
					add(map, name, values);
					final int len = arg.length();
					for (int j = 1; j < len; j++) {
						add(map, KEY_OPTIONS, "" + arg.charAt(j));
					}
					name = null;
					values = null;
				} else {
					// an operand value
					if (name == null) {
						name = KEY_DEFAULT_OPERAND;
					}
					if (values == null) {
						values = new ArrayList<String>(2);
					}
					values.add(arg);
				}
			}
		}
		add(map, name, values);
		return map;
	}

	/**
	 * Adds the given value to the list in the map if it exist for the specified
	 * key, and to a new list added to the map if it does not exist yet
	 */
	private static void add(Map<String, List<String>> map, String key, String value) {
		List<String> values = map.get(key);
		if (values == null) {
			map.put(key, values = new ArrayList<String>());
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
	private static void add(Map<String, List<String>> map, String key, List<String> values) {
		if (key != null) {
			if (values == null) {
				// an option long name
				add(map, KEY_OPTIONS, key);
			} else {
				// an operand
				List<String> old = map.get(key);
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
