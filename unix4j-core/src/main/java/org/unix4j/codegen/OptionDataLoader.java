package org.unix4j.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fmpp.Engine;
import fmpp.tdd.DataLoader;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class OptionDataLoader implements DataLoader {
	@SuppressWarnings("rawtypes")
	@Override
	public Object load(Engine e, List args) throws Exception {
		final List<TemplateModel> optionDefs = new ArrayList<TemplateModel>();
		for (final Object arg : args) {
			final TemplateModel optionDef = loadOptionDef(e, String.valueOf(arg));
			optionDefs.add(optionDef);
		}
		return new SimpleSequence(optionDefs);
	}

	private TemplateModel loadOptionDef(Engine e, String className) throws ClassNotFoundException, TemplateModelException {
		final Class<?> optionClass = Class.forName(className);
		if (Enum.class.isAssignableFrom(optionClass)) {
			final OptionDef optionDef = create(optionClass);
			return ObjectWrapper.DEFAULT_WRAPPER.wrap(optionDef);
		} else {
			throw new IllegalArgumentException("option class " + className + " is not an enum type");
		}
	}

	@SuppressWarnings("unchecked")
	private OptionDef create(Class<?> optionClass) {
		return OptionDef.create(optionClass.asSubclass(Enum.class));
	}
	
	/* 
  optionDefsTest: org.unix4j.codegen.OptionDataLoader([
  	org.unix4j.unix.ls.LsOption
  ])
*/
	public static void main(String[] args) {
		try {
			final Object mdl = new OptionDataLoader().load(null, Collections.singletonList("org.unix4j.unix.ls.LsOption"));
			
			System.out.println(mdl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
