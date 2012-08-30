package org.unix4j.codegen.command.def;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.unix4j.codegen.def.AbstractElementDef;
import org.unix4j.codegen.def.PackageDef;
import org.unix4j.codegen.def.TypeDef;

public class CommandDef extends AbstractElementDef {
	
	public CommandDef(String commandName, String className, String commandPackage, String name, String synopsis, String description) {
		this.commandName = commandName;
		this.command = new TypeDef(className);
		this.pkg = new PackageDef(commandPackage);
		this.name = name;
		this.synopsis = synopsis;
		this.description = description;
	}

	public final String commandName;		//the command name, such as "ls"
	public final TypeDef command;			//the comand type, e.g. org.unix4j.unix.Ls
	public final PackageDef pkg;			//package with command specific classes, e.g. org.unix4j.unix.ls
	public final String name;				//e.g. ls - list directory contents
	public final String synopsis;			//e.g. ls [-ahlRrt] [file...]
	public final String description;		//the html file body
	public final List<String> notes = new ArrayList<String>();
	public final List<MethodDef> methods = new ArrayList<MethodDef>();
	public final Map<String, OptionDef> options = new LinkedHashMap<String, OptionDef>();		//by (long) name
	public final Map<String, OperandDef> operands = new LinkedHashMap<String, OperandDef>();	//by name
	
}