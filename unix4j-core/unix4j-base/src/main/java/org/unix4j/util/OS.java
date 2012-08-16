package org.unix4j.util;

/**
 * Enum constants for operating systems.
 */
public enum OS {
	/** Windows, any version */
	Windows {
		@Override
		public boolean isCurrent(String osName) {
			return osName.indexOf("win") >= 0;
		}
	},
	/** MAC */
	Mac {
		@Override
		public boolean isCurrent(String osName) {
			return osName.indexOf("mac") >= 0;
		}
	},
	/** Linux or other Unix */
	Unix {
		@Override
		public boolean isCurrent(String osName) {
			return osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0;
		}
	},
	/** SUN Solaris */
	Solaris {
		@Override
		public boolean isCurrent(String osName) {
			return osName.indexOf("sunos") >= 0;
		}
	};
	
	public boolean isCurrent() {
		return isCurrent(System.getProperty("os.name").toLowerCase());
	}

	abstract protected boolean isCurrent(String osName);
	
	public static OS current() {
		for (final OS os : values()) {
			if (os.isCurrent()) return os;
		}
		throw new IllegalStateException("Cannot evaluate OS constant for current operating system: " + System.getProperty("os.name"));
	}
}