package org.unix4j.unix.sort;

import java.io.File;

import org.unix4j.unix.Sort.Interface;
import org.unix4j.unix.Sort.Options;

/**
 * Factory class returning a new {@link SortCommand} instance from every
 * signature method.
 */
public final class SortFactory implements Interface<SortCommand> {

	/**
	 * The singleton instance of this factory.
	 */
	public static final SortFactory INSTANCE = new SortFactory();

	/**
	 * Private, only used to create singleton instance.
	 */
	private SortFactory() {
		super();
	}

	@Override
	public SortCommand sort() {
		return new SortCommand(new SortArguments());
	}

	@Override
	public SortCommand sort(File... files) {
		final SortArguments args = new SortArguments();
		args.setFiles(files);
		return new SortCommand(args);
	}

	@Override
	public SortCommand sort(String... paths) {
		final SortArguments args = new SortArguments();
		args.setPaths(paths);
		return new SortCommand(args);
	}

	@Override
	public SortCommand sort(Options options) {
		final SortArguments args = new SortArguments(options);
		return new SortCommand(args);
	}

	@Override
	public SortCommand sort(Options options, File... files) {
		final SortArguments args = new SortArguments(options);
		args.setFiles(files);
		return new SortCommand(args);
	}

	@Override
	public SortCommand sort(Options options, String... paths) {
		final SortArguments args = new SortArguments(options);
		args.setPaths(paths);
		return new SortCommand(args);
	}

}