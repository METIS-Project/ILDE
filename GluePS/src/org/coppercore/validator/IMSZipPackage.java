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

/**
 * @todo validate class tag to contain at least one class name (implied attribute, should be required)
 *
 */
package org.coppercore.validator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;

import org.apache.log4j.Logger;
import org.coppercore.common.MessageList;
import org.coppercore.common.Util;
import org.coppercore.exceptions.SemanticException;
import org.coppercore.exceptions.TechnicalException;
import org.coppercore.exceptions.ValidationException;
import org.coppercore.resources.MessageKeys;
import org.coppercore.resources.MessageUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * IMSZipPackage represents an packaged IMS Learning Design Content Package
 * package.
 * 
 * <p>
 * The Content Package is stored as a zip file using the pkzip 2.04g zip format.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.12 $, $Date: 2009/07/02 09:11:29 $
 */
public class IMSZipPackage extends IMSPackage implements MessageKeys {
	static final int BUFFER = 2048;

	private static final String XSI = "http://www.w3.org/2001/XMLSchema-instance"; // schema
	// language

	/** this directory is used to store any additional schemas * */
	protected String additionalSchemaDir = "additional";

	/** list of additional schemas as declared in the manifest * */
	protected String[] additionalSchemas;

	/**
	 * Constructs a new IMSPackage object with the given parameters.
	 * 
	 * @param aFileName
	 *            is the name of the physical IMS Learning Design package file
	 * @param aSchemaOffset
	 *            is the path where the ims-ld schemas can be found
	 * @param messageList
	 *            is the CCLogger object where all messages generated during the
	 *            validation are stored
	 */
	public IMSZipPackage(String aFileName, String aSchemaOffset, MessageList messageList) {
		super(aFileName, aSchemaOffset, messageList);
	}

	public boolean validate() {
		// TODO Auto-generated method stub
		return validate(new File(getSchemaLocation("")));
	}

	/**
	 * Returns a list of local references to schema's that are possibly included
	 * in the manifest.
	 * <p>
	 * 
	 * @return a list of local references to schema's that are possibly included
	 *         in the manifest.
	 */
	private String[] getSchemaReferences() {
		ArrayList result = new ArrayList();

		try {
			DocumentBuilder builder = org.coppercore.common.Parser.getDocumentBuilder(false, true, null, null);
			Document doc = builder.parse(new ByteArrayInputStream(getManifest()));
			Element root = doc.getDocumentElement();
			String schemaLocations = root.getAttributeNS(XSI, "schemaLocation");

			// split the schema location in name space and location tuples
			String[] values = schemaLocations.split(" ");
			for (int i = 1; i < values.length; i += 2) {
				URI uri = new URI(values[i]);
				if (!uri.isAbsolute())
					result.add(values[i]);
			}

		} catch (Exception e) {
			// we do nothing with exception, errors in the document structure
			// will be handled elsewhere
		}

		return (String[]) result.toArray(new String[0]);
	}

	private void extractAddtionalSchemas() {
		try {
			for (int i = 0; i < additionalSchemas.length; i++) {

				// ok, we have found the corresponding physical file, lets store
				// it
				saveSchemaToFile(additionalSchemas[i]);
			}
		} catch (Exception e) {
			// failures will be stumbled upon in later stages of validation
		}
	}

	protected void analysePackage() throws ValidationException {
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(contentPackage));
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry e;

			while ((e = zin.getNextEntry()) != null) {
				if (!e.isDirectory()) {
					if (e.getName().equalsIgnoreCase(IMSPackage.IMSMANIFEST_FILE)) {
						byte[] xmlBinding = unzip(zin);
						manifest = new IMSLDManifest(xmlBinding, this, schemaLocation); // create
					} else {
						files.put(getURIKey(e.getName()), new PhysicalFile(e.getName())); // create
					}
				}
			}
			zin.close();

			// get any additional schemas
			additionalSchemas = getSchemaReferences();

			// extract additional schemas from package
			extractAddtionalSchemas();

			// check if a manifest file was encountered in the package.
			if (manifest == null) {
				throw new SemanticException(MessageUtil.formatMessage(MSG_MANIFEST_NOT_FOUND, contentPackage));
			}
		} catch (Exception ex) {
			throw new TechnicalException(ex);
		}
	}

	private static byte[] unzip(InputStream zin) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[512];
		int len = 0;
		while ((len = zin.read(b)) != -1) {
			out.write(b, 0, len);
		}
		out.close();

		return out.toByteArray();
	}

	/**
	 * Extracts the ims LD package into the specified destination path.
	 * 
	 * <p>
	 * Files containing globalcontent are patched before they are stored.
	 * 
	 * @param destinationPath
	 *            String the location where the resources are stored
	 * @throws IOException
	 *             when there is an error reading or writing the resources
	 * @throws TechnicalException
	 *             when there is an error patchin resources containing global
	 *             content
	 */
	public void storeResources(String destinationPath) throws IOException, TechnicalException {
		File path = new File(destinationPath);
		path.mkdirs();
		InputStream in = new BufferedInputStream(new FileInputStream(contentPackage));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry entry;
		while ((entry = zin.getNextEntry()) != null) {
			// Skip directories
			if (!entry.isDirectory()) {
				// lookup the resource
				PhysicalFile file = (PhysicalFile) files.get(getURIKey(entry.getName()));
				if (file != null) {
					if (file.containsGlobalElements) {
						byte[] globalContent = unzip(zin);
						file.patch(globalContent, manifest, destinationPath);
					} else {
						BufferedInputStream is = new BufferedInputStream(zin, BUFFER);
						file.saveToFile(destinationPath + entry.getName(), is);
					}
				}
			}
		}
	}

	protected boolean validateGlobalContent() throws ValidationException {
		boolean result = true;
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(contentPackage));
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry entry;
			while ((entry = zin.getNextEntry()) != null) {
				if (!entry.isDirectory()) {
					PhysicalFile file = (PhysicalFile) files.get(getURIKey(entry.getName()));
					if ((file != null) && (file.hasGlobalContent())) {
						byte[] globalContent = unzip(zin);
						result &= file.isValid(globalContent, messageList, manifest);
					}
				}
			}
			return result;
		} catch (Exception ex) {
			throw new TechnicalException(ex);
		}
	}

	/**
	 * Delete all temporary additional schemas
	 */
	protected void cleanup() {
		super.cleanup();
		try {
			Util.deleteFolder(new File(getSchemaLocation("")));
		} catch (Exception e) {
			// failures will be stumbled upon in later stages of validation
		}

	}

	private void saveSchemaToFile(String fileName) throws FileNotFoundException, IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(contentPackage));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry entry;
		while ((entry = zin.getNextEntry()) != null) {
			if (!entry.isDirectory()) {
				if (fileName.equals(entry.getName())) {
					PhysicalFile file = (PhysicalFile) files.get(getURIKey(entry.getName()));

					// we also mark this file as being referenced
					if (file != null)
						file.incReferenceCount();
					BufferedInputStream is = new BufferedInputStream(zin, BUFFER);
					file.saveToFile(getSchemaLocation(fileName), is);
					return;
				}
			}
		}
		Logger log = Logger.getLogger(this.getClass());
		log.warn("Referenced local schema \'" + fileName + "\' not found in package");
	}

	private String getSchemaLocation(String fileName) {
		// TODO once we move over to Java 1.5+ we should replace hash by a guid
		return schemaLocation + File.separator + additionalSchemaDir + File.separator + Integer.toHexString(hashCode()) + File.separator + fileName;
	}

}
