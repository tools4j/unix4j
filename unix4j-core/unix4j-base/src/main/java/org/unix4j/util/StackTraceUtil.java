package org.unix4j.util;

/**
 * The <code>StackTraceUtil</code> class determines the
 * {@link StackTraceElement} of any of the calling methods.
 */
public class StackTraceUtil {

	/**
	 * Returns the stack trace element belonging to the direct caller method,
	 * that is the method which invokes this method.
	 */
	public static StackTraceElement getCurrentMethodStackTraceElement() {
		return internalGetCurrentMethodStackTraceElement(1);
	}

	/**
	 * Returns the stack trace element belonging to the
	 * <code>callerOffset<code><sup>th</sup> caller of the calling 
	 * method.
	 * 
	 * @param callerOffset
	 *            The index of the caller of the method which invokes this
	 *            method
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