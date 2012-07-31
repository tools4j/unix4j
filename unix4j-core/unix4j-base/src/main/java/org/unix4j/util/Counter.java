package org.unix4j.util;

public class Counter {
	
	private long count;
	
	public Counter() {
		count = 0;
	}
	
	public long getCount() {
		return count;
	}

	public void reset() {
		count = 0;
	}
	public long increment() {
		return ++count;
	}
	public long increment(int inc) {
		count += inc;
		return count;
	}
	public long decrement() {
		return --count;
	}
	public long decrement(int dec) {
		count -= dec;
		return count;
	}
	
	@Override
	public String toString() {
		return "count=" + count;
	}
}
