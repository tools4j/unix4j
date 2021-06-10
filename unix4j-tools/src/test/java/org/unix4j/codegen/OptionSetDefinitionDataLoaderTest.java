package org.unix4j.codegen;

import org.junit.Test;

import java.util.Collections;

public class OptionSetDefinitionDataLoaderTest {

	@Test
	public void testOptionSetDefinition() throws Exception {
		final Object ls = new OptionSetDefinitionDataLoader().load(null, Collections.singletonList("/ls.xml"));
		System.out.println(ls);
		final Object uniq = new OptionSetDefinitionDataLoader().load(null, Collections.singletonList("/uniq.xml"));
		System.out.println(uniq);
		final Object cut = new OptionSetDefinitionDataLoader().load(null, Collections.singletonList("/cut.xml"));
		System.out.println(cut);
		final Object sort = new OptionSetDefinitionDataLoader().load(null, Collections.singletonList("/sort.xml"));
		System.out.println(sort);
	}
}
