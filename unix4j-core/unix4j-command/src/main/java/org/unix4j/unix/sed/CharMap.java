package org.unix4j.unix.sed;

import java.util.HashMap;
import java.util.Map;

/**
 * A map from char to char. The mappings for the most common 256 source
 * characters are simply kept in a char array. For all other more exotic source
 * characters, a proper hash map is used to store the mapping.
 */
class CharMap {

	private final char[] map = new char[256];
	private final Map<Character, Character> extendedMap = new HashMap<Character, Character>();

	/**
	 * Creates a char map with the given source and destination characters. If
	 * the strings do not have the same length, subsequent characters in the
	 * longer string are ignored. The first mapping is defined as
	 * {@code source[0] --> destination[0]}, all other mappings in an analogous
	 * way with matching character indices in the two strings.
	 * 
	 * @param source
	 *            source characters
	 * @param destination
	 *            destination characters
	 * @throws IllegalArgumentException
	 *             if any of the destination characters is the zero character
	 */
	public CharMap(String source, String destination) {
		add(source, destination);
	}

	/**
	 * Adds the given source and destination characters to this char map.. If
	 * the strings do not have the same length, subsequent characters in the
	 * longer string are ignored. The first mapping is defined as
	 * {@code source[0] --> destination[0]}, all other mappings in an analogous
	 * way with matching character indices in the two strings.
	 * 
	 * @param source
	 *            source characters
	 * @param destination
	 *            destination characters
	 * @throws IllegalArgumentException
	 *             if any of the destination characters is the zero character
	 */
	public void add(String source, String destination) {
		final int len = Math.min(source.length(), destination.length());
		for (int i = 0; i < len; i++) {
			add(source.charAt(i), destination.charAt(i));
		}
	}

	/**
	 * Adds the given mapping {@code source --> destination} to this char map.
	 * 
	 * @param source
	 *            source character
	 * @param destination
	 *            destination character
	 * @throws IllegalArgumentException
	 *             if the destination character is the zero character
	 */
	public void add(char source, char destination) {
		if (destination == 0) {
			throw new IllegalArgumentException("cannot map to zero character");
		}
		if (source < 256) {
			map[source] = destination;
		} else {
			extendedMap.put(source, destination);
		}
	}

	public char map(char source) {
		if (source < 256) {
			return map[source];
		}
		final Character mapped = extendedMap.get(source);
		return mapped == null ? 0 : mapped.charValue();
	}
}
