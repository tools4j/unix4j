package org.unix4j.variable;

/**
 * Default variable implementation.
 */
public class DefaultVariable implements Variable {
	
	private final String name;
	
	public DefaultVariable(String name) {
		if (name == null) {
			throw new NullPointerException("name cannot be null");
		}
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj != null && obj.getClass() == getClass()) {
			final String otherName = ((DefaultVariable)obj).getName();
			return getName().equals(otherName);
		}
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

}
