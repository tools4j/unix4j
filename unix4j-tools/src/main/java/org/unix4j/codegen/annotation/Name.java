package org.unix4j.codegen.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code Name} annotation defines the name of a command, usually a lower
 * case string such as "ls".
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
	String value();
}
