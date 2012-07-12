package org.unix4j.codegen;

import java.util.List;

import org.unix4j.codegen.annotation.Options;
import org.unix4j.codegen.loader.OptionModelLoader;
import org.unix4j.codegen.model.option.OptionDef;

import fmpp.Engine;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class OptionDataLoader extends CompilingDataLoader {
	
	public OptionDataLoader() {
		super(TEMPLATE_LOADER);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public SimpleSequence load(Engine engine, List args) throws Exception {
		System.out.println("loading command options...");
		System.out.println("...args: " + args);
		final SimpleSequence result = super.load(engine, args);
		System.out.println("loaded " + result.size() + " command options.");
		return result;
	}
	
	public static final ClassBasedDataLoader TEMPLATE_LOADER = new ClassBasedDataLoader() {
		@Override
		public TemplateModel load(Class<?> clazz) {
			final Options options = clazz.getAnnotation(Options.class);
			if (options != null) {
				System.out.println("...FOUND: command=" + clazz.getName() + ", options=" + options.value().getName());
				return loadOptionDef(clazz, options.value());
			} else {
				System.out.println("...not a command: " + clazz.getName());
			}
			return null;
		}
		private TemplateModel loadOptionDef(Class<?> commandClass, Class<? extends Enum<?>> optionClass) {
			final OptionDef optionDef = create(commandClass, optionClass);
			try {
				return ObjectWrapper.DEFAULT_WRAPPER.wrap(optionDef);
			} catch (TemplateModelException e) {
				throw new RuntimeException(e);
			}
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private OptionDef create(Class<?> commandClass, Class<? extends Enum> optionClass) {
			return new OptionModelLoader().create(commandClass, optionClass);
		}
	};
	
}
