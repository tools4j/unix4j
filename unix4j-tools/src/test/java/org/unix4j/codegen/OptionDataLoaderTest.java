package org.unix4j.codegen;

import org.junit.Test;

public class OptionDataLoaderTest {

	@Test
	public void testLoadOptionModel() throws Exception {
		final Object mdl = OptionDataLoader.TEMPLATE_LOADER.load(Ls.class);
		System.out.println(mdl);
	}
}
