package org.unix4j.util;

public class Assert {

	/**
	 * Returns the given {@code value} if it is not null and throws an exception
	 * otherwise.
	 * 
	 * @param message
	 *            the error message used if {@code value} is null
	 * @param value
	 *            the value to assert
	 * @return the given {@code value} if it is not null
	 * @throws NullPointerException
	 *             if {@code value==null}
	 */
	public static <T> T assertNotNull(String message, T value) {
		if (value != null) {
			return value;
		}
		throw new NullPointerException(message);
	}

	public static void assertArgNotNull(final String message, final Object obj) {
		if (obj == null) {
			throw new NullPointerException(message);
		}
	}

	public static void assertArgNotNull(final String message, final Object... objects) {
		for (Object obj : objects) {
			assertArgNotNull(message, obj);
		}
	}

	public static void assertArgTrue(final String message, boolean expression) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgGreaterThan(final String message, int argValue, int num) {
		if (argValue <= num) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgGreaterThanOrEqualTo(final String message, int argValue, int num) {
		if (argValue < num) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgLessThan(final String message, int argValue, int num) {
		if (argValue >= num) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgLessThanOrEqualTo(final String message, int argValue, int num) {
		if (argValue > num) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void assertArgFalse(final String message, boolean expression) {
		if (expression) {
			throw new IllegalArgumentException(message);
		}
	}
}
