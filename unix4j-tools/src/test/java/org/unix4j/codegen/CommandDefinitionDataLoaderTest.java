package org.unix4j.codegen;

import org.junit.Test;

import java.util.Collections;

public class CommandDefinitionDataLoaderTest {

	@Test
	public void testLoadCommandDefinition() throws Exception {
		final Object mdl = new CommandDefinitionDataLoader().load(null, Collections.singletonList("/ls.xml"));
		System.out.println(mdl);
	}
}
