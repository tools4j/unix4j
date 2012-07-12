package org.unix4j.codegen;

import freemarker.template.TemplateModel;

/**
 * Loads and returns a {@link TemplateModel} based on a Java {@link Class}, for
 * instance by analysing the class through reflection and/or reading annotations
 * present with the class or its elements.
 */
public interface ClassBasedDataLoader {
	/**
	 * Loads and returns a {@link TemplateModel} based on the specified
	 * {@code clazz}. Most implementations analyze the class through reflection
	 * which is possibly enriched with annotations supporting the code
	 * generation.
	 * 
	 * @param clazz
	 *            the class to analyze
	 * @return the template model constructed on the basis of the specified
	 *         {@code clazz}
	 */
	TemplateModel load(Class<?> clazz);

}
