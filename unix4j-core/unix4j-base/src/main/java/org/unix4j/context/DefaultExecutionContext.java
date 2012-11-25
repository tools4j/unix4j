package org.unix4j.context;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.unix4j.convert.ConverterRegistry;
import org.unix4j.convert.DefaultConverterRegistry;
import org.unix4j.convert.ValueConverter;
import org.unix4j.variable.StackableVariableContext;
import org.unix4j.variable.VariableContext;


public class DefaultExecutionContext implements ExecutionContext {
	private String user; 
	private File userHome; 
	private File tempDirectory; 
	private File currentDirectory; 
	private Locale locale;
	private VariableContext variableContext = null;//lazy init
	private ConverterRegistry converterRegistry = null;//lazy init
	
	public DefaultExecutionContext() {
		this.currentDirectory = null;//default
	}
	public DefaultExecutionContext(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
	public void setCurrentDirectory(File currentDirectory) {
		this.currentDirectory = currentDirectory;
	}
	@Override
	public File getCurrentDirectory() {
		if (currentDirectory == null) {
			currentDirectory = new File(System.getProperty("user.dir"));
		}
		return currentDirectory;
	}
	@Override
	public String getUser() {
		if (user == null) {
			user = System.getProperty("user.name");
		}
		return user;
	}
	@Override
	public File getUserHome() {
		if (userHome == null) {
			userHome = new File(System.getProperty("user.home"));
		}
		return userHome;
	}
	@Override
	public File getTempDirectory() {
		if (tempDirectory == null) {
			tempDirectory = new File(System.getProperty("java.io.tmpdir"));
		}
		return tempDirectory;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	@Override
	public Locale getLocale() {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		return locale;
	}
	@Override
	public Map<String, String> getEnv() {
		return System.getenv();
	}
	@Override
	public Properties getSys() {
		return System.getProperties();
	}
	@Override
	public VariableContext getVariableContext() {
		if (variableContext == null) {
			variableContext = new StackableVariableContext();
		}
		return variableContext;
	}
	@Override
	public <V> ValueConverter<V> getValueConverterFor(Class<V> type) {
		if (converterRegistry == null) {
			converterRegistry = new DefaultConverterRegistry();
		}
		return converterRegistry.getValueConverterFor(type);
	}
	
}
