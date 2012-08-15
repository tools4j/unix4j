package org.unix4j.codegen.loader;

import java.io.InputStream;

import fmpp.Engine;
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
	 * @param engine
	 *            the fmpp/freemarker template engine
	 * @param resource
	 *            the input streams representing a resource, never null
	 * @return the template model constructed on the basis of the specified
	 *         {@code resource}
	 */
	TemplateModel load(Engine engine, InputStream resource);

}
