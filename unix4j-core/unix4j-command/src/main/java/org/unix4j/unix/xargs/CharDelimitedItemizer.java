package org.unix4j.unix.xargs;

import org.unix4j.line.Line;

public class CharDelimitedItemizer implements Itemizer {
	
	private final int maxArgs;
	private final long maxLines;
	private final char delim;
	
	private StringBuilder multiLine = new StringBuilder();
	private long curLines;
	
	public CharDelimitedItemizer(XargsArguments args) {
		this.maxArgs = args.isMaxArgsSet() ? args.getMaxArgs() : 1;
		this.maxLines = args.isMaxLinesSet() ? args.getMaxLines() : Integer.MAX_VALUE;
		if (args.isDelimiterSet()) {
			final String delimiter = args.getDelimiter();
			if (delimiter.length() != 1) {
				throw new IllegalArgumentException("delimiter is set and length is " + delimiter.length() + " but only one char is expected: delimiter=" + delimiter);
			}
			this.delim = delimiter.charAt(0);
		} else if (args.isDelimiter0()) {
			this.delim = '\0';
		} else {
			throw new IllegalArgumentException("neither delimiter operand nor delimiter0 option is set in args=" + args);
		}
	}

	@Override
	public boolean itemizeLine(Line line, boolean eof, ItemStorage itemStorage) {
		curLines++;
		
		final int len = line.length();
		int start = 0;
		int end = 0;
		while (start < len) {
			if (delim != line.charAt(end)) {
				end++;
			} else {
				if (multiLine.length() > 0) {
					multiLine.append(line, start, end);
					itemStorage.storeItem(multiLine.toString());
					multiLine.setLength(0);
				} else {
					final CharSequence item = line.subSequence(start, end);
					itemStorage.storeItem(item.toString());
				}
				start = end + 1;
				end = start;
			}
		}
		
		if (curLines < maxLines && itemStorage.size() < maxArgs) {
			multiLine.append(line, start, len);
			return false;
		}
		return true;
	}

}
