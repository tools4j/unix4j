package org.unix4j.codegen;

import java.net.URL;
import java.util.List;

import org.unix4j.codegen.command.CommandDefinitionLoader;
import org.unix4j.codegen.command.def.CommandDef;
import org.unix4j.codegen.loader.ResourceBasedDataLoader;
import org.unix4j.codegen.loader.ResourceDataLoader;
import org.unix4j.codegen.optset.OptionSetDefinitionLoader;
import org.unix4j.codegen.optset.def.OptionSetDef;

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
		public TemplateModel load(URL resource) {
			final CommandDef commandDef = new CommandDefinitionLoader().load(resource);
			final OptionSetDef optionSetDef = new OptionSetDefinitionLoader().create(commandDef);
			System.out.println(optionSetDef.toString("......"));
			try {
				return ObjectWrapper.DEFAULT_WRAPPER.wrap(optionSetDef);
			} catch (TemplateModelException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
}
