package org.unix4j.unix.ls;

import java.io.File;

import org.unix4j.unix.Ls.Interface;

/**
 * Factory class returning a new {@link LsCommand} instance from every signature
 * method.
 */
public final class LsFactory implements Interface<LsCommand> {
	
	/**
	 * The singleton instance of this factory.
	 */
	public static final LsFactory INSTANCE = new LsFactory();
	
	/**
	 * Private, only used to create singleton instance.
	 */
	private LsFactory() {
		super();
	}

	@Override
	public LsCommand ls() {
		return new LsCommand(new LsArguments());
	}

	@Override
	public LsCommand ls(File... files) {
		final LsArguments args = new LsArguments();
		args.setFiles(files);
		return new LsCommand(args);
	}

	@Override
	public LsCommand ls(String... paths) {
		final LsArguments args = new LsArguments();
		args.setPaths(paths);
		return new LsCommand(args);
	}

	@Override
	public LsCommand ls(LsOptions options) {
		return new LsCommand(new LsArguments(options));
	}

	@Override
	public LsCommand ls(LsOptions options, File... files) {
		final LsArguments args = new LsArguments(options);
		args.setFiles(files);
		return new LsCommand(args);
	}

	@Override
	public LsCommand ls(LsOptions options, String... paths) {
		final LsArguments args = new LsArguments(options);
		args.setPaths(paths);
		return new LsCommand(args);
	}

}