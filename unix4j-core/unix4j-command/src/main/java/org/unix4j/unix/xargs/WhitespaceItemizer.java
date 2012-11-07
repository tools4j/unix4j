package org.unix4j.unix.xargs;

import org.unix4j.line.Line;
import org.unix4j.util.StringUtil;

public class WhitespaceItemizer implements Itemizer {
	
	private final int maxArgs;
	private final long maxLines;
	
	private long curLines;
	private boolean lastLineWithTrailingWhitespace;
	
	public WhitespaceItemizer(XargsArguments args) {
		this.maxArgs = args.isMaxArgsSet() ? args.getMaxArgs() : 0;
		this.maxLines = args.isMaxLinesSet() ? args.getMaxLines() : 0;
	}

	@Override
	public boolean itemizeLine(Line line, boolean eof, ItemStorage itemStorage) {
		if (!lastLineWithTrailingWhitespace) {
			curLines++;
		}
		
		final int len = line.length();
		int start = StringUtil.findStartTrimWhitespace(line);
		int end = Integer.MAX_VALUE;
		while (start < len) {
			end = start + 1;
			while (end < len && !Character.isWhitespace(line.charAt(end))) {
				end++;
			}
			final String item = line.subSequence(start, end).toString();
			itemStorage.storeItem(item);
			start = StringUtil.findStartTrimWhitespace(line, end);
		}
		lastLineWithTrailingWhitespace = end < line.getContentLength();
		
		return eof || curLines >= maxLines || itemStorage.size() >= maxArgs;
	}

}
