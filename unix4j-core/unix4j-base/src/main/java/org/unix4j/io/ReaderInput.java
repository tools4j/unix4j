package org.unix4j.io;

import java.io.IOException;
import java.io.Reader;

import org.unix4j.line.Line;
import org.unix4j.line.SingleCharSequenceLine;

/**
 * Input device based on a {@link Reader}. This class is the base for most input
 * devices and handles the parsing and recognition of lines.
 */
public class ReaderInput extends AbstractInput {
	
	private final Reader reader;
	private final char[] buffer = new char[1024];
	private int length;
	private int offset;
	
	/**
	 * Constructor with reader.
	 * 
	 * @param reader the reader forming the basis of this input device.
	 */
	public ReaderInput(Reader reader) {
		this.reader = reader;
		readBuffer();
	}

	@Override
	public boolean hasMoreLines() {
		return length > offset;
	}

	@Override
	public Line readLine() {
		if (length == 0) {
			readBuffer();
		}
		if (length > offset) {
			return makeLine(null);
		}
		//no more lines
		return null;
	}
	
	private Line makeLine(StringBuilder lineBuilder) {
		int len = length;
		int index = offset;
		do {
			while (index < len) {
				final char ch0 = buffer[index];
				if (ch0 == '\n' || ch0 == '\r') {
					int contentEnd = index;
					index++;
					if (index < len) {
						final char ch1 = buffer[index]; 
						if ((ch1 == '\n' || ch1 == '\r') && ch0 != ch1) {
							index++;
						}
						if (lineBuilder == null) {
							lineBuilder = new StringBuilder(index - offset);
						}
						lineBuilder.append(buffer, offset, index - offset);
						if (index < len) {
							offset = index;
						} else {
							readBuffer();
						}
						return new SingleCharSequenceLine(lineBuilder, index - contentEnd);
					} else {
						if (lineBuilder == null) {
							lineBuilder = new StringBuilder(len - offset + 1);
						}
						lineBuilder.append(buffer, offset, len - offset);
						return makeLineMaybeWithOneMoreLineEndingChar(lineBuilder);
					}
				}
				index++;
			}
			if (lineBuilder == null) {
				lineBuilder = new StringBuilder(len - offset + 16);
			}
			lineBuilder.append(buffer, offset, len - offset);
			readBuffer();
			index = offset;
			len = length;
		} while (index < len);
		
		//eof, no newline, return rest as a line if there is something to return 
		return lineBuilder.length() > 0 ? new SingleCharSequenceLine(lineBuilder, 0) : null;
	}

	private Line makeLineMaybeWithOneMoreLineEndingChar(StringBuilder lineBuilder) {
		int lineEndingLength = 1;
		readBuffer();
		if (offset < length) {
			final char ch = buffer[offset];
			if (ch == '\n' || ch == '\r') {
				if (lineBuilder.charAt(lineBuilder.length() - 1) != ch) {
					lineBuilder.append(ch);
					lineEndingLength++;
					offset++;
				}
			}
		}
		return new SingleCharSequenceLine(lineBuilder, lineEndingLength);
	}

	private void readBuffer() {
		try {
			this.length = reader.read(buffer);
			this.offset = 0;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
