package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.builder.Unix4jCommandBuilder;

public class FindTest {

	private Unix4jCommandBuilder unix4j;

	@Test
	public void testFind() {
		Unix4j.find(".", "/home/ben/dev/*/groovy/*/*", "target/*", "asdf").toStdOut();
	}
}
