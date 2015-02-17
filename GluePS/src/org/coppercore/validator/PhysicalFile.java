/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/*
 * CopperCore, an IMS-LD level C engine
 * Copyright (C) 2003,2005 Harrie Martens and Hubert Vogten
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
package org.coppercore.validator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.coppercore.common.Configuration;
import org.coppercore.common.MessageList;
import org.coppercore.common.Parser;
import org.coppercore.exceptions.TechnicalException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class represents a physical file in the IMS package.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.25 $, $Date: 2009/07/02 09:11:30 $
 */
public class PhysicalFile {
	static final int BUFFER = 2048;

	private File file;

	private int referenceCount = 0;

	private boolean hasGlobalContent = false;

	/** Specifies whether this file actually contains global elements. */
	protected boolean containsGlobalElements = false;

	private static DocumentBuilder builder = null;

	/**
	 * Creates a new PhysicalFile instance for the specified file.
	 * 
	 * @param aFilePath
	 *            String the filename of this physical file
	 */
	public PhysicalFile(String aFilePath) {
		file = new File(aFilePath);
	}

	/**
	 * Creates a new PhysicalFile instance for the specified file.
	 * 
	 * @param file
	 *            a File containing the filename of this file
	 */
	public PhysicalFile(File file) {
		this.file = file;
	}

	/**
	 * Returns the file location of this resource.
	 * 
	 * @return String the file location of this resource
	 */
	public String getFilePath() {
		return file.getPath();
	}

	/**
	 * Returns true if this file is being referenced in the manifest.
	 * 
	 * @return boolean true if this file is being referenced in the manifest
	 */
	public boolean isReferenced() {
		return (referenceCount != 0);
	}

	/**
	 * Increments the reference count for this file resource.
	 */
	protected void incReferenceCount() {
		referenceCount++;
	}

	/**
	 * Marks this file resource as having global content.
	 * <p>
	 * Files containing global content need special treatment during
	 * publicattion and need to be personalized during runtime delivery.
	 */
	protected void setHasGlobalContent() {
		hasGlobalContent = true;
	}

	/**
	 * Returns true if this file has global content elements.
	 * <p>
	 * Files containing global content need special treatment during
	 * publicattion and need to be personalized during runtime delivery.
	 * 
	 * @return boolean true if this file has global content elements
	 */
	protected boolean hasGlobalContent() {
		return hasGlobalContent;
	}

	/**
	 * Saves the input stream to the specified file.
	 * 
	 * @param filename
	 *            String the name of the file to save to
	 * @param input
	 *            BufferedInputStream the data to save to the file
	 * @throws IOException
	 *             if there is an error reading from the stream of writing to
	 *             the file.
	 */
	protected void saveToFile(String filename, BufferedInputStream input) throws IOException {
		File newFile = new File(filename);
		BufferedOutputStream dest = null;

		// Create the path where we want to store the file
		File parent = newFile.getParentFile();
		if (parent != null) {
			parent.mkdirs();
		}
		int count;
		byte data[] = new byte[BUFFER];
		FileOutputStream fos = new FileOutputStream(newFile);
		dest = new BufferedOutputStream(fos, BUFFER);
		while ((count = input.read(data, 0, BUFFER)) != -1) {
			dest.write(data, 0, count);
		}
		dest.flush();
		dest.close();
	}

	/**
	 * Validates the file resource.
	 * 
	 * @param globalContent
	 *            byte[] the data of this file
	 * @param messageList
	 *            MessageList the list to store all validation logging messages
	 *            to
	 * @param manifest
	 *            IMSLDManifest the manifest this file is part of
	 * @throws TechnicalException
	 *             when there is an error validating the file
	 * @return boolean true if the file resource is valid, else false
	 */
	protected boolean isValid(byte[] globalContent, MessageList messageList, IMSLDManifest manifest)
			throws TechnicalException {
		boolean result = true;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(globalContent);
			Document document = getDocumentBuilder().parse(bis);
			result &= validateProperties(document, "view-property", manifest, messageList);
			result &= validateProperties(document, "set-property", manifest, messageList);
			result &= validateProperties(document, "view-property-group", manifest, messageList);
			result &= validateProperties(document, "set-property-group", manifest, messageList);
			result &= validatePropertyGroups(document, "view-property-group", manifest, messageList);
			result &= validatePropertyGroups(document, "set-property-group", manifest, messageList);
			result &= patchNotifications(document, messageList, true);
		} catch (Exception ex) {
			throw new TechnicalException(ex);
		}

		return result;
	}

	private boolean validateProperties(Document document, String localTagName, IMSLDManifest manifest,
			MessageList messageList) {
		boolean result = true;
		NodeList properties = document.getElementsByTagNameNS(Parser.IMSLDNS, localTagName);
		int count = properties.getLength();

		if (count > 0) {
			containsGlobalElements = true;
		}
		for (int i = 0; i < count; i++) {
			Element property = (Element) properties.item(i);
			Attr attr = property.getAttributeNode("href");
			String href = (attr == null ? null : attr.getNodeValue());
			attr = property.getAttributeNode("ref");
			String ref = (attr == null ? null : attr.getNodeValue());
			String id = null;

			if (href != null) {
				id = manifest.findUriMapping(href);
				if (id == null) {
					messageList.logError("(view)property with href [" + href + "] in globalcontent [" + file
							+ "] not declared in imsmanifest.xml");
					result = false;
				} else if (id.equals(ref)) {
					messageList.logWarning("(view)property with href [" + href + "] in globalcontent [" + file
							+ "] contains both a ref and a href attribute pointing to the same property");

				} else if (ref != null) {
					messageList.logError("(view)property with href [" + href + "] in globalcontent [" + file
							+ "] contains both a ref and a href attribute pointing to different properties");
					result = false;
				}
			} else if (ref == null) {
				messageList
						.logError("(view)property in globalcontent [" + file + "] contains no ref or href attribute");
				result = false;
			} else {
				// only ref filled in, check the ref
				/** @todo validate ref from global content */
			}
		}
		return result;
	}

	/**
	 * patches and saves the file resource to the specified location.
	 * 
	 * @param globalContent
	 *            byte[] the data of this file
	 * @param manifest
	 *            IMSLDManifest the manifest this file is a part of
	 * @param destinationPath
	 *            String the location where to save the patched file
	 * @throws TechnicalException
	 *             when there is an error patching and saving the file
	 */
	protected void patch(byte[] globalContent, IMSLDManifest manifest, String destinationPath)
			throws TechnicalException {
		try {
			Document document = getDocumentBuilder().parse(new ByteArrayInputStream(globalContent));
			
			// Patch all global elements in the global content
			patchProperties(document, "view-property", manifest);
			patchProperties(document, "set-property", manifest);
			patchProperties(document, "view-property-group", manifest);
			patchProperties(document, "set-property-group", manifest);
			expandPropertyGroup(document, "view-property-group", manifest, "view");
			expandPropertyGroup(document, "set-property-group", manifest, "set");

			/**
			 * @todo check how we can split validation and patching in an
			 *       effective way
			 */
			patchNotifications(document, null, false);

			if (containsGlobalElements) {
				// document contains global elements so add our namespace to it
				document.getDocumentElement().setAttribute("xmlns:cc", Parser.CCNS);
				// Prepare the DOM document for writing

				Source source = new DOMSource(document);

				// Prepare the output file
				File newFile = new File(destinationPath + file);
				boolean success = newFile.getParentFile().mkdirs();
				Result result = new StreamResult(new FileOutputStream(newFile));

				// Write the DOM document to the file
				Transformer xformer = TransformerFactory.newInstance().newTransformer();
				xformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				xformer.setOutputProperty(OutputKeys.METHOD, "xml");

				DocumentType doctype = document.getDoctype();
				if (doctype != null && !Configuration.removeDoctype()) {
					
					String id = doctype.getPublicId();
					if (id != null) {
						xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, id);
					}
					id = doctype.getSystemId();
					if (id != null) {
						xformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, id);
					}
				}
				xformer.transform(source, result);
			}
		} catch (Exception ex) {
			throw new TechnicalException(ex);
		}
	}

	private void patchProperties(Document document, String localTagName, IMSLDManifest manifest) {
		// Collect all specified elements from the document using the ims ld
		// namespace
		NodeList properties = document.getElementsByTagNameNS(Parser.IMSLDNS, localTagName);
		int count = properties.getLength();
		for (int i = 0; i < count; i++) {
			Element property = (Element) properties.item(i);
			property.setPrefix("cc");
			property.setAttribute("xmlns:cc", Parser.CCNS);
			Attr attr = property.getAttributeNode("href");
			String href = (attr == null ? null : attr.getNodeValue());
			attr = property.getAttributeNode("ref");
			if (href != null) {
				// lookup the ref id for this href, it will be found because
				// validation
				// took place before
				String id = manifest.findUriMapping(href);
				if (attr != null) {
					// ref attribute already exists, change it
					attr.setNodeValue(id);
				} else {
					// no ref attribute found, create a new one
					property.setAttribute("ref", id);
				}
			}
			// patch the notification
			patchChildren(property);
		}
	}

	private void patchChildren(Element node) {
		Node child = node.getFirstChild();

		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				child.setPrefix("cc");
				patchChildren((Element) child);
			}

			child = child.getNextSibling();
		}
	}

	private boolean validatePropertyGroups(Document document, String localTagName, IMSLDManifest manifest,
			MessageList messageList) {
		boolean result = true;
		NodeList pgs = document.getElementsByTagNameNS(Parser.IMSLDNS, localTagName);
		int count = pgs.getLength();

		if (count > 0) {
			containsGlobalElements = true;
		}
		for (int i = 0; i < count; i++) {
			Element pg = (Element) pgs.item(i);

			String ref = pg.getAttribute("ref");
			// fixed 2005-09-28 retrieve value for view using a default value if
			// attribute was omitted
			String view = Parser.getImpliedNamedAttribute(pg, "view", "value");

			if ("value".equals(view) || "title-value".equals(view)) {

				IMSObject pgObject = manifest.findIMSObject(ref);

				if (pgObject == null) {
					messageList.logError(localTagName + " with ref \"" + ref
							+ "\" was not defined in the learning design");
					result = false;
				}
			} else {
				messageList.logError(localTagName + " has an invalid value \"" + view + "\" for the view attribute");
				result = false;
			}
		}
		return result;
	}

	private void expandPropertyGroup(Document document, String localTagName, IMSLDManifest manifest, String prefix) {
		// Collect all specified elements from the document using the ims ld
		// namespace
		NodeList pgs = document.getElementsByTagNameNS(Parser.IMSLDNS, localTagName);

		// nodes from this nodeslist are replaced while expanding this property
		// group, so
		// the number of nodes will decrease.
		for (int i = 0; i < pgs.getLength(); i++) {
			Element pg = (Element) pgs.item(i);

			String ref = pg.getAttribute("ref");

			IMSObject pgObject = manifest.findIMSObject(ref);
			Element pgNode = (Element) pgObject.node;

			expandPropertyGroup(document, pg, pgNode, true, manifest, prefix, pg.getAttribute("property-of"), pg
					.getAttribute("view"));
		}

	}

	/**
	 * This method will replace the reference a property-group in IMSLD global
	 * content with the actual members of that property group. This method is
	 * called recursively to resolve all new property-groups references that may
	 * have been introduced by this process. It assumes there are no circular
	 * references present. These are checked during validation. <br>
	 * changed 16-01-2005: is now namespace aware
	 * 
	 * @param document
	 *            the dom document containing the property-groups
	 * @param root
	 *            the root element of the document
	 * @param pgNode
	 *            the property group node replacing the reference
	 * @param isFirst
	 *            boolean if this is the starting point of the
	 *            property-group-ref
	 * @param manifest
	 *            IMSLDManifest the manifest containing the LD
	 * @param prefix
	 *            String the prefix being either set or get
	 * @param propertyOf
	 *            String the owner of the property either self or
	 *            supported-person
	 * @param view
	 *            String the type of view either being title-value or value
	 */
	private void expandPropertyGroup(final Document document, final Element root, final Element pgNode,
			boolean isFirst, final IMSLDManifest manifest, final String prefix, final String propertyOf,
			final String view) {
		Element newRoot = root;

		// changed 16-01-2005: is now namespace aware
		if ("property-group".equals(pgNode.getLocalName()) && Parser.IMSLDNS.equals(pgNode.getNamespaceURI())) {
			// check if a new level should start here
			if (isFirst) {
				// we will have to patch the place holder
				newRoot.setPrefix("cc");
				newRoot.setAttribute("xmlns:cc", Parser.CCNS);
				patchChildren(newRoot); // patch the optional notification
			} else {
				// there was no place holder
				newRoot = document.createElement("cc:" + prefix + "-property-group");
				newRoot.setAttribute("view", view);
				root.appendChild(newRoot);
			}
		}

		// loop of the children an process them
		Node child = pgNode.getFirstChild();

		while (child != null) {
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				if ("title".equals(child.getLocalName()) && Parser.IMSLDNS.equals(child.getNamespaceURI())) {
					// append the title/metadata to the new root for reference;
					// changed
					Element newNode = document.createElement("cc:title");
					NodeList children = child.getChildNodes();
					newRoot.appendChild(newNode);

					// now add the content to the new child node
					for (int j = 0; j < children.getLength(); j++) {
						Node newChild = document.importNode(children.item(j), true);
						newNode.appendChild(newChild);
					}
				} else if ("property-ref".equals(child.getLocalName())
						&& Parser.IMSLDNS.equals(child.getNamespaceURI())) {
					// add a view or set property to the current root
					Element newNode = document.createElement("cc:" + prefix + "-property");
					newNode.setAttribute("ref", ((Element) child).getAttribute("ref"));
					newNode.setAttribute("property-of", propertyOf);
					newNode.setAttribute("view", view);
					newRoot.appendChild(newNode);
				} else if ("property-group-ref".equals(child.getLocalName())
						&& Parser.IMSLDNS.equals(child.getNamespaceURI())) {
					// we encountered a new property group reference, so we must
					// call
					// this method recursively
					String ref = ((Element) child).getAttribute("ref");
					IMSObject pgObject = manifest.findIMSObject(ref);
					Element newPgNode = (Element) pgObject.node;
					expandPropertyGroup(document, newRoot, newPgNode, false, manifest, prefix, propertyOf, view);
				}
			}
			child = child.getNextSibling();
		}
	}

	private boolean patchNotifications(Document document, MessageList messageList, boolean validate) {
		boolean result = true;
		NodeList notifications = document.getElementsByTagNameNS(Parser.IMSLDNS, "notification");

		for (int i = 0; i < notifications.getLength(); i++) {
			Element notification = (Element) notifications.item(i);

			NodeList emailData = notification.getElementsByTagNameNS(Parser.IMSLDNS, "email-data");
			int count = emailData.getLength();
			if (count > 0) {
				for (int j = 0; j < count; j++) {
					Element data = (Element) emailData.item(j);

					NodeList roleRefs = data.getElementsByTagNameNS(Parser.IMSLDNS, "role-ref");

					if (roleRefs.getLength() == 1) {
						Element roleRef = (Element) roleRefs.item(0);
						if (roleRef.hasAttribute("ref")) {
							// move reference to email data
							if (!validate) {
								data.setAttribute("role-ref", roleRef.getAttribute("ref"));

								// now remove the role-ref element
								data.removeChild(roleRef);
							}
						} else {
							if (validate) {
								messageList.logError("missing ref attribute for role-ref element");
							}
							result = false;
							break;
						}
					} else {
						if (validate) {
							messageList.logError("missing role-ref element for email-data");
						}
						result = false;
						break;
					}
				}

				// check if everything was ok until so far
				if (result) {
					NodeList learningActivity = notification.getElementsByTagNameNS(Parser.IMSLDNS,
							"learning-activity-ref");
					NodeList supportActivity = notification.getElementsByTagNameNS(Parser.IMSLDNS,
							"support-activity-ref");

					// count all the found nodes
					count = learningActivity.getLength() + supportActivity.getLength();

					if (count <= 1) {
						Element activity = null;
						if (learningActivity.getLength() == 1) {
							activity = (Element) learningActivity.item(0);
						} else if (supportActivity.getLength() == 1) {
							activity = (Element) supportActivity.item(0);
						}

						if (activity != null) {
							if (activity.hasAttribute("ref")) {
								if (!validate) {
									String ref = activity.getAttribute("ref");
									notification.setAttribute("ref", ref);
									notification.removeChild(activity);
								}
							} else {
								if (validate) {
									messageList.logError("Missing href attribute for " + activity.getLocalName());
								}
								result = false;
								break;
							}
						}

						// are we still ok?
						if (result) {
							NodeList subjects = notification.getElementsByTagNameNS(Parser.IMSLDNS, "subject");

							if (subjects.getLength() > 1) {
								if (validate) {
									messageList.logError("Only one subject allowed in a notification");
								}
								result = false;
								break;
							}
						}
					} else {
						if (validate) {
							messageList.logError("Only one learning-activity-ref or support-activity-ref allowed");
						}
					}
				}
			} else {
				if (validate) {
					messageList.logError("notification requires at least one email-data element");
				}
				result = false;
				break;
			}

		}
		return result;
	}

	/**
	 * Checks if this instance equals the passed object.
	 * <p>
	 * The two objects are considered equal when they both are of the same type
	 * and the both point to the same file.
	 * 
	 * @param obj
	 *            the object to compare this instance to
	 * @return true if the passed object equals this instance
	 * 
	 */
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PhysicalFile)) {
			return false;
		}
		PhysicalFile that = (PhysicalFile) obj;
		return (that.file.equals(this.file));
	}

	/**
	 * Singleton to create and re-use only one documentbuilder.
	 * 
	 * @throws ParserConfigurationException
	 *             if the requested document builder cold not be created
	 * @return DocumentBuilder
	 */
	private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		if (builder == null) {
			// Create a namespace aware documentbuilder
			builder = Parser.getDocumentBuilder(false, true, null, null);
		}
		return builder;
	}

}
