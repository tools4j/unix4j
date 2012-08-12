package org.unix4j.codegen.model.command;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.unix4j.codegen.xml.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Loads the XML command definition file and returns the contents as an fmpp
 * data model.
 */
public class CommandDefinitionLoader {
	private static enum XmlElement {
		name, synopsis, description, notes, 
		methods, method,
		options, option,
		operands, operand
	}
	private static enum XmlAttribtue {
		class_, package_, args, name, acronym, type
	}
	public CommandDef load(InputStream commandDefinition) {
		try {
			final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			final Document doc = builder.parse(commandDefinition);
			return load(doc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public CommandDef load(Document commandDefinition) {
		final Element elCommand = commandDefinition.getDocumentElement();
		final String commandName = elCommand.getNodeName();
		final String className = XmlUtil.getRequiredAttribute(elCommand, XmlAttribtue.class_);
		final String packageName = XmlUtil.getRequiredAttribute(elCommand, XmlAttribtue.package_);
		final String name = XmlUtil.getRequiredElementText(elCommand, XmlElement.name);
		final String synopsis = XmlUtil.getRequiredElementText(elCommand, XmlElement.synopsis);
		final CommandDef def = new CommandDef(commandName, className, packageName, name, synopsis);
		loadOptions(def, elCommand);
		loadOperands(def, elCommand);
		loadMethods(def, elCommand);
		return def;
	}

	private void loadOptions(CommandDef def, Element elCommand) {
		final Element elOptions = XmlUtil.getSingleChildElement(elCommand, XmlElement.options);
		final List<Element> list = XmlUtil.getChildElements(elOptions, XmlElement.option);
		for (final Element elOption : list) {
			final String name = XmlUtil.getRequiredAttribute(elOption, XmlAttribtue.name);
			final String acronym = XmlUtil.getRequiredAttribute(elOption, XmlAttribtue.acronym);
			final String desc = XmlUtil.getRequiredElementText(elOption);
			final OptionDef optDef = new OptionDef(name, acronym, desc);
			def.options.put(name, optDef);
		}
	}

	private void loadOperands(CommandDef def, Element elCommand) {
		final Element elOperands = XmlUtil.getSingleChildElement(elCommand, XmlElement.operands);
		final List<Element> list = XmlUtil.getChildElements(elOperands, XmlElement.operand);
		for (final Element elOperand : list) {
			final String name = XmlUtil.getRequiredAttribute(elOperand, XmlAttribtue.name);
			final String type = XmlUtil.getRequiredAttribute(elOperand, XmlAttribtue.type);
			final String desc = XmlUtil.getRequiredElementText(elOperand);
			final OperandDef opDef = new OperandDef(name, type, desc);
			def.operands.put(name, opDef);
		}
	}

	private void loadMethods(CommandDef def, Element elCommand) {
		final Element elMethods = XmlUtil.getSingleChildElement(elCommand, XmlElement.methods);
		final List<Element> list = XmlUtil.getChildElements(elMethods, XmlElement.method);
		for (final Element elMethod : list) {
			final String name = XmlUtil.getAttribute(elMethod, XmlAttribtue.name, def.commandName);
			final String args = XmlUtil.getAttribute(elMethod, XmlAttribtue.args);
			final String desc = XmlUtil.getRequiredElementText(elMethod);
			final MethodDef methodDef;
			if (args == null) {
				methodDef = new MethodDef(name, desc);
			} else {
				final String[] splitArgs = args.split(",");
				methodDef = new MethodDef(name, desc, splitArgs);
				validateMethodArgs(def, methodDef);
			}
			def.methods.add(methodDef);
		}
	}

	private void validateMethodArgs(CommandDef def, MethodDef methodDef) {
		for (final String arg : methodDef.args) {
			if (!def.operands.containsKey(arg)) {
				throw new IllegalArgumentException("method argument '" + arg + "' is missing in the operands list of the '" + def.commandName + "' command");
			}
		}
	}

}
