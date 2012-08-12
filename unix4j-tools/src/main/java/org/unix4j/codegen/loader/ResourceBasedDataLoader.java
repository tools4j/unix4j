package org.unix4j.codegen.loader;

import java.io.InputStream;

import freemarker.template.TemplateModel;

/**
 * Loads and returns a {@link TemplateModel} based on a resource such as
 * an XML file.
 */
public interface ResourceBasedDataLoader {
	/**
	 * Loads and returns a {@link TemplateModel} based on the specified
	 * {@code resource}.
	 * 
	 * @param resource
	 *            the input stream representing the resource, never null
	 * @return the template model constructed on the basis of the specified
	 *         {@code resource}
	 */
	TemplateModel load(InputStream resource);

}
