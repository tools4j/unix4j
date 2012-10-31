package org.unix4j.convert;

import org.unix4j.option.DefaultOptionSet;
import org.unix4j.option.Option;
import org.unix4j.option.OptionSet;

public class OptionSetConverters {
	public static class OptionToSingletonSetConverter<O extends Enum<O> & Option> implements ValueConverter<OptionSet<O>> {
		private final Class<O> optionEnumClass;
		public OptionToSingletonSetConverter(Class<O> optionEnumClass) {
			this.optionEnumClass = optionEnumClass;
		}
		@Override
		public OptionSet<O> convert(Object value) throws IllegalArgumentException {
			if (optionEnumClass.isInstance(value)) {
				final O option = optionEnumClass.cast(value);
				return toSingletonSet(option);
			}
			return null;
		}
	}
	public static class OptionNameToSingletonSetConverter<O extends Enum<O> & Option> implements ValueConverter<OptionSet<O>> {
		private final Class<O> optionEnumClass;
		public OptionNameToSingletonSetConverter(Class<O> optionEnumClass) {
			this.optionEnumClass = optionEnumClass;
		}
		@Override
		public OptionSet<O> convert(Object value) throws IllegalArgumentException {
			if (value instanceof Object) {
				final String optionString = value.toString();
				if (optionString.startsWith("--")) {
					final O option;
					try {
						option = Enum.valueOf(optionEnumClass, optionString.substring(2));
					} catch (IllegalArgumentException e) {
						return null;
					}
					return toSingletonSet(option);
				} 
			}
			return null;
		}
	};
	public static class AcronymStringToOptionSetConverter<O extends Enum<O> & Option> implements ValueConverter<OptionSet<O>> {
		private final Class<O> optionEnumClass;
		private final O[] options;
		public AcronymStringToOptionSetConverter(Class<O> optionEnumClass) {
			this.optionEnumClass = optionEnumClass;
			this.options = optionEnumClass.getEnumConstants();
		}
		@Override
		public OptionSet<O> convert(Object value) throws IllegalArgumentException {
			if (value instanceof Object) {
				final String optionString = value.toString();
				if (optionString.startsWith("-")) {
					final DefaultOptionSet<O> set = new DefaultOptionSet<O>(optionEnumClass);
					set.setUseAcronymForAll(true);
					for (int i = 1; i < optionString.length(); i++) {
						final char acronym = optionString.charAt(i);
						O found = null;
						for (final O option : options) {
							if (option.acronym() == acronym) {
								found = option;
								break;
							}
						}
						if (found == null) {
							return null;
						}
						set.set(found);
					}
					return set;
				} 
			}
			return null;
		}
	};
	public static class OptionSetConverter<O extends Enum<O> & Option> extends CompositeValueConverter<OptionSet<O>> {
		public OptionSetConverter(Class<O> optionEnumClass) {
			add(new OptionToSingletonSetConverter<O>(optionEnumClass));
			add(new OptionNameToSingletonSetConverter<O>(optionEnumClass));
			add(new AcronymStringToOptionSetConverter<O>(optionEnumClass));
			add(ConcatenatedConverter.concat(new EnumConverters.StringToEnumConverter<O>(optionEnumClass), new OptionToSingletonSetConverter<O>(optionEnumClass)));
		}
	}
	
	private static <O extends Enum<O> & Option> OptionSet<O> toSingletonSet(O option) {
		if (option instanceof OptionSet) {
			@SuppressWarnings("unchecked")
			final OptionSet<O> set = (OptionSet<O>)option;
			return set;
		}
		return new DefaultOptionSet<O>(option);
	}
}
