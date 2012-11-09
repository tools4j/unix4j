package org.unix4j.unix.xargs;

import org.unix4j.line.Line;

class CharDelimitedItemizer implements Itemizer {
	
	private final char delimiter;
	
	private StringBuilder multiLine = new StringBuilder();
	
	public CharDelimitedItemizer(char delimiter) {
		this.delimiter = delimiter;
	}

	@Override
	public void itemizeLine(Line line, ItemStorage itemStorage) {
		final int len = line.length();
		
		if (line.getLineEndingLength() == 1 && line.getLineEnding().charAt(0) == delimiter) {
			//the delim is the line ending (e.g. for \0 terminated lines)
			//it is a constraint for the Line that no other such character 
			//exists in the content part of the line
			itemStorage.storeItem(line.getContent());
		} else {
			int start = 0;
			int end = 0;
			while (end < len) {
				if (delimiter != line.charAt(end)) {
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
			if (start < len) {
				multiLine.append(line, start, len);
			}
		}
		itemStorage.incrementLineCount();
	}
	
	@Override
	public void finish(ItemStorage itemStorage) {
		if (multiLine.length() > 0) {
			itemStorage.storeItem(multiLine.toString());
			multiLine.setLength(0);
		}
	}
}
