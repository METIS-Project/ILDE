/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003 Harrie Martens and Hubert Vogten
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program (/license.txt); if not,
 * write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *
 *     Contact information:
 *     Open University of the Netherlands
 *     Valkenburgerweg 177 Heerlen
 *     PO Box 2960 6401 DL Heerlen
 *     e-mail: hubert.vogten@ou.nl or
 *             harrie.martens@ou.nl
 *
 *
 * Open Universiteit Nederland, hereby disclaims all copyright interest
 * in the program CopperCore written by
 * Harrie Martens and Hubert Vogten
 *
 * prof.dr. Rob Koper,
 * director of learning technologies research and development
 *
 */

package org.coppercore.common;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.ejb.EJBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.coppercore.exceptions.InvalidEscapeFormatException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class contains utility methods to parse xml documents from and to
 * streams.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.30 $, $Date: 2005/09/29 09:53:12 $
 */
public class Parser {

	/* attribute constants for XML factory */
	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	/** The IMS Content Package namespace uri. */
	public static final String IMSCPNS = "http://www.imsglobal.org/xsd/imscp_v1p1";

	/** The IMS Meta data namespace uri. */
	public static final String IMSMDNS = "http://www.imsglobal.org/xsd/imsmd_v1p2";

	/** The IMS Learning Design namespace uri. */
	public static final String IMSLDNS = "http://www.imsglobal.org/xsd/imsld_v1p0";

	/** The XML namespace uri. */
	public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";

	/** The CopperCore namespace uri. */
	public static final String CCNS = "http://coppercore.org/xsd/gc";

	/**
	 * Returns the value of the attribute with the specified name from the
	 * specified node. All leading and trailing spaces are removed.
	 * 
	 * <p>
	 * If the attribute cannot be found, or the attribute has no value, the method
	 * returns null.
	 * 
	 * @param node
	 *          Node the node to search the attribute for
	 * @param attributeName
	 *          String the name of the attribute to search for
	 * @return String the value of the attribute if found, otherwise null
	 */
	public static String getNamedAttribute(Node node, String attributeName) {
		Attr attrNode = ((Element) node).getAttributeNode(attributeName);
		if (attrNode != null) {
			return attrNode.getNodeValue().trim();
		}
		return null;
	}

	/**
	 * Returns the value of the specified attribute of the passed node. All
	 * leading and trailing spaces are removed.
	 * 
	 * <p>
	 * The name of the attribute is defined by the namespace uri and the local
	 * name of the attribute.
	 * 
	 * <p>
	 * If the attribute could not be found, or if the attribute has no value the
	 * method returns null.
	 * 
	 * @param node
	 *          Node the node to search the attribute of
	 * @param attributeName
	 *          String the local name of the attribute to look for
	 * @param NS
	 *          String the namespace uri of the attribute to look for
	 * @return String the value of the attribute or null if the attribute could
	 *         not be found or it has no value.
	 */
	public static String getNamedAttribute(Node node, String attributeName,
			String NS) {
		Attr attrNode = ((Element) node).getAttributeNodeNS(NS, attributeName);
		if (attrNode != null) {
			return attrNode.getNodeValue().trim();
		}
		return null;
	}

	/**
	 * Returns the actual value or the implied value for the passed node and
	 * attribute.
	 * <p>
	 * The value is read from the xml node defining this element and if the value
	 * is not found the method returns the implied value.
	 * 
	 * @param node
	 *          the Node for which the attribute should be retrieved
	 * @param attributeName
	 *          the name of the attribute to look for
	 * @param impliedValue
	 *          the value to return if the attribute was not found
	 * @return the actual or implied value of the attribute
	 */
	public static String getImpliedNamedAttribute(Node node,
			String attributeName, String impliedValue) {
		String result = ((Element) node).getAttribute(attributeName);
		if ("".equals(result)) {
			result = impliedValue;
		}
		return result;
	}

	/**
	 * Returns a DocumentBuilder using the passed schemas for validation purposes.
	 * 
	 * @param validating
	 *          boolean switches validation on or off
	 * @param nameSpaceAware
	 *          boolean switches name space awareness on or off
	 * @param schemas
	 *          String[] array of schema files that will be used for validation
	 * @param errorHandler
	 *          ErrorHandler the handler that deals with error messages
	 * @throws ParserConfigurationException
	 *           thrown when bound XML builder doesn't support one of the required
	 *           features
	 * @return DocumentBuilder the created documentbuilder
	 */
	public static DocumentBuilder getDocumentBuilder(boolean validating,
			boolean nameSpaceAware, String[] schemas, ErrorHandler errorHandler)
			throws ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating(validating);
		factory.setNamespaceAware(nameSpaceAware);
		factory.setAttribute(org.coppercore.common.Parser.JAXP_SCHEMA_LANGUAGE,
				org.coppercore.common.Parser.W3C_XML_SCHEMA);
		factory.setAttribute(org.coppercore.common.Parser.JAXP_SCHEMA_SOURCE,
				schemas);
		// disable loading of external dtd to prevent loading of doctype dtd when
		// non validating
		factory.setAttribute(
				"http://apache.org/xml/features/nonvalidating/load-external-dtd",
				new Boolean(false));

		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(errorHandler);

		return builder;
	}

	/**
	 * Returns a non-validating and none name space aware DocumentBuilder.
	 * 
	 * @param errorHandler
	 *          ErrorHandler specifies the handler to process the errors
	 *          encountered during the parsing of the document
	 * 
	 * @throws ParserConfigurationException
	 *           thrown when bound XML builder doesn't support one of the required
	 *           features
	 * @return DocumentBuilder the documentbuilder with the specified
	 *         configuration
	 */
	public static DocumentBuilder getDocumentBuilder(ErrorHandler errorHandler)
			throws ParserConfigurationException {
		String[] schemas = {};
		return getDocumentBuilder(false, false, schemas, errorHandler);
	}

	/**
	 * Returns a non validating SAXParser.
	 * 
	 * @param nameSpaceAware
	 *          boolean switches name space awareness on or off
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @return SAXParser
	 */
	public static SAXParser getParser(boolean nameSpaceAware)
			throws SAXException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		factory.setValidating(false);
		factory.setNamespaceAware(nameSpaceAware);

		SAXParser saxParser = factory.newSAXParser();

		return saxParser;

	}

	/**
	 * Returns a new SaxParser created using JAXP factory.
	 * 
	 * @param validating
	 *          boolean switches validation on or off
	 * @param nameSpaceAware
	 *          boolean switches name space awareness on or off
	 * @param schemas
	 *          String[] array of schema files that will be used for validation
	 * @return a new SaxParser
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static SAXParser getParser(boolean validating, boolean nameSpaceAware,
			String[] schemas) throws SAXException, ParserConfigurationException {

		SAXParserFactory factory = SAXParserFactory.newInstance();

		factory.setValidating(validating);
		factory.setNamespaceAware(nameSpaceAware);

		SAXParser saxParser = factory.newSAXParser();
		saxParser.setProperty(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);

		saxParser.setProperty(JAXP_SCHEMA_SOURCE, schemas);

		return saxParser;
	}

	/**
	 * Creates new Document based on the InputSource. The default
	 * ParserErrorHandler will be used.
	 * 
	 * @param anXMLDocument
	 *          InputSource for which document is created
	 * @param isNamespaceAware
	 *          switches name space awareness on or off
	 * @return Document that has been created
	 */
	public static Document createDomInstance(InputSource anXMLDocument,
			boolean isNamespaceAware) {
		Document document = null;

		try {
			String[] schemas = {};

			document = getDocumentBuilder(false, isNamespaceAware, schemas,
					new ParserErrorHandler()).parse(anXMLDocument);
		} catch (ParserConfigurationException ex) {
			Logger logger = Logger.getLogger("org.coppercore");
			logger.error(ex);
			throw new EJBException(ex);
		} catch (SAXParseException ex) {
			throw new EJBException(ex);
		} catch (SAXException ex) {
			throw new EJBException(ex);
		} catch (IOException ex) {
			// I/O error
			throw new EJBException(ex);
		}
		return (document);
	}

	/**
	 * Creates a Document from the specified xml document using a default error
	 * handler.
	 * 
	 * @param anXMLDocument
	 *          InputSource the xml document to parse
	 * @return Document the parsed xml document
	 */
	public static Document createDomInstance(InputSource anXMLDocument) {
		return createDomInstance(anXMLDocument, new ParserErrorHandler());
	}

	/**
	 * Creates a Document from the specified xml string using a default error
	 * handler.
	 * 
	 * @param xml
	 *          String the xml document to parse
	 * @return Document the parsed xml document
	 * @throws ParserConfigurationException
	 *           when there is an error creating a document builder
	 * @throws IOException
	 *           when there is an error converting the string to an inputsource
	 * @throws SAXException
	 *           when there is an error parsing the xml document
	 */
	public static Document createDomInstance(String xml)
			throws ParserConfigurationException, IOException, SAXException {
		return getDocumentBuilder(new ParserErrorHandler()).parse(
				inputSourceFromString(xml));
	}

	/**
	 * Creates new Document based on the InputSource and the passed
	 * DocumentBuilder.
	 * 
	 * @param anXMLDocument
	 *          InputSource for which document is created
	 * @param errorHandler
	 *          ErrorHandler to be used with this document
	 * @return Document that has been created
	 */
	public static Document createDomInstance(InputSource anXMLDocument,
			ErrorHandler errorHandler) {
		Document document = null;

		try {
			// build a document of the basis of a ByteArrayInputStream, so any
			// encoding issues will be dealt with by the parser
			document = getDocumentBuilder(errorHandler).parse(anXMLDocument);
		} catch (ParserConfigurationException ex) {
			Logger logger = Logger.getLogger("org.coppercore");
			logger.error(ex);
			throw new EJBException(ex);
		} catch (SAXParseException ex) {
			throw new EJBException(ex);
		} catch (SAXException ex) {
			throw new EJBException(ex);
		} catch (IOException ex) {
			// I/O error
			throw new EJBException(ex);
		}
		return (document);
	}

	/**
	 * returns the passed xml node as an indented String.
	 * <p>
	 * 
	 * @param doc
	 *          Node the xml node to convert
	 * @return String containing the converted xml document
	 */
	public static String documentToString2(Node doc) {

		try {
			// Serialisation through Transform.
			StringReader xslt = new StringReader(
					" <xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:xalan=\"http://xml.apache.org/xslt\">"
							+ "<xsl:output method=\"xml\" indent=\"yes\" xalan:indent-amount=\"2\"/>"
							+ "<xsl:template match=\"*\"><xsl:copy><xsl:copy-of select=\"@*\" /><xsl:apply-templates /></xsl:copy></xsl:template>"
							+ "<xsl:template match=\"comment()|processing-instruction()\"><xsl:copy /></xsl:template></xsl:stylesheet>");
			StringWriter out = new StringWriter();
			DOMSource domSource = new DOMSource(doc);
			StreamResult streamResult = new StreamResult(out);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = null;
			serializer = tf.newTransformer(new StreamSource(xslt));
			serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			if (doc instanceof Document) {
				DocumentType doctype = ((Document) doc).getDoctype();
				if (doctype != null) {
					String id = doctype.getPublicId();
					if (id != null) {
						serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, id);
					}
					id = doctype.getSystemId();
					if (id != null) {
						serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, id);
					}
				}

			}

			serializer.transform(domSource, streamResult);
			return out.toString();
		} catch (TransformerConfigurationException ex) {
			throw new EJBException(ex);
		} catch (TransformerException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Returns the specified xml node as a String.
	 * 
	 * @param doc
	 *          Node the xml node to convert
	 * @return String contains the converted xml node
	 */
	public static String documentToString(Node doc) {

		try {
			// Serialisation through Transform.
			StringWriter out = new StringWriter();
			DOMSource domSource = new DOMSource(doc);
			StreamResult streamResult = new StreamResult(out);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer serializer = null;
			serializer = tf.newTransformer();
			serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.METHOD, "xml");
			serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			if (doc instanceof Document) {
				DocumentType doctype = ((Document) doc).getDoctype();
				if (doctype != null) {
					String id = doctype.getPublicId();
					if (id != null) {
						serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, id);
					}
					id = doctype.getSystemId();
					if (id != null) {
						serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, id);
					}
				}
			}

			serializer.transform(domSource, streamResult);
			return out.toString();
		} catch (TransformerConfigurationException ex) {
			throw new EJBException(ex);
		} catch (TransformerException ex) {
			throw new EJBException(ex);
		}
	}

	/**
	 * Returns the complete text value of a node.
	 * 
	 * <p>
	 * The text of an xml node may be spread over more text nodes. This method
	 * loops over all TEXT_NODE to retrieve the complete textvalue for this node.
	 * 
	 * @param aNode
	 *          Node the node to retrieve the text value of
	 * @return String the retrieved text value
	 */
	public static String getTextValue(Node aNode) {
		StringBuffer result = new StringBuffer();
		Node child = aNode.getFirstChild();
		while (child != null) {
			if (child.getNodeType() == Node.TEXT_NODE) {
				result.append(child.getNodeValue());
			}
			child = child.getNextSibling();
		}
		return result.toString();
	}

	/**
	 * Converts a string to the form that is acceptable inside Xml files, escaping
	 * special characters.
	 * 
	 * @param s
	 *          the string to convert
	 * @return string with certain characters replaiced with their SGML entity
	 *         representations
	 */
	static public String escapeXML(String s) {
		StringBuffer result = new StringBuffer(s.length() * 6 / 5);

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			String esc = (c == '<') ? "&lt;" : (c == '>') ? "&gt;"
					: (c == '\'') ? "&#39;" : (c == '\"') ? "&quot;"
							: (c == '&') ? "&amp;" : null;

			if (esc == null)
				result.append(c);
			else
				result.append(esc);
		}

		return result.toString();
	}

	/**
	 * Converts escaped xml entities back to normal text.
	 * 
	 * @param text
	 *          String a xml string containg the escaped values
	 * @throws InvalidEscapeFormatException
	 *           if an invalid xml entity is encountered
	 * @return String the unescaped version of the passed text
	 */
	public static final String unescapeXML(String text)
			throws InvalidEscapeFormatException {
		int size = text.length();
		StringBuffer result = new StringBuffer(size);

		for (int i = 0; i < size; ++i) {
			char c = text.charAt(i);

			if (c == '&') {
				StringBuffer escapedSequence = new StringBuffer();

				++i;
				while (i < size) {
					c = text.charAt(i);
					if (c == ';')
						break;
					escapedSequence.append(c);
					++i;
				}

				if (escapedSequence.length() >= 2 && escapedSequence.charAt(0) == '#') {
					int j;

					try {
						if (escapedSequence.charAt(1) == 'x')

							// create an integer value based on the hexidecimal representation
							j = Integer.parseInt(escapedSequence.substring(2), 16);
						else

							// create an integer value based on the decimal representation
							j = Integer.parseInt(escapedSequence.substring(1));
					} catch (NumberFormatException e) {
						throw new InvalidEscapeFormatException(
								"Illegal numeric entity reference encountered");
					}

					if (j < 0 || j > Character.MAX_VALUE) {
						throw new InvalidEscapeFormatException(
								"Illegal numeric entity reference encountered");
					}
					// create character based on the numeric value
					c = (char) j;
				} else {
					String escapedToken = escapedSequence.toString();
					if (escapedToken.equals("amp")) {
						c = '&';
					} else if (escapedToken.equals("apos")) {
						c = '\'';
					} else if (escapedToken.equals("quot")) {
						c = '\"';
					} else if (escapedToken.equals("lt")) {
						c = '<';
					} else if (escapedToken.equals("gt")) {
						c = '>';
					} else {
						throw new EJBException("Unknown escape sequence " + escapedToken);
					}
				}
			}

			result.append(c);
		}

		return result.toString();
	}

	/**
	 * Returns an InputSource based on a passed input String. This method should
	 * be called whenever parsing a UTF-8 String with Xerces that contains a BOM
	 * 
	 * @param input
	 *          String
	 * @return InputSource
	 */
	public static InputSource inputSourceFromString(String input) {
		// only valid construct when preserving correct character encoding
		return new InputSource(new StringReader(input));
	}

	/**
	 * Transforms the xml string to an indented xml string.
	 * 
	 * <p>
	 * The resulting string also includes CR/LF's. This method is used to make an
	 * xml document visually more atractive for presentation purposes.
	 * 
	 * @param xmlBlob
	 *          String containing the xml document to indent
	 * @return String the indented xml document
	 */
	public static String indentXmlString(String xmlBlob) {
		// create a dom document from the xmlBlob
		Node root = createDomInstance(inputSourceFromString(xmlBlob));
		// indent the dom document and return as a string
		return documentToString2(root);
	}

}
