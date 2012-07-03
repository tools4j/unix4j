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
		assertEquals("test\n", Unix4j.create().echo("test").executeToString());
	}

	@Test
	public void testEchoEmpty() {
		assertEquals("\n", Unix4j.create().echo("").executeToString());
	}

	@Test
	public void testEchoTwoWords() {
		assertEquals("one two\n", Unix4j.create().echo("one two").executeToString());
	}

	@Test
	public void testTwoLines() {
		assertEquals("one\ntwo\n", Unix4j.create().echo("one\ntwo").executeToString());
	}

	@Test
	public void testThreeLinesLastLineEmpty() {
		assertEquals("one\ntwo\n\n", Unix4j.create().echo("one\ntwo\n").executeToString());
	}

	@Test
	public void testTwoMessages() {
		assertEquals("one\ntwo three\nfour\n", Unix4j.create().echo("one\ntwo", "three\nfour").executeToString());
	}
}
