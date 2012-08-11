package org.unix4j.util;

/**
 * Utility class to evaluate the {@link StackTraceElement} corresponding to a
 * calling method in the caller stack.
 */
public class StackTraceUtil {

	/**
	 * Returns the stack trace element corresponding to the direct caller
	 * method, that is, the method which calls this method.
	 */
	public static StackTraceElement getCurrentMethodStackTraceElement() {
		return internalGetCurrentMethodStackTraceElement(1);
	}

	/**
	 * Returns the stack trace element belonging to the
	 * <code>callerOffset<sup>th</sup></code> caller of the calling method.
	 * <p>
	 * For example, {@code callerOffset=0} returns the stack trace element of
	 * the direct caller of this method; {@code callerOffset=1} corresponds to
	 * the method calling the direct caller method, etc.
	 * 
	 * @param callerOffset
	 *            the index of the stack trace element in the caller stack,
	 *            where {@code callerOffset=0} corresponds to the direct caller
	 *            of this method
	 */
	public static StackTraceElement getCurrentMethodStackTraceElement(int callerOffset) {
		return internalGetCurrentMethodStackTraceElement(callerOffset + 1);
	}

	private static StackTraceElement internalGetCurrentMethodStackTraceElement(int callerOffset) {
		// trc[0] Thread.dumpThreads(native)
		// trc[1] Thread.getStackTrace(..)
		// trc[2] StackTrace.internalGetCurrentMethodStackTraceElement(..)
		// trc[3] is what we want for offset == 0!

		// NOTE sometimes also
		// trc[0] Thread.getStackTrace(..)
		// trc[1] StackTrace.internalGetCurrentMethodStackTraceElement(..)
		// trc[2] is what we want for offset == 0!
		StackTraceElement[] trc = Thread.currentThread().getStackTrace();
		for (int i = 0; i < trc.length; i++) {
			if (trc[i].getClassName().equals(StackTraceUtil.class.getName()) && trc[i].getMethodName().equals("internalGetCurrentMethodStackTraceElement")) {
				return trc[callerOffset + i + 1];
			}
		}
		throw new RuntimeException("internal error: current method not found");
	}

	// no instances
	private StackTraceUtil() {
	}
}