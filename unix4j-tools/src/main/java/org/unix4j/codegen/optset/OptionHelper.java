package org.unix4j.codegen.optset;

import java.util.Arrays;
import java.util.Collection;

import org.unix4j.codegen.command.def.OptionDef;

public class OptionHelper {
	
	public String getNameWithOptionPostfix(String name, Collection<OptionDef> options) {
		if (options.isEmpty()) return name;
		return name + "_" + allOptionAcronyms(options);
	}
	public String allOptionAcronyms(Collection<OptionDef> options) {
		final char[] acronyms = new char[options.size()];
		int index = 0;
		for (final OptionDef opt : options) {
			acronyms[index] = opt.acronym.charAt(0);
			index++;
		}
		Arrays.sort(acronyms);
		return String.valueOf(acronyms);
	}
}