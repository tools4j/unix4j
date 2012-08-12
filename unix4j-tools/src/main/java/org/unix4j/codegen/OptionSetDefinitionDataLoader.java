package org.unix4j.codegen;

import java.io.InputStream;
import java.util.List;

import org.unix4j.codegen.loader.ResourceBasedDataLoader;
import org.unix4j.codegen.loader.ResourceDataLoader;
import org.unix4j.codegen.model.command.CommandDef;
import org.unix4j.codegen.model.command.CommandDefinitionLoader;
import org.unix4j.codegen.model.option.OptionSetDef;
import org.unix4j.codegen.model.option.OptionSetDefLoader;

import fmpp.Engine;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class OptionSetDefinitionDataLoader extends ResourceDataLoader {
	
	public OptionSetDefinitionDataLoader() {
		super(TEMPLATE_LOADER);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public SimpleSequence load(Engine engine, List args) throws Exception {
		System.out.println("loading option set definitions...");
		System.out.println("...args: " + args);
		final SimpleSequence result = super.load(engine, args);
		System.out.println("loaded " + result.size() + " option set definitions.");
		return result;
	}
	
	public static final ResourceBasedDataLoader TEMPLATE_LOADER = new ResourceBasedDataLoader() {
		@Override
		public TemplateModel load(InputStream resource) {
			final CommandDef commandDef = new CommandDefinitionLoader().load(resource);
			final OptionSetDef optionSetDef = new OptionSetDefLoader().create(commandDef);
			System.out.println(optionSetDef.toString("......"));
			try {
				return ObjectWrapper.DEFAULT_WRAPPER.wrap(optionSetDef);
			} catch (TemplateModelException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
}
