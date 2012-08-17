package org.unix4j.unix;

import org.junit.Test;
import org.unix4j.Unix4j;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */

public class EchoTest {
	@Test
	public void testEchoSingle() {
		assertEquals("test", Unix4j.create().echo("test").toStringResult());
	}

	@Test
	public void testEchoEmpty() {
		assertEquals("", Unix4j.create().echo("").toStringResult());
	}

	@Test
	public void testEchoTwoWords() {
		assertEquals("one two", Unix4j.create().echo("one two").toStringResult());
	}

	@Test
	public void testTwoLines() {
		assertEquals("one\ntwo", Unix4j.create().echo("one\ntwo").toStringResult());
	}

	@Test
	public void testThreeLinesLastLineEmpty() {
		assertEquals("one\ntwo", Unix4j.create().echo("one\ntwo").toStringResult());
	}

	@Test
	public void testTwoMessages() {
		assertEquals("one\ntwo three\nfour", Unix4j.create().echo("one\ntwo", "three\nfour").toStringResult());
	}
}
