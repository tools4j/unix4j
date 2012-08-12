package org.unix4j.codegen.model.command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.unix4j.codegen.model.AbstractModelElement;
import org.unix4j.codegen.model.PackageDef;
import org.unix4j.codegen.model.TypeDef;

public class CommandDef extends AbstractModelElement {
	
	public CommandDef(String commandName, String className, String commandPackage, String name, String synopsis) {
		this.commandName = commandName;
		this.command = new TypeDef(className);
		this.commandPackage = new PackageDef(commandPackage);
		this.name = name;
		this.synopsis = synopsis;
	}

	public final String commandName;		//the command name, such as "ls"
	public final TypeDef command;			//the comand type, e.g. org.unix4j.unix.Ls
	public final PackageDef commandPackage;	//package with command specific classes, e.g. org.unix4j.unix.ls
	public final String name;				//e.g. ls - list directory contents
	public final String synopsis;			//e.g. ls [-ahlRrt] [file...]
	public final List<String> notes = new ArrayList<String>();
	public final List<MethodDef> methods = new ArrayList<MethodDef>();
	public final Map<String, OptionDef> options = new LinkedHashMap<String, OptionDef>();
	public final Map<String, OperandDef> operands = new LinkedHashMap<String, OperandDef>();
	
}