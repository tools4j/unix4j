package org.unix4j.unix.uniq;


/**
 * Factory class returning a new TODO {@code UniqCommand} instance from every signature
 * method.
 */
public final class UniqFactory /*implements Interface<UniqCommand> */ {
	
	/**
	 * The singleton instance of this factory.
	 */
	public static final UniqFactory INSTANCE = new UniqFactory();
	
	/**
	 * Private, only used to create singleton instance.
	 */
	private UniqFactory() {
		super();
	}

}