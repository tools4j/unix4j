package org.unix4j.codegen;

import java.util.Collections;

import org.junit.Test;

public class CommandDefinitionDataLoaderTest {

	@Test
	public void testLoadCommandDefinition() throws Exception {
		final Object mdl = new CommandDefinitionDataLoader().load(null, Collections.singletonList("/ls.xml"));
		System.out.println(mdl);
	}
}
