package org.unix4j.codegen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.unix4j.annotation.Alias;

public class OptionDef {
	public Command command = new Command();;
	public String commandClass;
	public String packagePath;
	public String packageName;
	public String optionClassName;
	public String optionSetClassName;
	public Map<String, String> options = new LinkedHashMap<String, String>();
	public List<OptionSet> optionSets = new ArrayList<OptionSet>();

	@Override
	public String toString() {
		return "command:            " + command + "\n" +
				"commandClass:       " + commandClass + "\n" +
				"packagePath:        " + packagePath + "\n" +
				"packageName:        " + packageName + "\n" +
				"optionClassName:    " + optionClassName + "\n" +
				"optionSetClassName: " + optionSetClassName + "\n" +
				"options:            " + options + "\n" +
				"optionSets:         " + optionSets + "\n";
	}

	public static class Command {
		public String name;
		public String packageName;
		public String className;

		@Override
		public String toString() {
			return "\n\tname:        " + name + "\n" +
					"\tpackageName: " + packageName + "\n" +
					"\tclassName:   " + className + "\n";
		}
	}

	public static class OptionSet {
		public String name;
		public Set<String> active = new LinkedHashSet<String>();
		public Map<String, String> next = new LinkedHashMap<String, String>();

		@Override
		public String toString() {
			return "\n\tname:   " + name + "\n" +
					"\tactive: " + active + "\n" +
					"\tnext:   " + next + "\n";
		}
	}

	public static <E extends Enum<E>> OptionDef create(Class<E> optionClass) {
		final OptionDef def = new OptionDef();
		defineBasics(optionClass, def);
		defineCommand(optionClass, def);
		defineOptions(optionClass, def);
		defineOptionSets(optionClass, def);
		return def;
	}

	private static <E extends Enum<E>> void defineBasics(Class<E> optionClass, OptionDef def) {
		def.packagePath = optionClass.getPackage().getName().replace('.', '/');
		def.packageName = optionClass.getPackage().getName();
		def.optionClassName = optionClass.getSimpleName();
		def.optionSetClassName = optionClass.getSimpleName() + "Set";
	}

	private static <E extends Enum<E>> void defineCommand(Class<E> optionClass, OptionDef def) {
		// FIXME make annotation for this
		def.command.name = "ls";
		def.command.packageName = "org.unix4j.unix";
		def.command.className = "Ls";
	}

	private static <E extends Enum<E>> void defineOptions(Class<E> optionClass, OptionDef def) {
		final E[] options = optionClass.getEnumConstants();
		for (int i = 0; i < options.length; i++) {
			def.options.put(getOptionName(options[i]), options[i].name());
		}
	}

	private static <E extends Enum<E>> void defineOptionSets(Class<E> optionClass, OptionDef def) {
		defineOptionSetsRecursive(optionClass, def, Collections.singletonList(EnumSet.allOf(optionClass)));
	}

	private static <E extends Enum<E>> void defineOptionSetsRecursive(Class<E> optionClass, OptionDef def, Collection<EnumSet<E>> thisLevelActiveOptions) {
		final Set<EnumSet<E>> nextLevelActiveOptions = new LinkedHashSet<EnumSet<E>>();
		for (final EnumSet<E> active : thisLevelActiveOptions) {
			final OptionSet optionSet = defineOptionSet(def.optionSetClassName, active);
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
	private static <E extends Enum<E>> OptionSet defineOptionSet(String optionSetClassName, EnumSet<E> activeOptions) {
		final OptionSet set = new OptionSet();
		set.name = getOptionSetName(optionSetClassName, activeOptions);
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

	private static <E extends Enum<E>> String getOptionSetName(String baseName, EnumSet<E> active) {
		final StringBuilder sb = new StringBuilder(baseName);
		for (final E act : active) {
			sb.append('_').append(getOptionName(act));
		}
		return sb.toString();
	}
	private static <E extends Enum<E>> String getOptionName(E option) {
		try {
			final Alias alias = option.getClass().getField(option.name()).getAnnotation(Alias.class);
			return alias.value();
		} catch (Exception e) {
			//ignore
			System.err.println(e);
		}
		//fallback: simply the option name
		return option.name();
	}
}
