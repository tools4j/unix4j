package org.unix4j.codegen.loader;

import java.util.ArrayList;
import java.util.List;

import fmpp.Engine;
import fmpp.tdd.DataLoader;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;

/**
 * A {@link DataLoader} for the fmpp/freemarker codegenerator passing existing
 * classes to a {@link ClassBasedDataLoader} for further analysis. The fully
 * qualified class names are expected as input parameters in the arguments list
 * of {@link #load(Engine, List)}.
 */
public class ClassInspectionDataLoader implements DataLoader {

	private final ClassBasedDataLoader templateLoader;

	public ClassInspectionDataLoader(ClassBasedDataLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public SimpleSequence load(Engine engine, List args) throws Exception {
		return loadDataModel(engine, args);
	}

	private SimpleSequence loadDataModel(Engine engine, List<?> args) throws Exception {
		final List<Class<?>> classes = loadClasses(engine, args);
		final List<TemplateModel> templateModels = new ArrayList<TemplateModel>();
		for (final Class<?> clazz : classes) {
			final TemplateModel templateModel = templateLoader.load(clazz);
			if (templateModel != null) {
				templateModels.add(templateModel);
			}
		}
		return new SimpleSequence(templateModels);
	}

	private List<Class<?>> loadClasses(Engine engine, List<?> args) throws ClassNotFoundException {
		final List<Class<?>> classes = new ArrayList<Class<?>>(args.size());
		for (final Object arg : args) {
			classes.add(loadClass(String.valueOf(arg)));
		}
		return classes;
	}

	private Class<?> loadClass(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

}
