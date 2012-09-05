package org.unix4j.unix.wc;

import org.unix4j.unix.Wc;

/**
 * User: ben
 */
public class WcFactory implements Wc.Interface<WcCommand> {
	public static final WcFactory INSTANCE = new WcFactory();

	private WcFactory() {
		super();
	}

	@Override
	public WcCommand wc() {
		return wcCountLinesWordsAndChars();
	}

	@Override
	public WcCommand wc(Wc.Options options) {
		final WcArguments args = new WcArguments(options);
		return new WcCommand(args);
	}

	@Override
	public WcCommand wcCountLinesWordsAndChars() {
		final WcArguments args = new WcArguments(Wc.OPTIONS.lines.words.chars);
		return new WcCommand(args);
	}

	@Override
	public WcCommand wcCountLines() {
		final WcArguments args = new WcArguments(Wc.OPTIONS.lines);
		return new WcCommand(args);
	}

	@Override
	public WcCommand wcCountChars() {
		final WcArguments args = new WcArguments(Wc.OPTIONS.chars);
		return new WcCommand(args);
	}

	@Override
	public WcCommand wcCountWords() {
		final WcArguments args = new WcArguments(Wc.OPTIONS.words);
		return new WcCommand(args);
	}
}
