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
	public long increment(long inc) {
		count += inc;
		return count;
	}
	public long decrement() {
		return --count;
	}
	public long decrement(long dec) {
		count -= dec;
		return count;
	}
    public int getWidth(){
        return String.valueOf(getCount()).length();
    }
	
	@Override
	public String toString() {
		return "count=" + count;
	}
}
