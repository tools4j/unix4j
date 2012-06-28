package org.unix4j.unix.ls;

class StringUtil {

	static final String fixSizeString(int size, boolean left, char filler, int value) {
		return fixSizeString(size, left, filler, String.valueOf(value));
	}

	static final String fixSizeString(int size, boolean left, String s) {
		return fixSizeString(size, left, ' ', s);
	}

	static final String fixSizeString(int size, boolean left, char filler, String s) {
		if (s.length() < size) {
			final StringBuilder sb = new StringBuilder(size);
			if (left)
				sb.append(s);
			for (int i = 0; i < size - s.length(); i++) {
				sb.append(filler);
			}
			if (!left)
				sb.append(s);
			return sb.toString();
		} else {
			return left ? s.substring(0, size) : s.substring(s.length() - size, s.length());
		}
	}

}
