package org.unix4j.unix.uniq;

import java.io.File;

import org.unix4j.unix.Uniq.Interface;

/**
 * Factory class returning a new {@link UniqCommand} instance from every
 * signature method.
 */
public final class UniqFactory implements Interface<UniqCommand> {

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

	@Override
	public UniqCommand uniq() {
		return new UniqCommand(new UniqArguments());
	}

	@Override
	public UniqCommand uniq(File file) {
		final UniqArguments args = new UniqArguments();
		args.setFile(file);
		return new UniqCommand(args);
	}

	@Override
	public UniqCommand uniq(String path) {
		final UniqArguments args = new UniqArguments();
		args.setPath(path);
		return new UniqCommand(args);
	}

	@Override
	public UniqCommand uniq(UniqOptions options) {
		return new UniqCommand(new UniqArguments(options));
	}

	@Override
	public UniqCommand uniq(UniqOptions options, File file) {
		final UniqArguments args = new UniqArguments(options);
		args.setFile(file);
		return new UniqCommand(args);
	}

	@Override
	public UniqCommand uniq(UniqOptions options, String path) {
		final UniqArguments args = new UniqArguments(options);
		args.setPath(path);
		return new UniqCommand(args);
	}
}