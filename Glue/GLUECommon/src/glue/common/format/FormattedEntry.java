/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletCommon.
 * 
 * GlueletCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.common.format;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.atom.Content;
import org.restlet.ext.atom.Entry;
import org.restlet.ext.atom.Link;
import org.restlet.ext.atom.Person;
import org.restlet.ext.atom.Relation;
import org.restlet.ext.atom.Text;
import org.restlet.ext.xml.DomRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * GLUE-formatted Atom entry 
 *
 * @author  David A. Velasco
 * @version 2012092501
 * @package glue.common.format
 * @TODO	complete until API consistence
 */
public class FormattedEntry {

	/**
	 * Constructor for a new, empty entry
	 */
	public FormattedEntry() {
		entry = new Entry();
		entry.setNamespaceAware(true);
		doc = null;
		xhtmlContent = null;
		textContent = null;
		structuredChildren = null;
		textChildren = null;
	}
	

	/**
	 * Constructor for an existing entry
	 *  
	 * @param entryInAnInputStream
	 */
	public FormattedEntry(InputStream entryInAnInputStream) throws ResourceException {
		try {
			DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
			docBF.setNamespaceAware(true);
			DocumentBuilder docB = docBF.newDocumentBuilder();
			doc = docB.parse(entryInAnInputStream);
		} catch (ParserConfigurationException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while creating an empty DOM document for parsing a formatted entry ", e);
		
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "File access error while parsing a formatted entry ", e);
			
		} catch (SAXException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while parsing a formatted entry ", e);
		
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown exception while parsing a formatted entry ", e);
		}
		if (doc == null) {
			// parsing was wrong - build an empty formatted entry
			entry = new Entry();
			entry.setNamespaceAware(true);
			doc = null;
			xhtmlContent = null;
			textContent = null;
			structuredChildren = null;
			textChildren = null;

		} else {
			entry = null;
		}
	}

	/**
	 * Constructor for an existing entry
	 *  
	 * @param serializedEntry
	 * @throws UnsupportedEncodingException 
	 */
	public FormattedEntry(String serializedEntry) throws UnsupportedEncodingException {
		this(new ByteArrayInputStream(serializedEntry.getBytes("UTF-8")));
	}
	
	
	/**
	 * Constructor for an entry coming from an existing feed yet converted to text representation.
	 * 
	 * See FormattedFeed.getEntries() to understand.
	 * 
	 * @param entryNode 	DOM node corresponding to an Atom entry
	 */
	public FormattedEntry(Node entryNode) throws ResourceException {
		try {
			DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
			docBF.setNamespaceAware(true);
			DocumentBuilder docB = docBF.newDocumentBuilder();
			doc = docB.newDocument();
			doc.appendChild(doc.importNode(entryNode, true));
			
		} catch (ParserConfigurationException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while creating an empty DOM document for parsing a formatted entry ", e);
		
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown exception while parsing a formatted entry ", e);
		}
	}

	/**
	 * Setter for Atom content element in XHTML format
	 * 
	 * @param	content		DOM Document with the content to be inserted as child of the <content> element 
	 */
	public void setXhtmlContent(Document doc) {
		if (entry != null) {
			xhtmlContent = doc;
		}		
	}
	
	
	/**
	 * Setter for Atom content element in text format
	 * 
	 * @param text		Text to be inserted as child of the <content> element
	 */
	public void setTextContent(String text) {
		if (entry !=null) {
			textContent = text;
		}
	}
	
	
	/**
	 * Setter for Atom content element as a reference to the actual content 
	 * 
	 * @param	uri		URI to the external content 
	 */
	public void setOutOfLineContent(String uri) {
		if (entry != null) {
			Content content = new Content();
			content.setExternalType(MediaType.TEXT_HTML);	// media type SHOULD be provided, but it's not mandatory, and it's advisory
			content.setExternalRef(new Reference(uri));
			entry.setContent(content);
		}		
	}
	
	
	/**
	 * Setter for Atom id field
	 *   
	 * @param id	String to set as entry identifier
	 */
	public void setId(String id) {
		if (entry != null)
			entry.setId(id);
		else {
			Element idEl = (Element) doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "id").item(0);	// TODO create when non existing
			idEl.setTextContent(id);
		}
	}
	
	/**
	 *  Getter for Atom id field
	 * 
	 *  @return		Entry identifier
	 */
	public String getId() {
		if (entry != null)
			return entry.getId();
		else {
			//FormatStatic.printXML((DomRepresentation)this.getRepresentation());
			Element idEl = (Element) doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "id").item(0);
			return idEl.getTextContent();
		}
	}
	
	/**
	 * Setter for Atom update field
	 *   
	 * @param date	Entry update date to set
	 */
	public void setUpdated(Date date) {
		if (entry != null) {
			entry.setUpdated(date);
		}
	}
	
	/**
	 * Setter for Atom title field
	 *   
	 * @param title		Entry title to set
	 */
	public void setTitle(String title) {
		if (entry != null)
			entry.setTitle(new Text(MediaType.TEXT_PLAIN, title));
	}

	/**
	 * Getter for Atom title field
	 *   
	 * @return		Entry title
	 */
	public String getTitle() {
		if (entry != null)
			return entry.getTitle().getContent();
		else {
			Element titleEl = (Element) doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "title").item(0);
			return titleEl.getTextContent();
		}
	}

	
	/**
	 *  Getter for content entry, when "type" attribute is "text"
	 * 
	 *  @return		String		Text content, or null if there's no content or if it's not "text"
	 */
	public String getTextContent() {
		if (entry != null) {
			if (textContent != null) {
				return textContent;
			}
			if (xhtmlContent != null) {
				
			}

		} else if (doc != null) {
			NodeList contents = doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "content");
			if (contents == null);
				//System.out.println("UUUPS - no se encuentra atom:content !!");
			else {
				Element contentEl = (Element) contents.item(0);
				String attType = contentEl.getAttribute("type");
				if (attType == null) {
					//System.out.println("UUUPS - no se encuentra atom:content !!");
					
				} else if (attType.equals("text")) {
					return contentEl.getTextContent();
				}
			}	
		}

		return null;
	}
	
	
	/**
	 *  Getter for content entry, when "type" attribute is "xhtml"; serialized in text form
	 * 
	 * @return		XHTML content, or null if there's no content or if it's not "text"
	 * @throws TransformerException 
	 * @throws TransformerConfigurationException 
	 */
	public String getXhtmlContentAsText() throws TransformerConfigurationException, TransformerException {
		Node content = getXhtmlContent();
		if (content != null)
			return FormatStatic.xmlToString(content);
		return null;
	}
	

	/**
	 *  Getter for content entry, when "type" attribute is "xhtml"
	 * 
	 *  @return		XHTML content, or null if there's no content or if it's not "xhtml"
	 */
	public Node getXhtmlContent() {
		if (entry != null) {
			if (xhtmlContent != null) {
				return xhtmlContent;
			}

		} else if (doc != null) {
			NodeList contents = doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "content");
			if (contents == null);
				//System.out.println("UUUPS - no se encuentra atom:content !!");
			else {
				Element contentEl = (Element) contents.item(0);
				String attType = contentEl.getAttribute("type");
				if (attType == null) {
					//System.out.println("UUUPS - no se encuentra atom:content !!");
				} else {
					if (attType.equals("xhtml")) {
						DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
						docBF.setNamespaceAware(true);
						//DocumentBuilder docB;
						return contentEl.getFirstChild();
						/*try {
							/*docB = docBF.newDocumentBuilder();
							Document result = docB.newDocument();
							result.appendChild(result.importNode(contentEl, true));
							return result;
						} catch (ParserConfigurationException e) {
							System.out.println("UUUPS - no se puedo crear el objeto respuesta");
						}*/
					}
				}
			}	
		}

		return null;
	}
	
	

	/**
	 * Adds info about one of the entry authors
	 * 
	 * @param 	name	Author name
	 * @param	email	Author email
	 * @param	uri		Author URI
	 */
	public void addAuthor(String name, String email, String uri) {
		if (entry != null) {
			Person p = new Person();
			if (name != null && name.length() > 0)
				p.setName(name);
			if (email != null && email.length() > 0)
				p.setEmail(email);
			if (uri != null && uri.length() > 0)
				p.setUri(new Reference(uri));
			entry.getAuthors().add(p);
		}
	}
	
	/**
	 * Getter for list of authors
	 * 
	 * @return		List of authors
	 */
	public List<Person> getAuthors() {
		if (entry != null) {
			return entry.getAuthors();

		} else {
			NodeList authorNodes = doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "author");
			String name, uri, email, tag;
			NodeList authorData; Node authorPieceOfData;
			List<Person> result = new ArrayList<Person>();
			for (int i=0; i<authorNodes.getLength(); i++) {
				authorData = authorNodes.item(i).getChildNodes();
				name = ""; uri = ""; email = "";
				for (int j=0; j<authorData.getLength(); j++) {
					authorPieceOfData = authorData.item(j);
					if (authorPieceOfData.getNodeType() == Node.ELEMENT_NODE) {
						tag = authorPieceOfData.getNodeName();
						if (tag.equals("name"))
							name = authorPieceOfData.getTextContent();
						else if (tag.equals("uri"))
							uri = authorPieceOfData.getTextContent();
						else if (tag.equals("email"))
							email = authorPieceOfData.getTextContent();
					}
				}
				result.add(new Person(name, new Reference(uri), email));
			}
			return result;
		}
		
	}
	
	/**
	 * Getter for list of links
	 * 
	 * @return		List of links (hrefs)
	 */
	public List<String> getLinks() {
		List<String> result = new ArrayList<String>();
		if (entry != null) {
			List<Link> links = entry.getLinks();
			int count = links.size();
			for (int i=0; i < count; i++)
				result.add(links.get(i).getHref().toString());
			
		} else {
			NodeList linkNodes = doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "link");
			int count = linkNodes.getLength();
			Node linkNode;
			for (int i=0; i < count; i++) {
				linkNode = linkNodes.item(i);
				if (linkNode.getNodeType() == Node.ELEMENT_NODE)
					result.add(((Element)linkNode).getAttribute("href")); 
			}
		}
		return result;
	}
	
	
	/**
	 * Adds the needed link with attribute rel="alternate"
	 * 
	 * @param title		Link title
	 * @param uri		URI referenced by the link
	 */
	public void setAlternateLink(String title, String uri) {
		if (entry != null) {
			Link altLink = new Link();
			altLink.setHref(new Reference(uri));
			altLink.setRel(Relation.ALTERNATE);
			if (title != null && title.length() > 0)
				altLink.setTitle(title);
			entry.getLinks().add(altLink);
		}
	}
	
	
	/**
	 * Adds the needed link with attribute rel="related"
	 * 
	 * @param title		Link title
	 * @param uri		URI referenced by the link
	 */
	public void setRelatedLink(String title, String uri) {
		if (entry != null) {
			Link altLink = new Link();
			altLink.setHref(new Reference(uri));
			altLink.setRel(Relation.RELATED);
			if (title != null && title.length() > 0)
				altLink.setTitle(title);
			entry.getLinks().add(altLink);
		}
	}
	
	
	/**
	 * Adds a new simple element as a child of <entry>, with the text content specified in 'textContent'
	 * 
	 * @param tag			Tag name 
	 * @param textContent	Text content for the new element.
	 */
	public void addExtendedTextChild(String tag, String textContent) {
		if (entry != null) {
			if (textChildren == null)
				textChildren = new HashMap<String, String>();
			textChildren.put(tag, textContent);
		}
	}
	
	
	/**
	 * Adds a list of elements with text content as children of a new element, which is added as a new child of the <entry> element. 
	 * 
	 * @param listTag		Tag name for the list element
	 * @param itemTag		Tag name for every item element
	 * @param textContents	Array of Strings, text content for each of the item elements.
	 * 
	 *  TODO make much better, please...
	 */
	public void addExtentedStructuredList(String listTag, String itemTag, String[] textContents) {
		if (entry != null) {
			if (listChildren == null) {
				listChildren = new HashMap<String, String>();
				textContentsInListChildren = new HashMap<String, String []>();
			}
			listChildren.put(listTag, itemTag);
			textContentsInListChildren.put(listTag, textContents);
		}
		
	}
	
	
	/**
	 * Adds a new structured element with name 'tag', parsing the text in 'serializedXml'.
	 * 
	 * @param tag			Tag name 
	 * @param serializedXml	Serialized XML document to be 'hung' from the new element
	 */
	public void addExtendedStructuredElement(String tag, String serializedXml) {
		if (entry != null) {
			if (structuredChildren == null)
				structuredChildren = new HashMap<String, Element>();
			
			Document lDoc = null;
			try {
				DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
				docBF.setNamespaceAware(true);
				DocumentBuilder docB = docBF.newDocumentBuilder();
				InputStream is = new ByteArrayInputStream(serializedXml.getBytes("UTF-8"));
				lDoc = docB.parse(is);
			} catch (ParserConfigurationException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while creating an empty DOM document for parsing a new structured element ", e);
			
			} catch (IOException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "File access error while parsing a new structured element ", e);
				
			} catch (SAXException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while parsing a new structured element ", e);
			
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown exception while parsing a new structured element ", e);
			}
			if (lDoc != null) {
				Element newEl = lDoc.createElementNS(FormatStatic.GLUE_NAMESPACE, FormatStatic.GLUE_PREFIX + ":" + tag);
				structuredChildren.put(tag, newEl);	// it can't be cloned, no DOM document as target of import // TODO better solution!!!
				
				NodeList nodes = lDoc.getChildNodes();
				for (int i=0; i<nodes.getLength(); i++)	{
					if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE)
						newEl.appendChild((Element) nodes.item(i));
				}
				
				//System.out.println("HERE WE GO: " + FormatStatic.xmlToString(newEl));
			}
		}
	}
	
	
	/**
	 * Returns the text content of a simple extension element
	 * 
	 * @param tag		Tag name 
	 * @return			Text content for the new element.
	 */
	public String getExtendedTextChild(String tag) {
		if (entry != null) {
			if (textChildren != null)
				return textChildren.get(tag);
		} else {
			Element extendedChildEl = (Element) doc.getElementsByTagNameNS(FormatStatic.GLUE_NAMESPACE, tag).item(0);
			if (extendedChildEl != null)
				return extendedChildEl.getTextContent();
		}
		return null;
	}
	
	
	/**
	 * Returns a GLUE-specific element, if existing
	 * 
	 * @param 	tag		String 		Tag name
	 * @erturn			Element		First element child of <entry> with tag name 'tag'
	 */
	public Element getExtendedStructuredChild(String tag) {
		if (entry != null) {
			if (structuredChildren != null)
				return structuredChildren.get(tag);

		} else {
			return (Element) doc.getElementsByTagNameNS(FormatStatic.GLUE_NAMESPACE, tag).item(0);
		}
		return null;
	}
	
	
	/**
	 * Returns a GLUE-specific parameter consisting in a list of string values.
	 * 
	 * @param	tag		String			Tag name of the list element.
	 * @return			List<String>	List of string values, children of the list element
	 */
	public List<String> getExtendedStructuredList(String tag) {
		Element listEl = getExtendedStructuredChild(tag);
		List<String> result = null;
		if (listEl != null) {
			NodeList children = listEl.getChildNodes();
			int length = children.getLength();
			if (length > 0) {
				result = new ArrayList<String>();
				Node child = null;
				for (int i=0; i<length; i++) {
					child = children.item(i);
					if (child.getNodeType() == Node.ELEMENT_NODE)
						result.add(((Element)child).getTextContent());
				}
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param source	FormattedEntry	Entry to copy from	
	 * @param tag		String			Name of the element(s) to copy 
	 */
	public void copyExtendedElement(FormattedEntry source, String tag) {
		if (entry != null && source.doc != null) {		// just for now
			if (structuredChildren == null)
				structuredChildren = new HashMap<String, Element>();
			NodeList nodes = source.doc.getDocumentElement().getElementsByTagNameNS(FormatStatic.GLUE_NAMESPACE, tag);
			Element el = null;
			for (int i=0; i<nodes.getLength(); i++) {
				el = (Element) nodes.item(i);
				structuredChildren.put(el.getNodeName(), el);	// it can't be cloned, there's no a DOM document yet	// TODO better solution!!!
			}
		} else {
			//TODO
		}
	}
	

	/**
	 * Builds and returns a RESTlet DOM representation with the current data in the entry.
	 * 
	 * @return	Representation		RESTlet DOM representation containing the Atom entry
	 */
	public Representation getRepresentation() {
		if (doc == null) {
			/// convert org.restlet.ext.atom.Entry to org.restlet.ext.xml.DomRepresentation
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			try {
				entry.write(buffer);
			} catch (IOException e) {
				throw new RuntimeException("Weird issue while trying to dump XML data: " + e.getMessage(), e);
			}
			InputStream is = new ByteArrayInputStream(buffer.toByteArray());

			doc = null;
			try {
				DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
				docBF.setNamespaceAware(true);
				DocumentBuilder docB = docBF.newDocumentBuilder();
				doc = docB.parse(is);
		
			} catch (ParserConfigurationException e) {
				System.err.println("Error while creating an empty DOM document for parsing configuration form: " + e.getMessage());
				return null;
			
			} catch (IOException e) {
				System.err.println("File access error while parsing configuration form: " + e.getMessage());
				return null;
			
			} catch (SAXException e) {
				System.err.println("Error while parsing configuration form: " + e.getMessage());
				return null;
		
			} catch (Exception e) {
				System.err.println("Unknown error: " + e.getMessage());
				return null;
			}
			
			/// add XHTML content in the right way (RESTlet doesn't)
			if (xhtmlContent != null) {
				Element contentEl = doc.createElement("content");
				contentEl.appendChild(doc.importNode(xhtmlContent.getDocumentElement(), true));
				contentEl.setAttribute("type", "xhtml");
				doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "entry").item(0).appendChild(contentEl);
			}
			if (textContent != null) {
				Element contentEl = doc.createElement("content");
				contentEl.setTextContent(textContent);
				contentEl.setAttribute("type", "text");
				doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "entry").item(0).appendChild(contentEl);
			}
			
			/// add GLUE-specific text elements
			Element entryEl = (Element) doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "entry").item(0);
			entryEl.setAttribute("xmlns:" + FormatStatic.GLUE_PREFIX, FormatStatic.GLUE_NAMESPACE);
			if (textChildren != null) {
				Iterator<String> tagIt = textChildren.keySet().iterator();
				String tag; Element child;
				while (tagIt.hasNext()) {
					tag = tagIt.next();
					child = doc.createElementNS(FormatStatic.GLUE_NAMESPACE, FormatStatic.GLUE_PREFIX + ":" + tag);
					child.setTextContent(textChildren.get(tag));
					entryEl.appendChild(child);
				}
			}
			
			/// add GLUE-specific structured elements
			if (structuredChildren != null) {
				Iterator<String> tagIt = structuredChildren.keySet().iterator();
				String tag;
				while (tagIt.hasNext()) {
					tag = tagIt.next();
					entryEl.appendChild(doc.importNode(structuredChildren.get(tag), true));
				}
			}
			
			/// add more GLUE-specific structured elements
			if (listChildren != null) {
				Iterator<String> listTagIt = listChildren.keySet().iterator();
				String listTag, itemTag;
				Element listEl, itemEl;
				while (listTagIt.hasNext()) {
					listTag = listTagIt.next();
					itemTag = listChildren.get(listTag);
					listEl = (Element) entryEl.appendChild(doc.createElementNS(FormatStatic.GLUE_NAMESPACE, FormatStatic.GLUE_PREFIX + ":" + listTag));
					String [] textContents = textContentsInListChildren.get(listTag);
					for (int i=0; i<textContents.length; i++) {
						itemEl = (Element) listEl.appendChild(doc.createElementNS(FormatStatic.GLUE_NAMESPACE, FormatStatic.GLUE_PREFIX + ":" + itemTag));
						itemEl.setTextContent(textContents[i]);
					}
				}
			}
		
			entry = null;
			textChildren = null;
			structuredChildren = null;
		}
	
		DomRepresentation rep = null;
		try {
			rep = new DomRepresentation();
			rep.setMediaType(MediaType.APPLICATION_ATOM);
			rep.setCharacterSet(CharacterSet.UTF_8);
			rep.setDocument(doc);
		
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while creating representation ", e);
		}
		
		return rep;
	}
	

	/**
	 * RESTlet Atom entry wrapped by FormattedEntry
	 */
	protected Entry entry;
	
	/**
	 * Text elements added for GLUE-specific format.
	 * 
	 * The insertion is delayed until getRepresentation() is called.
	 */
	protected HashMap<String, String> textChildren = null;
	
	/**
	 * Structured elements added for GLUE-specific format (dangerous...)
	 * 
	 * The insertion is delayed until getRepresentation() is called.
	 */
	protected HashMap<String, Element> structuredChildren = null;
	
	/**
	 * Elements added for GLUE-specific format as direct children of <entry> element, parents of another level of elements.
	 * 
	 * The insertion is delayed until getRepresentation() is called.
	 */
	protected HashMap<String, String> listChildren = null;

	/**
	 * Elements added for GLUE-specific format as children of an element in listChildren.
	 * 
	 * The insertion is delayed until getRepresentation() is called.
	 */
	protected HashMap<String, String[]> textContentsInListChildren = null;
		
	/**
	 * Wrapped DOM document
	 */
	protected Document doc;
	
	/**
	 * Entry content in XHTML format, loaded as a DOM Document
	 */
	protected Document xhtmlContent;
	
	/**
	 * Entry content in text format
	 */
	protected String textContent;
	
}
