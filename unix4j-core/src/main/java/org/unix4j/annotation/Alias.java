package org.unix4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface Alias {
	String value();
}
