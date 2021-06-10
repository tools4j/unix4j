package org.unix4j.unix.xargs;

import org.unix4j.line.Line;
import org.unix4j.util.StringUtil;

class WhitespaceItemizer implements Itemizer {
	
	private boolean lastLineWithTrailingWhitespace;
	
	public WhitespaceItemizer() {
		super();
	}

	@Override
	public void itemizeLine(Line line, ItemStorage itemStorage) {
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
		if (!lastLineWithTrailingWhitespace) {
			itemStorage.incrementLineCount();
		}
	}

	@Override
	public void finish(ItemStorage itemStorage) {
		if (lastLineWithTrailingWhitespace) {
			itemStorage.incrementLineCount();
			lastLineWithTrailingWhitespace = false;
		}
	}

}
