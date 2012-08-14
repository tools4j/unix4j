package org.unix4j.codegen.model.option;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.unix4j.codegen.annotation.Javadoc;
import org.unix4j.codegen.annotation.Name;
import org.unix4j.codegen.annotation.Options;
import org.unix4j.codegen.model.TypeDef;

public class OptionModelLoader {

	public <E extends Enum<E>> OptionSetDef create(Class<?> commandClass, Class<E> optionClass) {
		final Command cmd = defineCommand(commandClass);
		final OptionSetDef def = new OptionSetDef(cmd, new TypeDef(optionClass), optionClass.getPackage().getName());
		defineOptions(optionClass, def);
		defineOptionSets(optionClass, def);
		return def;
	}

	private Command defineCommand(Class<?> commandClass) {
		final Name name = commandClass.getAnnotation(Name.class);
		return new Command(name != null ? name.value() : commandClass.getSimpleName(), commandClass);
	}

	private <E extends Enum<E>> void defineOptions(Class<E> optionClass, OptionSetDef def) {
		final E[] options = optionClass.getEnumConstants();
		for (int i = 0; i < options.length; i++) {
			final String name = getOptionAcronym(options[i]);
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

	private <E extends Enum<E>> void defineOptionSets(Class<E> optionClass, OptionSetDef def) {
		defineOptionSetsRecursive(optionClass, def, Collections.singletonList(EnumSet.allOf(optionClass)));
	}

	private <E extends Enum<E>> void defineOptionSetsRecursive(Class<E> optionClass, OptionSetDef def, Collection<EnumSet<E>> thisLevelActiveOptions) {
		final Set<EnumSet<E>> nextLevelActiveOptions = new LinkedHashSet<EnumSet<E>>();
		for (final EnumSet<E> active : thisLevelActiveOptions) {
			final OptionSet optionSet = defineOptionSet(def.optionSetType.simpleName, active);
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
			set.active.add(getOptionAcronym(active));
		}
		final EnumSet<E> inactiveOptions = EnumSet.complementOf(activeOptions);
		for (final E inactive : inactiveOptions) {
			activeOptions.add(inactive);
			final String nextName = getOptionSetName(optionSetClassName, activeOptions);
			set.next.put(getOptionAcronym(inactive), nextName);
			activeOptions.remove(inactive);
		}
		return set;
	}

	private <E extends Enum<E>> String getOptionSetName(String baseName, EnumSet<E> active) {
		final StringBuilder sb = new StringBuilder(baseName);
		for (final E act : active) {
			sb.append('_').append(getOptionAcronym(act));
		}
		return sb.toString();
	}
	private <E extends Enum<E>> String getOptionAcronym(E option) {
		try {
			final Object acronym = option.getClass().getMethod(Options.ACRONYM_METHOD_NAME).invoke(option);
			return acronym.toString();
		} catch (Exception e) {
			//ignore
			System.err.println("could not evaluate acronym for option " + option + " in class " + option.getClass().getName() + ", e=" + e);
		}
		//fallback: simply the option name
		return option.name();
	}
}
