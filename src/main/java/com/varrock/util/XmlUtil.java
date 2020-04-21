package com.varrock.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This utility file is used for xml parsing purposed
 * used throughout the server.
 * 
 * @author relex lawl
 */
public final class XmlUtil {

	/**
	 * Gets tag value from XML entry.
	 * @param tag			XML tag String.
	 * @param xmlElement	XML Element Object.
	 * @return				Integer.parseInt(((Node) lastNodeList.item(0)).getNodeValue()).
	 */
	public static String getEntry(String tag, Element xmlElement) {
		NodeList nodeList = xmlElement.getElementsByTagName(tag);
	    Element element = (Element) nodeList.item(0);
	    NodeList lastNodeList = element.getChildNodes();
		return lastNodeList.item(0).getNodeValue();
	}

	/**
	 * Gets tag value from XML entry.
	 * @param tag			XML tag String.
	 * @param xmlElement	XML Element Object.
	 * @return				Integer.parseInt(((Node) lastNodeList.item(0)).getNodeValue()).
	 */
	public static boolean elementExists(String tag, Element xmlElement) {
		NodeList nodeList = xmlElement.getElementsByTagName(tag);
	    Element element = (Element) nodeList.item(0);
	    return element != null;
	}
	
	/**
	 * Gets the nodes from the list with said name.
	 * @param doc		The xml document to read from.
	 * @param nodeName	The name of the tag.
	 * @return			The NodeList for said tag.
	 */
	public static NodeList getNodesByName(Document doc, String nodeName) {
		Element docEle = doc.getDocumentElement();
		NodeList list = docEle.getElementsByTagName(nodeName);
		return list;
	}
}
