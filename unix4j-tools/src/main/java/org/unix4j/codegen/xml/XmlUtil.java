package org.unix4j.codegen.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlUtil {
	public static String getRequiredElementText(Element parent, Enum<?> child) {
		final Element elChild = getSingleChildElement(parent, child);
		return getRequiredElementText(elChild);
	}
	public static String getRequiredElementText(Element element) {
		String text = element.getTextContent();
		if (text != null) {
			text = text.trim();
		}
		if (text != null && !text.isEmpty()) {
			return text;
		}
		throw new IllegalArgumentException("text content missing or empty for XML element " + element.getNodeName());
	}
	public static String getRequiredAttribute(Element element, Enum<?> attribute) {
		final String value = getAttribute(element, attribute);
		if (value != null) {
			return value;
		}
		throw new IllegalArgumentException("attribute " + getXmlName(attribute) + " is missing or empty in XML element " + element.getNodeName());
	}
	/**
	 * Returns the attribute value or {@code defaultValue} if it does not exist.
	 */
	public static String getAttribute(Element element, Enum<?> attribute, String defaultValue) {
		final String value = getAttribute(element, attribute);
		if (value != null) {
			return value;
		}
		return defaultValue;
	}
	
	/**
	 * Returns the attribute value or {@code null} if it does not exist.
	 */
	public static String getAttribute(Element element, Enum<?> attribute) {
		final String attName = getXmlName(attribute);
		final String attValue = element.getAttribute(attName);
		return attValue == null || attValue.isEmpty() ? null : attValue;
	}
	
	public static String getXmlName(Enum<?> node) {
		String name = node.name();
		
		//remove leading or trailing underscore, it is used if a name conflicts with a Java keyword
		if (name.length() > 0 && name.charAt(0) == '_') {
			name = name.substring(1);
		}
		if (name.length() > 0 && name.charAt(name.length() - 1) == '_') {
			name = name.substring(0, name.length() - 1);
		}
		return name.replace('_', '-');//use dashes instead of underscores in XML
	}

	public static Element getSingleChildElement(Element parent, Enum<?> child) {
		final Element el = getSingleOptionalChildElement(parent, child);
		if (el != null) {
			return el;
		}
		throw new IllegalArgumentException("mandatory (single) child element " + getXmlName(child) + " missing for XML element " + parent.getNodeName());
	}
	public static Element getSingleOptionalChildElement(Element parent, Enum<?> child) {
		final String elName = getXmlName(child);
		final NodeList list = parent.getElementsByTagName(elName);
		if (list.getLength() == 1) {
			return (Element)list.item(0);
		}
		if (list.getLength() == 0) {
			return null;
		}
		throw new IllegalArgumentException("expected a single " + elName + " child element of XML element " + parent.getNodeName() + ", but found " + list.getLength());
	}

	public static List<Element> getChildElements(Element parent, Enum<?> child) {
		final String elName = getXmlName(child);
		final NodeList list = parent.getElementsByTagName(elName);
		final List<Element> children = new ArrayList<Element>(list.getLength());
		for (int i = 0; i < list.getLength(); i++) {
			final Element elChild = (Element)list.item(i);
			children.add(elChild);
		}
		return children;
	}
	
	//no instances
	private XmlUtil() {
		super();
	}
}
