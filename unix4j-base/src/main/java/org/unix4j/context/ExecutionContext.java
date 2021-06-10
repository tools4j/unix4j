package org.unix4j.context;

import org.unix4j.command.Command;
import org.unix4j.convert.ConverterRegistry;
import org.unix4j.convert.ValueConverter;
import org.unix4j.processor.LineProcessor;
import org.unix4j.variable.VariableContext;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * The execution context encapsulates all information relevant during the
 * execution of a {@link Command} or chain of joined commands. It is passed to
 * the {@link Command#execute(ExecutionContext, LineProcessor) execute(..)}
 * method providing access to the current directory, environment variables and
 * other information useful for the commands during their execution.
 */
public interface ExecutionContext {

	/**
	 * Returns the current directory, never null. If the current directory has
	 * not explicitly been changed, the file specified by the {@code "user.dir"}
	 * system property is returned.
	 * 
	 * @return the current directory, the same as the {@code "user.dir"} system
	 *         property it has not been set explicitly
	 * @see System#getProperties()
	 */
	File getCurrentDirectory();

	/**
	 * Returns the given file but relative to the {@link #getCurrentDirectory()
	 * current directory} if the given file path is not absolute. Most commands
	 * should resolve files through this method since the unix4j current
	 * directory and the Java current directory are not the same.
	 * 
	 * @param file
	 *            the file to return relative to the current directory if it
	 *            denotes a relative path
	 * @return the given file relative to the execution context's
	 *         {@link #getCurrentDirectory() current directory}
	 */
	File getRelativeToCurrentDirectory(File file);

	/**
	 * Returns the user name, usually defined by the {@code "user.name"} system
	 * property.
	 * 
	 * @return the user name, usually defined by the {@code "user.name"} system
	 *         property
	 * @see System#getProperties()
	 */
	String getUser();

	/**
	 * Returns the user home directory, usually defined by the
	 * {@code "user.home"} system property.
	 * 
	 * @return the user home directory, usually defined by the
	 *         {@code "user.home"} system property
	 * @see System#getProperties()
	 */
	File getUserHome();

	File getTempDirectory();

	Locale getLocale();

	Map<String, String> getEnv();

	Properties getSys();

	VariableContext getVariableContext();

	ConverterRegistry getConverterRegistry();

	<V> ValueConverter<V> getValueConverterFor(Class<V> type);
}
