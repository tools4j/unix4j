package org.unix4j.util;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class RangeTest {

	@Test
	public void testOf(){
		assertWithinRange(Range.of(1,2,5,6), 1,2,5,6);
		assertNotWithinRange(Range.of(1, 2, 5, 6), 3,4,7,100);
	}

	@Test
	public void testFromStartTo(){
		assertWithinRange(Range.fromStartTo(3), 1,2,3);
		assertNotWithinRange(Range.fromStartTo(3), 4,5,6);
	}

	@Test
	public void testToEndFrom(){
		assertWithinRange(Range.toEndFrom(3), 3,4,10000);
		assertNotWithinRange(Range.toEndFrom(3), 1,2);
	}

	@Test
	public void testBetween(){
		assertWithinRange(Range.between(3, 6), 3,4,5,6);
		assertNotWithinRange(Range.between(3, 6), 1,2,7,8,9);
	}

	@Test
	public void testComposite1(){
		final Range range = Range.of(1,2,3).andBetween(6,9).andBetween(11,12).andToEndFrom(40);
		assertWithinRange(range, 1,2,3,6,7,8,9,11,12,40,210);
		assertNotWithinRange(range, 4,5,10,13,30,39);
	}

	@Test
	public void testComposite2(){
		final Range range = Range.of(10, 12, 13).andFromStartTo(3).andBetween(11, 12).andOf(40,41);
		assertWithinRange(range, 1,2,3,10,11,12,13,40,41);
		assertNotWithinRange(range, 4,5,6,7,8,9,14,1000);
	}

	private void assertWithinRange(Range range, int ... indexes) {
		for(int i: indexes){
			assertTrue(range.isWithinRange(i));
		}
	}

	private void assertNotWithinRange(Range range, int ... indexes) {
		for(int i: indexes){
			assertFalse(range.isWithinRange(i));
		}
	}
}
