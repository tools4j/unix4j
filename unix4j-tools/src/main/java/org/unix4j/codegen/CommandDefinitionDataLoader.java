package org.unix4j.codegen;

import fmpp.Engine;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.unix4j.codegen.command.CommandDefinitionLoader;
import org.unix4j.codegen.command.def.CommandDef;
import org.unix4j.codegen.loader.ResourceBasedDataLoader;
import org.unix4j.codegen.loader.ResourceDataLoader;

import java.net.URL;
import java.util.List;

public class CommandDefinitionDataLoader extends ResourceDataLoader {
	
	public CommandDefinitionDataLoader() {
		super(TEMPLATE_LOADER);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public SimpleSequence load(Engine engine, List args) throws Exception {
		System.out.println("loading command definitions...");
		System.out.println("...args: " + args);
		final SimpleSequence result = super.load(engine, args);
		System.out.println("loaded " + result.size() + " command definitions.");
		return result;
	}
	
	public static final ResourceBasedDataLoader TEMPLATE_LOADER = new ResourceBasedDataLoader() {
		@Override
		public TemplateModel load(URL resource) {
			final CommandDef commandDef = new CommandDefinitionLoader().load(resource);
			System.out.println(commandDef.toString("......"));
			try {
				return ObjectWrapper.DEFAULT_WRAPPER.wrap(commandDef);
			} catch (TemplateModelException e) {
				throw new RuntimeException(e);
			}
		}
	};
	
}
