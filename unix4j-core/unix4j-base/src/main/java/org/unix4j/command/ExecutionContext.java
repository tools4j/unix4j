package org.unix4j.command;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.unix4j.processor.LineProcessor;
import org.unix4j.variable.VariableContext;

/**
 * The execution context encapsulates all information relevant during the
 * execution of a {@link Command} or chain of joined commands. It is passed to
 * the {@link Command#execute(ExecutionContext, LineProcessor) execute(..)}
 * method providing access to the current directory, environment variables and
 * other information useful for the commands during their execution.
 */
public interface ExecutionContext {
	File getCurrentDirectory();

	String getUser();

	File getUserHome();

	File getTempDirectory();
	
	Locale getLocale();

	Map<String, String> getEnv();

	Properties getSys();

	VariableContext getVariableContext();
}
