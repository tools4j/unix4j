	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified file.
	 * 
	 * @param file
	 *            the file redirected to the input of the first command
	 * @return the fromFile to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromFile(File file) {
		return builder(new FileInput(file));
	}

	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified resource relative to the classpath. The resource is
	 * usually a file or URL on the classpath. The resource is read using
	 * {@link Class#getResourceAsStream(String)}.
	 * 
	 * @param resource
	 *            a path to the file to to redirect to the next command The will
	 *            need to be on the classpath. If the file is in the root
	 *            directory, the filename should be prefixed with a forward
	 *            slash. e.g.:
	 * 
	 *            <pre>
	 * /test-file.txt
	 * </pre>
	 * 
	 *            If the file is in a package, then the package should be
	 *            specified prefixed with a forward slash, and with each dot "."
	 *            replaced with a forward slash. e.g.:
	 * 
	 *            <pre>
	 * /org/company/my/package/test-file.txt
	 * </pre>
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromResource(String resource) {
		return builder(new ResourceInput(resource));
	}

	/**
	 * Returns a builder to create a command or command chain reading from the
	 * specified input string.
	 * 
	 * @param input
	 *            the String written to the input of the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder fromString(String input) {
		return builder(new StringInput(input));
	}

	/**
	 * Returns a builder to create a command or command chain reading the input
	 * from the specified input stream.
	 * 
	 * @param input
	 *            the input stream redirected to the input of the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder from(InputStream input) {
		return builder(new StreamInput(input));
	}

	/**
	 * Returns a builder to create a command or command chain reading from the
	 * specified input object.
	 * 
	 * @param input
	 *            the input passed to the first command
	 * @return the builder to create the command or command chain
	 */
	public static Unix4jCommandBuilder from(Input input) {
		return builder(input);
	}