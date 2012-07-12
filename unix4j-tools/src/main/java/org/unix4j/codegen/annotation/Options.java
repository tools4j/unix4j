package org.unix4j.codegen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The option annotation defines the options for a command. Options are defined
 * as enum constants, each constant representing an option.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Options {
	Class<? extends Enum<?>> value();
}
