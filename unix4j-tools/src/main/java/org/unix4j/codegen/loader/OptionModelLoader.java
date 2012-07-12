package org.unix4j.codegen.loader;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.unix4j.codegen.annotation.Acronym;
import org.unix4j.codegen.annotation.Javadoc;
import org.unix4j.codegen.annotation.Name;
import org.unix4j.codegen.model.TypeDef;
import org.unix4j.codegen.model.option.Command;
import org.unix4j.codegen.model.option.OptionDef;
import org.unix4j.codegen.model.option.OptionSet;

public class OptionModelLoader {

	public <E extends Enum<E>> OptionDef create(Class<?> commandClass, Class<E> optionClass) {
		final Command cmd = defineCommand(commandClass);
		final OptionDef def = new OptionDef(cmd, new TypeDef(optionClass), optionClass.getPackage().getName());
		defineOptions(optionClass, def);
		defineOptionSets(optionClass, def);
		return def;
	}

	private Command defineCommand(Class<?> commandClass) {
		final Name name = commandClass.getAnnotation(Name.class);
		return new Command(name != null ? name.value() : commandClass.getSimpleName(), commandClass);
	}

	private <E extends Enum<E>> void defineOptions(Class<E> optionClass, OptionDef def) {
		final E[] options = optionClass.getEnumConstants();
		for (int i = 0; i < options.length; i++) {
			final String name = getOptionName(options[i]);
			def.options.put(name, options[i].name());
			String javadoc;
			try {
				final Javadoc javadocAnnotation = optionClass.getField(options[i].name()).getAnnotation(Javadoc.class);
				if (javadocAnnotation != null) {
					javadoc = javadocAnnotation.value();
				} else {
					javadoc = "MISSING: " + Javadoc.class.getSimpleName() + " annotation for option " + options[i]; 
				}
			} catch (Exception e) {
				javadoc = "ERROR: " + e; 
			}
			def.javadoc.put(name, javadoc);
		}
	}

	private <E extends Enum<E>> void defineOptionSets(Class<E> optionClass, OptionDef def) {
		defineOptionSetsRecursive(optionClass, def, Collections.singletonList(EnumSet.allOf(optionClass)));
	}

	private <E extends Enum<E>> void defineOptionSetsRecursive(Class<E> optionClass, OptionDef def, Collection<EnumSet<E>> thisLevelActiveOptions) {
		final Set<EnumSet<E>> nextLevelActiveOptions = new LinkedHashSet<EnumSet<E>>();
		for (final EnumSet<E> active : thisLevelActiveOptions) {
			final OptionSet optionSet = defineOptionSet(def.optionSetType.className, active);
			def.optionSets.add(optionSet);
			for (final E newInactive : active) {
				final EnumSet<E> nextActive = EnumSet.copyOf(active);
				nextActive.remove(newInactive);
				nextLevelActiveOptions.add(nextActive);
			}
		}
		if (!nextLevelActiveOptions.isEmpty()) {
			defineOptionSetsRecursive(optionClass, def, nextLevelActiveOptions);
		}
	}
	private <E extends Enum<E>> OptionSet defineOptionSet(String optionSetClassName, EnumSet<E> activeOptions) {
		final String name = getOptionSetName(optionSetClassName, activeOptions);
		final OptionSet set = new OptionSet(name);
		for (final E active : activeOptions) {
			set.active.add(getOptionName(active));
		}
		final EnumSet<E> inactiveOptions = EnumSet.complementOf(activeOptions);
		for (final E inactive : inactiveOptions) {
			activeOptions.add(inactive);
			final String nextName = getOptionSetName(optionSetClassName, activeOptions);
			set.next.put(getOptionName(inactive), nextName);
			activeOptions.remove(inactive);
		}
		return set;
	}

	private <E extends Enum<E>> String getOptionSetName(String baseName, EnumSet<E> active) {
		final StringBuilder sb = new StringBuilder(baseName);
		for (final E act : active) {
			sb.append('_').append(getOptionName(act));
		}
		return sb.toString();
	}
	private <E extends Enum<E>> String getOptionName(E option) {
		try {
			final Acronym acronym = option.getClass().getField(option.name()).getAnnotation(Acronym.class);
			return String.valueOf(acronym.value());
		} catch (Exception e) {
			//ignore
			System.err.println(e);
		}
		//fallback: simply the option name
		return option.name();
	}
}
