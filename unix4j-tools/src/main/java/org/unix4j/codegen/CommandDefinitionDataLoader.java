package org.unix4j.codegen;

import java.io.InputStream;
import java.util.List;

import org.unix4j.codegen.loader.ResourceBasedDataLoader;
import org.unix4j.codegen.loader.ResourceDataLoader;
import org.unix4j.codegen.model.command.CommandDef;
import org.unix4j.codegen.model.command.CommandDefinitionLoader;

import fmpp.Engine;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

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
		public TemplateModel load(Engine engine, InputStream resource) {
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
