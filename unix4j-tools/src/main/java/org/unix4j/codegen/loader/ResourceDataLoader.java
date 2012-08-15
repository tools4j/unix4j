package org.unix4j.codegen.loader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fmpp.Engine;
import fmpp.tdd.DataLoader;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;

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
			final InputStream resource = loadResource(args.get(i));
			if (resource != null) {
				final TemplateModel templateModel = templateLoader.load(engine, resource);
				if (templateModel != null) {
					templateModels.add(templateModel);
				}
			} else {
				throw new IllegalArgumentException("resource[" + i + "] not found: " + args.get(i));
			}
		}
		return new SimpleSequence(templateModels);
	}

	private InputStream loadResource(Object resourceName) {
		if (resourceName != null) {
			return ResourceDataLoader.class.getResourceAsStream(resourceName.toString());
		}
		return null;
	}

}
