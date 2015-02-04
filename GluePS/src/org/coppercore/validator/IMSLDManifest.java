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

package org.coppercore.validator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.SAXParser;

import org.coppercore.common.MessageList;
import org.coppercore.exceptions.IMSObjectClassNotFoundException;
import org.coppercore.exceptions.SemanticException;
import org.coppercore.exceptions.SyntaxException;
import org.coppercore.exceptions.TechnicalException;
import org.coppercore.exceptions.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Represents the IMS Learning Design manifest.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.16 $, $Date: 2009/01/21 07:53:36 $
 */
public class IMSLDManifest {

	private Document document = null; // dom document representing the
	// manifest
	
	// Access modifications by LP
	public Document getDocument() {
		return document;
	}

	public byte[] getManifestXML() {
		return manifestXML;
	}

	
	
	private IMSPackage pack; // package containing the manifest and the
	// resources
	private LinkedHashMap imsObject = new LinkedHashMap(); // indexed list of
	// all objects to be
	// validated

	/*
	 * actual XML manifest. We preserve the byte array because this allows the
	 * parser to solve any encoding issues.
	 */
	private byte[] manifestXML;
	private HashMap uriIdMapping = new HashMap();

	private static final String IMSCP = "imscp_v1p1.xsd"; // schema for IMS
	// content packaging
	private static final String IMSMD = "imsmd_v1p2p2.xsd"; // schema form IMS
	// metadata
	private static final String IMSLDA = "imsld_level_a.xsd"; // schema for
	// IMS learning
	// design level
	// A
	private static final String IMSLDB = "imsld_level_b.xsd"; // schema for
	// IMS learning
	// design level
	// B
	private static final String IMSLDC = "imsld_level_c.xsd"; // schema for
	// IMS learning
	// design level
	// C
	private static final String XML = "xml.xsd"; // schema for XML extensions

	// container for all schemas that are pre-parsed for LD (A, B and C)
	private String schemas[][] = { { IMSCP, IMSMD, IMSLDA, XML },
			{ IMSCP, IMSMD, IMSLDB, XML }, { IMSCP, IMSMD, IMSLDC, XML } };

	/**
	 * Constructs a IMSLDManifest representing the IMS Learning Design object.
	 * 
	 * @param aManifest
	 *            byte[] byte array containing the IMS-LD XML representation
	 * @param aPackage
	 *            IMSPackage representing the original package (zip file)
	 * @param schemaOffset
	 *            String offset to the location of the schemas
	 */
	public IMSLDManifest(byte[] aManifest, IMSPackage aPackage,
			String schemaOffset) {
		pack = aPackage;

		manifestXML = aManifest;

		// add fileLocation to schemas
		for (int i = 0; i < schemas.length; i++) {
			for (int j = 0; j < schemas[i].length; j++) {
				String path = schemas[i][j];

				// only change path to schemas if additional file location is
				// set
				if (schemaOffset != null) {
					// add schema Location to schema files
					path = schemaOffset
							+ (schemaOffset.endsWith(File.separator) ? ""
									: File.separator) + path;
				}
				schemas[i][j] = path;
			}
		}
	}

	/**
	 * Builds all the IMSObjects that need additional validation. Throws a
	 * IMSObjectClassNotFoundException if problems occur during this step.
	 * 
	 * @throws ValidationException
	 */
	private void buildList() throws ValidationException {

		String nodeTable[][] = IMSObjectFactory.NODETABLE;
		int maxNodeTable = nodeTable.length;

		try {
			for (int i = 0; i < maxNodeTable; i++) {
				NodeList nodesToValidate = document.getElementsByTagNameNS(
						nodeTable[i][0], nodeTable[i][1]);
				int maxNode = nodesToValidate.getLength();

				// Create an IMS item for each item encountered
				for (int j = 0; j < maxNode; j++) {
					IMSObject myObject = null;
					myObject = IMSObjectFactory.getIMSElementFactory()
							.createObject(nodesToValidate.item(j), this);
					imsObject.put(myObject.key, myObject);
				}
			}
		} catch (IMSObjectClassNotFoundException ex) {
			throw new TechnicalException(ex);
		}
	}

	private void resolveReferences() throws ValidationException {
		Iterator myIterator = imsObject.values().iterator();

		while (myIterator.hasNext()) {
			IMSObject myObject = (IMSObject) myIterator.next();
			myObject.resolveReferences();
		}
	}

	private String determineLevel(byte[] manifest) throws ValidationException {
		LevelHandler lh = null;
		try {
			// create a non-validating, name space aware sax parser
			SAXParser saxParser = org.coppercore.common.Parser.getParser(true);

			lh = new LevelHandler();
			saxParser.parse(new ByteArrayInputStream(manifest), lh);

			return lh.getLevel();
		} catch (SAXParseException ex) {
			throw new SyntaxException(ex);
		} catch (SAXException ex) {
			throw new SyntaxException(ex);
		} catch (Exception ex) {
			throw new TechnicalException(ex);
		}
	}

	/**
	 * Validates the Manifest. Throws a ValidationException if errors occurred
	 * during validating.
	 * 
	 * @param baseURI
	 *            the base uri when searching for additional schema resources
	 * 
	 * @throws ValidationException
	 *             when an error occurred during validation
	 * @return boolean true if the IMS LD package is valid, else false
	 */
	public boolean validate(File baseURI) throws ValidationException {
		String level = determineLevel(getManifest());

		if ("A".equalsIgnoreCase(level)) {
			return validate(schemas[0], baseURI);
		} else if ("B".equalsIgnoreCase(level)) {
			return validate(schemas[1], baseURI);
		} else if ("C".equalsIgnoreCase(level)) {
			return validate(schemas[2], baseURI);
		} else {
			throw new SemanticException(
					"Could not determine the IMS Learning Design level");
		}
	}

	/**
	 * Validates a manifest XML file against a number of schemas passed as
	 * parameter. Validates the Manifest. Throws ValidationExceptions if errors
	 * occurred during parsing.
	 * 
	 * @param schema
	 *            String[] array of schema files used for validation
	 * @param baseURI
	 *            the base uri when searching for additional schema resources
	 * 
	 * @return boolean true if the manifest is valid, otherwise return false
	 * 
	 * @throws ValidationException
	 *             whenever an unrecoverable validation error has occurred
	 */
	private boolean validate(String[] schema, File baseURI)
			throws ValidationException {
		boolean isValid = true;

		try {
			DocumentBuilder builder = org.coppercore.common.Parser
					.getDocumentBuilder(true, true, schema,
							new ParserErrorHandler(getMessageList()));

			// make sure our path ends with a file separator sign.
			String base = baseURI.getAbsolutePath()
					+ (baseURI.getAbsolutePath().endsWith(File.separator) ? ""
							: File.separator);

			// create the DOM instance for the manifest
			document = builder.parse(new ByteArrayInputStream(getManifest()),
					base);

			// construct all IMSObjects needed for validation
			buildList();

			try {
				// resolve all relevant references between IMSObjects
				resolveReferences();
			} catch (ValidationException ex1) {
				// don't log the error here, because this is already done
				// because the
				// validation should continue although errors are encountered.
				// This will
				// provide the user with the most feedback possible.
				isValid = false;
			}

			Iterator myIterator = imsObject.values().iterator();
			while (myIterator.hasNext()) {
				IMSObject myObject = (IMSObject) myIterator.next();
				try {
					myObject.isValid();
				} catch (ValidationException ex) {
					isValid = false;
					pack.getMessageList().logError(ex.getMessage());
				}
			}
			return isValid;
		} catch (SAXParseException ex) {
			throw new SyntaxException(ex);
		} catch (Exception ex) {
			throw new TechnicalException(ex);
		}
	}

	/**
	 * Finds the IMS object with the given key.
	 * 
	 * @param aKey
	 *            String the key to search for
	 * @return IMSObject the found IMS object or null if no IMS object is found
	 *         with the specified key
	 */
	protected IMSObject findIMSObject(String aKey) {
		IMSObject result = null;

		if (imsObject.containsKey(aKey)) {
			result = (IMSObject) imsObject.get(aKey);
		}
		return result;
	}

	/**
	 * Returns the manifest.
	 * 
	 * @return IMSPackage the manifest itself
	 */
	protected IMSPackage getPackage() {
		return pack;
	}

	/**
	 * Returns the MessageList which keeps all logging information collected
	 * during validation.
	 * 
	 * @return MessageList the list of all logged messages
	 */
	protected MessageList getMessageList() {
		return pack.getMessageList();
	}

	/**
	 * Returns the manifest as an byte array.
	 * 
	 * @return IMSPackage the manifest as an byte array
	 */
	protected byte[] getManifest() {
		return manifestXML;
	}

	/**
	 * Adds a uri id combination to the manifest.
	 * 
	 * @param uri
	 *            String the uri of the uri id mapping
	 * @param id
	 *            String the id of the uri id mapping
	 * @see #findUriMapping
	 */
	protected void addUriIdMapping(String uri, String id) {
		uriIdMapping.put(uri, id);
	}

	/**
	 * Resolves the id for the given uri.
	 * <p>
	 * If the uri cannot be found, the method returns null.
	 * 
	 * @param uri
	 *            String the uri to look up
	 * @return String the id of the uri, or null if the uri could not be found
	 * @see #addUriIdMapping
	 */
	protected String findUriMapping(String uri) {
		return (String) uriIdMapping.get(uri);
	}
}
