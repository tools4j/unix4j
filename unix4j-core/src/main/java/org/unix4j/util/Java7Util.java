package org.unix4j.util;

/**
 * Utility class with static methods to help loading a Java 7 version of a class
 * if it is available, and the java 6 version otherwise.
 */
public class Java7Util {

	public static final String JAVA7_CLASS_NAME_SUFFIX = "7";

	/**
	 * Returns a new instance of the Java 7 version of the given class if it
	 * exists and can be instantiated. Otherwise, the given
	 * {@code defaultInstance} is returned.
	 * <p>
	 * The Java 7 version of the class must be a subtype of {@code baseClass}
	 * and its fully qualified name must be identical to that of
	 * {@code baseClass} with a suffix "7". For instance, if the Java 6 base
	 * class is called "org.unix4j.MyClass", the Java 7 version must be called
	 * "org.unix4j.MyClass7". Furthermore, the class must have a default
	 * constructor with no arguments.
	 * <p>
	 * If the Java 7 version of the class is not found (usually because unix4j
	 * was compiled with a Java 6 compiler) or if the Java 7 class cannot be
	 * instantiated (usually because unix4j is run in a Java 6 JVM), the given
	 * {@code defaultInstance} is returned.
	 * 
	 * @param <T>
	 *            the generic return type
	 * @param baseClass
	 *            the base class and superclass of the Java 7 class
	 * @return a new instance of the Java 7 subtype if possible and
	 *         {@code defaultInstance} otherwise
	 */
	public static <T> T newInstance(Class<T> baseClass, T defaultInstance) {
		final Class<? extends T> clazz = loadClass(baseClass);
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			return defaultInstance;
		}
	}

	/**
	 * Returns the Java 7 version of the given class if it exists and can be
	 * loaded, and the given {@code baseClass} otherwise.
	 * <p>
	 * The Java 7 version of the class must be a subtype of {@code baseClass}
	 * and its fully qualified name must be identical to that of
	 * {@code baseClass} with a suffix "7". For instance, if the Java 6 base
	 * class is called "org.unix4j.MyClass", the Java 7 version must be called
	 * "org.unix4j.MyClass7".
	 * <p>
	 * If the Java 7 version of the class is not found (usually because unix4j
	 * was compiled with a Java 6 compiler), the given {@code defaultInstance}
	 * is returned. If the class is found but it is not a subtype of the given
	 * {@code baseClass}, an exception is thrown.
	 * 
	 * @param <T>
	 *            the generic type of the base class
	 * @param baseClass
	 *            the base class and superclass of the Java 7 class
	 * @return the Java 7 subclass if possible and {@code baseClass} otherwise
	 * @throws NullPointerException
	 *             if {@code baseClass} is null
	 * @throws IllegalArgumentException
	 *             if the Java 7 class is found but it is not a subclass of
	 *             {@code baseClass}
	 */
	public static <T> Class<? extends T> loadClass(Class<T> baseClass) {
		if (baseClass == null) {
			throw new NullPointerException("baseClass cannot be null");
		}
		final Class<?> java7Class;
		try {
			java7Class = Class.forName(baseClass.getName() + JAVA7_CLASS_NAME_SUFFIX);
		} catch (ClassNotFoundException e) {
			return baseClass;
		}
		if (baseClass.isAssignableFrom(java7Class)) {
			return java7Class.asSubclass(baseClass);
		}
		throw new IllegalArgumentException("class " + java7Class.getName() + " is not a subclass of " + baseClass.getName());
	}
}
