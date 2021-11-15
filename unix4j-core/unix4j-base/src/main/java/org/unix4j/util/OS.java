package org.unix4j.util;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Enum constants for operating systems.
 */
public enum OS {
	/** Windows, any version */
	Windows(osName -> osName.contains("win")),
	/** MAC */
	Mac(osName -> osName.contains("mac")),
	/** Linux, AIX or other Unix */
	Unix(osName -> osName.contains("nix") || osName.contains("nux") || osName.contains("aix")),
	/** SUN Solaris */
	Solaris(osName -> osName.contains("sunos")),
	/** Any other unrecognised OS */
	Other(osName -> true);

	private final Predicate<? super String> osNameMatcher;
	
	OS(final Predicate<? super String> osNameMatcher) {
		this.osNameMatcher = requireNonNull(osNameMatcher);
	}
	
	private static final OS CURRENT = initCurrent();

	public boolean isCurrent() {
		return this == CURRENT;
	}

	public static OS current() {
		return CURRENT;
	}

	private static OS initCurrent() {
		final String osName = System.getProperty("os.name").toLowerCase();
		for (final OS os : values()) {
			if (os.osNameMatcher.test(osName)) {
				return os;
			}
		}
		//should not get here
		return Other;
	}
}