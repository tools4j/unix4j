package org.unix4j.codegen.loader;

import java.net.URL;

import freemarker.template.TemplateModel;

/**
 * Loads and returns a {@link TemplateModel} based on a resource such as an XML
 * file.
 */
public interface ResourceBasedDataLoader {
	/**
	 * Loads and returns a {@link TemplateModel} based on the specified
	 * {@code resource}.
	 * 
	 * @param resource
	 *            the resource reference, never null
	 * @return the template model constructed on the basis of the specified
	 *         {@code resource}
	 */
	TemplateModel load(URL resource);

}
