package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;

public class FindTest {

	@Test
	public void testFind() {
		Unix4j.find(".", "/home/ben/dev/*/groovy/*/*", "target/*", "asdf").toStdOut();
	}
}
