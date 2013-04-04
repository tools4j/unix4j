package org.unix4j.unix;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.unix4j.Unix4j;

/**
 * Unit test for simple App.
 */

public class EchoTest {
	@Test
	public void testEchoSingle() {
		assertEquals("test", Unix4j.builder().echo("test").toStringResult());
	}

	@Test
	public void testEchoEmpty() {
		assertEquals("", Unix4j.builder().echo("").toStringResult());
	}

	@Test
	public void testEchoTwoWords() {
		assertEquals("one two", Unix4j.builder().echo("one two").toStringResult());
	}

	@Test
	public void testTwoLines() {
		assertEquals("one\ntwo", Unix4j.builder().echo("one\ntwo").toStringResult());
	}

	@Test
	public void testThreeLinesLastLineEmpty() {
		assertEquals("one\ntwo", Unix4j.builder().echo("one\ntwo").toStringResult());
	}

	@Test
	public void testTwoMessages() {
		assertEquals("one\ntwo three\nfour", Unix4j.builder().echo("one\ntwo", "three\nfour").toStringResult());
	}
}
