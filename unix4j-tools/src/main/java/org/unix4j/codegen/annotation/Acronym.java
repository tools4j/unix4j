package org.unix4j.codegen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The acronym annotation defines the one-letter abbreviation for an option; it
 * is applied to enum constants.
 * <p>
 * As an example, the {@code --longFormat} option of the {@code ls} command has
 * the well known acronym {@code -l}. In this example, the enum constant name
 * would be "longFormat"; the acronym {@link #value()} simply 'l';
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Acronym {
	/**
	 * Defines the acronym character for the annotated option (usually an enum
	 * constant}, for instance 'l' for the enum constant "longFormat".
	 * 
	 * @return the acronym character for the annotated option
	 */
	char value();
}
