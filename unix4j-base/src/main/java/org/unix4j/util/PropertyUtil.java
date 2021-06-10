package org.unix4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class with static utility methods if one has to deal with {@link Properties}.
 */
public class PropertyUtil {

	/**
	 * Returns the specified property given the {@code propertyName} either from
	 * the current user's properties file if it exists or from the given
	 * {@code defaultPropertiesFile}.
	 * <p>
	 * The method throws an exception if neither the default nor the machine
	 * specific file exists.
	 * 
	 * @param defaultPropertiesFile
	 *            the default properties file, must exist
	 * @param propertyName
	 *            the name of the desired property
	 * @param fallbackValue
	 *            the fallback value if no property of the given name exists
	 * @return the property value
	 */
	public static String getProperty(String defaultPropertiesFile, String propertyName, String fallbackValue) {
		final Properties props = getProperties(defaultPropertiesFile);
		if (props == null) {
			throw new IllegalArgumentException("default properties file not found: " + defaultPropertiesFile);
		}
		return props.getProperty(propertyName, fallbackValue);
	}

	public static Properties getProperties(String defaultPropertiesFile) {
		try {
			final Properties properties = new Properties();
			properties.load(ResourceUtil.getResource(PropertyUtil.class, defaultPropertiesFile));
			final String userPropertiesFile = getUserPropertiesFileNameFor(defaultPropertiesFile);
			InputStream userPropertiesStream = null;
			try {
				userPropertiesStream = ResourceUtil.getResource(PropertyUtil.class, userPropertiesFile);
			} catch (IllegalArgumentException e) {
				System.err.println("WARN: user properties file not found : " + userPropertiesFile);
				System.err.println("WARN: using default properties file  : " + defaultPropertiesFile);
			}
			if (userPropertiesStream != null) {
				final Properties userProperties = new Properties();
				userProperties.load(userPropertiesStream);
				properties.putAll(userProperties);//replaces default properties
			}
			return properties;
		} catch (IOException e) {
			return null;
		}
	}

	private static String getUserPropertiesFileNameFor(String defaultPropertiesFile) {
		final String defaultPostfix = "-default";
		final String localPostfix = "-" + getUserName();
		final String ending = ".properties";
		final String postfix = defaultPostfix + ending;
		if (defaultPropertiesFile.endsWith(postfix)) {
			return defaultPropertiesFile.substring(0, defaultPropertiesFile.length() - postfix.length()) + localPostfix + ending;
		}
		throw new IllegalArgumentException("name of default properties file must end with " + postfix + ": " + defaultPropertiesFile);
	}

	private static String getUserName() {
		final String userName = System.getProperty("user.name", null);
		if (userName != null) {
			return userName;
		}
		throw new IllegalStateException("cannot evaulate user.name from system properties");
	}

	// no instances
	private PropertyUtil() {
		super();
	}

}
