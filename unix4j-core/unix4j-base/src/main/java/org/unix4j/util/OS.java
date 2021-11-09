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
	},
	/** IBM AIX */
	AIX {
		@Override
		public boolean isCurrent(String osName) {
			return osName.indexOf("aix") >= 0;
		}
	},
	/** Other */
	Other {
		@Override
		public boolean isCurrent(String osName) {
			return false;
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
		return Other;
	}
}