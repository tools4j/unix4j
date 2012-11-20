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
	public static class NameOrAcronymToOptionConverter<O extends Enum<O> & Option> implements ValueConverter<O> {
		private final O[] options;
		public NameOrAcronymToOptionConverter(Class<O> optionEnumClass) {
			this.options = optionEnumClass.getEnumConstants();
		}
		@Override
		public O convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				final String optionString = value.toString();
				if (optionString.length() == 1) {
					for (final O option : options) {
						if (option.name().equals(optionString) || (optionString.length() == 1 && optionString.charAt(0) == option.acronym())) {
							return option;
						}
					}
				}
			}
			return null;
		}
	}
	public static class OptionNameStringToSingletonSetConverter<O extends Enum<O> & Option> implements ValueConverter<OptionSet<O>> {
		private final Class<O> optionEnumClass;
		public OptionNameStringToSingletonSetConverter(Class<O> optionEnumClass) {
			this.optionEnumClass = optionEnumClass;
		}
		@Override
		public OptionSet<O> convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				final String optionString = value.toString();
				if (optionString.startsWith("--")) {
					final String optionName = optionString.substring(2);  
					final O option;
					try {
						option = Enum.valueOf(optionEnumClass, optionName);
					} catch (IllegalArgumentException e) {
						return null;
					}
					return toSingletonSet(option);
				}
			}
			return null;
		}
	}
	public static class AcronymStringToOptionSetConverter<O extends Enum<O> & Option> implements ValueConverter<OptionSet<O>> {
		private final Class<O> optionEnumClass;
		private final O[] options;
		public AcronymStringToOptionSetConverter(Class<O> optionEnumClass) {
			this.optionEnumClass = optionEnumClass;
			this.options = optionEnumClass.getEnumConstants();
		}
		@Override
		public OptionSet<O> convert(Object value) throws IllegalArgumentException {
			if (value != null) {
				final String optionString = value.toString();
				if (optionString.startsWith("-")) {
					DefaultOptionSet<O> set = null;
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
						if (set == null) {
							set = new DefaultOptionSet<O>(optionEnumClass);
						}
						set.set(found);
						set.setUseAcronymFor(found, true);
					} 
					return set;
				}
			}
			return null;
		}
	}
	public static class IterableOfOptionNameOrAcronymToOptionSetConverter<O extends Enum<O> & Option> implements ValueConverter<OptionSet<O>> {
		private final Class<O> optionEnumClass;
		private final NameOrAcronymToOptionConverter<O> optionConverter;
		public IterableOfOptionNameOrAcronymToOptionSetConverter(Class<O> optionEnumClass) {
			this.optionEnumClass = optionEnumClass;
			this.optionConverter = new NameOrAcronymToOptionConverter<O>(optionEnumClass);
		}
		@Override
		public OptionSet<O> convert(Object value) throws IllegalArgumentException {
			if (value instanceof Iterable) {
				final Iterable<?> iterable = (Iterable<?>)value;
				DefaultOptionSet<O> set = null;
				for (final Object optionNameOrAcronym : iterable) {
					final O option = optionConverter.convert(optionNameOrAcronym);
					if (option == null) {
						return null;
					}
					if (set == null) {
						set = new DefaultOptionSet<O>(optionEnumClass);
					}
					set.set(option);
					set.setUseAcronymFor(option, !option.name().equals(optionNameOrAcronym));
				}
				return set;
			}
			return null;
		}
	}
	
	public static class OptionSetConverter<O extends Enum<O> & Option> extends CompositeValueConverter<OptionSet<O>> {
		public OptionSetConverter(Class<O> optionEnumClass) {
			add(new OptionToSingletonSetConverter<O>(optionEnumClass));
			add(new IterableOfOptionNameOrAcronymToOptionSetConverter<O>(optionEnumClass));
			add(new ConcatenatedConverter<OptionSet<O>>(new NameOrAcronymToOptionConverter<O>(optionEnumClass), new OptionToSingletonSetConverter<O>(optionEnumClass)));
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
