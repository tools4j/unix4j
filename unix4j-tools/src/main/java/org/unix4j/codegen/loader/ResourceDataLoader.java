package org.unix4j.codegen.loader;

import fmpp.Engine;
import fmpp.tdd.DataLoader;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A {@link DataLoader} for the fmpp/freemarker codegenerator passing existing
 * resources to a {@link ResourceBasedDataLoader} for further analysis. The
 * resource paths as defined by {@link Class#getResourceAsStream(String)} are
 * expected as input parameters in the arguments list of
 * {@link #load(Engine, List)}.
 */
public class ResourceDataLoader implements DataLoader {

	private final ResourceBasedDataLoader templateLoader;

	public ResourceDataLoader(ResourceBasedDataLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleSequence load(Engine engine, List args) throws Exception {
		return loadDataModel(engine, args);
	}

	private SimpleSequence loadDataModel(Engine engine, List<?> args) throws Exception {
		final List<TemplateModel> templateModels = new ArrayList<TemplateModel>();
		for (int i = 0; i < args.size(); i++) {
			final String resourceName = args.get(i).toString();
			final URL resource = loadResource(engine, resourceName);
			if (resource != null) {
				final TemplateModel templateModel = templateLoader.load(resource);
				if (templateModel != null) {
					templateModels.add(templateModel);
				}
			} else {
				throw new IllegalArgumentException("resource[" + i + "] not found: " + resourceName);
			}
		}
		return new SimpleSequence(templateModels);
	}

	public static URL loadResource(Engine engine, String resourceName) throws URISyntaxException, MalformedURLException {
		if (resourceName != null) {
			final URL url = ResourceDataLoader.class.getResource(resourceName);
			if (url != null) {
				return url;
			} else {
				File file = new File(resourceName);
				if (file.canRead()) {
					return file.toURI().toURL();
				}
				file = new File(engine.getDataRoot(), resourceName);
				if (file.canRead()) {
					return file.toURI().toURL();
				}
			}
		}
		return null;
	}

}
