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

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.atom.Feed;
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
 * GLUE-formatted Atom feed 
 *
 * @author  David A. Velasco
 * @version 2012092501
 * @package glue.common.format
 */
public class FormattedFeed {
	
	/**
	 * Constructor for a new, empty feed
	 */
	public FormattedFeed() {
		feed = new Feed();
		feed.setNamespaceAware(true);
		doc = null;
		entries = new ArrayList<FormattedEntry>();
	}
	
	/**
	 * Constructor for an existing feed
	 *  
	 * @param serializedFeed	Feed content in an InputStream	
	 */
	public FormattedFeed(InputStream serializedFeed) {
		try {
			DocumentBuilderFactory docBF = DocumentBuilderFactory.newInstance();
			docBF.setNamespaceAware(true);
			DocumentBuilder docB = docBF.newDocumentBuilder();
			doc = docB.parse(serializedFeed);
		} catch (ParserConfigurationException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while creating an empty DOM document for parsing a formatted feed ", e);
		
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "File access error while parsing a formatted feed ",e);
			
		} catch (SAXException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while parsing a formatted feed ", e);
		
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown exception while parsing a formatted feed ", e);
		}
		if (doc == null) {
			// parsing was wrong - build an empty formatted feed
			feed = new Feed();
			feed.setNamespaceAware(true);
			doc = null;
			entries = new ArrayList<FormattedEntry>();

		} else {
			feed = null;
			entries = null;
		}
	}
	
	
	/**
	 * Constructor for an existing feed
	 *  
	 * @param String	Feed content in a text string	
	 * @throws UnsupportedEncodingException 
	 */
	public FormattedFeed(String serializedFeed) throws UnsupportedEncodingException {
		this(new ByteArrayInputStream(serializedFeed.getBytes("UTF-8")));
	}
	
	
	/**
	 * Setter for Atom id field
	 *   
	 * @param id	String to set as feed identifier
	 */
	public void setId(String id) {
		if (feed != null)
			feed.setId(id);
	}
	
	/**
	 *  Getter for Atom id field
	 * 
	 *  @return		Feed identifier
	 */
	public String getId() {
		if (feed != null)
			return feed.getId();
		else {
			Element idEl = (Element) doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "id").item(0);
			return idEl.getTextContent();
		}
	}
	
	/**
	 * Setter for Atom update field
	 *   
	 * @param date	Feed update date to set
	 */
	public void setUpdated(Date date) {
		if (feed != null)
			feed.setUpdated(date);
	}
	
	/**
	 * Setter for Atom title field
	 *   
	 * @param title		Feed title to set
	 */
	public void setTitle(String title) {
		if (feed != null)
			feed.setTitle(new Text(MediaType.TEXT_PLAIN, title));
	}

	/**
	 * Getter for Atom title field
	 *   
	 * @return		Feed title
	 */
	public String getTitle() {
		if (feed != null)
			return feed.getTitle().getContent();
		else {
			Element titleEl = (Element) doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "title").item(0);
			return titleEl.getTextContent();
		}
	}

	/**
	 * Adds info about one of the feed authors
	 * @param 	name	Author name
	 * @param	email	Author email
	 * @param	uri		Author URI
	 */
	public void addAuthor(String name, String email, String uri) {
		if (feed != null) {
			Person p = new Person();
			if (name != null && name.length() > 0)
				p.setName(name);
			if (email != null && email.length() > 0)
				p.setEmail(email);
			if (uri != null && uri.length() > 0)
				p.setUri(new Reference(uri));
			feed.getAuthors().add(p);
		}
	}
	
	/**
	 * Getter for list of authors
	 * 
	 * @return		List of authors
	 */
	public List<Person> getAuthors() {
		if (feed != null) {
			return feed.getAuthors();

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
	 * Adds a link with attribute rel="self"
	 * 
	 * @param title		Link title
	 * @param uri		URI referenced by the link
	 */
	public void setSelfLink(String title, String uri) {
		if (feed != null) {
			Link altLink = new Link();
			altLink.setHref(new Reference(uri));
			altLink.setRel(Relation.SELF);
			if (title != null && title.length() > 0)
				altLink.setTitle(title);
			feed.getLinks().add(altLink);
		}
	}

	
	/**
	 * Adds a link with attribute rel="alternate"
	 * 
	 * @param title		Link title
	 * @param uri		URI referenced by the link
	 */
	public void setAlternateLink(String title, String uri) {
		if (feed != null) {
			Link altLink = new Link();
			altLink.setHref(new Reference(uri));
			altLink.setRel(Relation.ALTERNATE);
			if (title != null && title.length() > 0)
				altLink.setTitle(title);
			feed.getLinks().add(altLink);
		}
	}
	
	
	/**
	 * Adds a link with attribute rel="related"
	 * 
	 * @param title		Link title
	 * @param uri		URI referenced by the link
	 */
	public void setRelatedLink(String title, String uri) {
		if (feed != null) {
			Link altLink = new Link();
			altLink.setHref(new Reference(uri));
			altLink.setRel(Relation.RELATED);
			if (title != null && title.length() > 0)
				altLink.setTitle(title);
			feed.getLinks().add(altLink);
		}
	}
	
	
	/**
	 * Adds a new simple element as a child of <feed>, with the text content specified in 'textContent'
	 * 
	 * @param tag			Tag name 
	 * @param textContent	Text content for the new element.
	 */
	public void addExtendedTextChild(String tag, String textContent) {
		if (feed != null) {
			if (textChildren == null)
				textChildren = new HashMap<String, String>();
			textChildren.put(tag, textContent);
		}
	}
	
	
	/**
	 * Returns the text content of a simple extension element
	 * 
	 * @param tag		Tag name 
	 * @return			Text content for the new element.
	 */
	public String getExtendedTextChild(String tag) {
		if (feed != null)
			if (textChildren != null)
				return textChildren.get(tag);
			else
				return null;
		else {
			Element extendedChildEl = (Element) doc.getElementsByTagNameNS(FormatStatic.GLUE_NAMESPACE, tag).item(0);
			return extendedChildEl.getTextContent();
		}
	}
	
	
	/**
	 * Returns a list with the FormattedEntry objects contained in the feed.

	 * @return	List of FormattedEntry objects contained in the feed.
	 */
	public List<FormattedEntry> getEntries() {
		if (entries == null && doc != null) {
			entries = new ArrayList<FormattedEntry>();
			NodeList entryNodes = doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "entry");
			int len = entryNodes.getLength();
			Node entry = null;
			FormattedEntry formattedEntry = null;
			for (int i=0; i<len; i++) {
				entry = entryNodes.item(i);
				formattedEntry = new FormattedEntry(entry);
				entries.add(formattedEntry);
			}
		} 
		return entries;
	}
	

	/**
	 * Builds and returns a RESTlet DOM representation with the current data in the feed.
	 * 
	 * @return	Representation		RESTlet DOM representation containing the Atom feed
	 */
	public Representation getRepresentation() {
		if (doc == null) {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			try {
				feed.write(buffer);
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
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while creating an empty DOM document for parsing configuration form ", e);
			
			} catch (IOException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "File access error while parsing configuration form ", e);
			
			} catch (SAXException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while parsing configuration form ", e);
		
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown error ",e);
			}
			
			Element feedEl = (Element) doc.getElementsByTagNameNS(FormatStatic.ATOM_NAMESPACE, "feed").item(0);
			feedEl.setAttribute("xmlns:" + FormatStatic.GLUE_PREFIX, FormatStatic.GLUE_NAMESPACE);
			if (textChildren != null) {
				Iterator<String> tagIt = textChildren.keySet().iterator();
				String tag; Element child;
				while (tagIt.hasNext()) {
					tag = tagIt.next();
					child = doc.createElementNS(FormatStatic.GLUE_NAMESPACE, FormatStatic.GLUE_PREFIX + ":" + tag);
					child.setTextContent(textChildren.get(tag));
					feedEl.appendChild(child);
				}
			}
			feed = null;
			textChildren = null;
			
			/// entries 
			Iterator<FormattedEntry> it = entries.iterator();
			FormattedEntry entry;
			Document domEntry = null;
			while (it.hasNext()) {
				entry = it.next();
				try {
					domEntry = ((DomRepresentation) entry.getRepresentation()).getDocument();
					doc.getDocumentElement().appendChild(doc.importNode(domEntry.getDocumentElement(), true));
				} catch (IOException e) {
					//System.out.println("DOM Document couldn't be obtained from DomRepresentation :(  : " + e.getMessage());
					throw new RuntimeException("DOM Document couldn't be obtained from DomRepresentation :(  : " + e.getMessage(), e);
				}
			}
			entries = null;
		}
	
		DomRepresentation rep = null;
		try {
			rep = new DomRepresentation();
			rep.setMediaType(MediaType.APPLICATION_ATOM);
			rep.setCharacterSet(CharacterSet.UTF_8);
			rep.setDocument(doc);
		
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while creating representation  ", e);
		}
		
		return rep;
	}
	

	/**
	 * RESTlet Atom feed wrapped by FormattedFeed
	 */
	protected Feed feed;
	
	/**
	 * Text elements added for specific GLUE formatting
	 */
	protected HashMap<String, String> textChildren = null;
	
	/**
	 * Wrapped DOM document
	 */
	protected Document doc;

	/**
	 * Contained entries
	 */
	 protected ArrayList<FormattedEntry> entries;
}
