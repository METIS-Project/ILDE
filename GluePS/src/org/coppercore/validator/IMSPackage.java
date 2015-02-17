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
 * CopperCore , an IMS-LD level C engine
 * Copyright (C) 2003, 2004 Harrie Martens and Hubert Vogten
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.coppercore.common.MessageList;
import org.coppercore.common.URL;
import org.coppercore.exceptions.TechnicalException;
import org.coppercore.exceptions.ValidationException;

/**
 * Abstract class representing an IMS Content Package.
 * 
 * @author Harrie Martens
 * @author Hubert Vogten
 * @version $Revision: 1.23 $, $Date: 2009/07/02 09:11:29 $
 */
abstract public class IMSPackage {

	/** The fixed name of the manifest file in a content package. */
	public static String IMSMANIFEST_FILE = "imsmanifest.xml";

	/** XML manifest incorporating the Learning Design. */
	protected IMSLDManifest manifest;

	/** Container for the physical resources. */
	protected HashMap files = new HashMap();

	/** The filename of the IMS Content Package file. */
	protected File contentPackage = null;

	/** The list of all logged messages. */
	protected MessageList messageList = null;

	/** The file location of the xml schemas. */
	protected String schemaLocation = null;
	

	/**
	 * Constructs a new IMSPackage object with the given parameters.
	 * 
	 * @param fileName
	 *            is the name of the physical IMS Learning Design package file
	 * @param schemaOffset
	 *            is the path where the ims-ld schemas can be found
	 * @param messageList
	 *            is the CCLogger object where all messages generated during the
	 *            validation are stored
	 */
	public IMSPackage(String fileName, String schemaOffset, MessageList messageList) {
		this.contentPackage = new File(fileName);
		this.schemaLocation = schemaOffset;
		this.messageList = messageList;
	}

	/**
	 * Abstract method for analyzing the content package.
	 * 
	 * <p>
	 * Locates and reads the manifest file and creates a list of all file
	 * resources available in the package.
	 * 
	 * @throws ValidationException
	 *             when there is an error analyzing the package
	 */
	abstract protected void analysePackage() throws ValidationException;

	/**
	 * Abstract method vor validating the global content files of the package.
	 * 
	 * @throws ValidationException
	 *             when there is a non recoverable error during validation
	 * @return boolean true if the global content files are valid
	 */
	abstract protected boolean validateGlobalContent() throws ValidationException;

	/**
	 * Extracts the ims LD package into the specified destination path.
	 * 
	 * <p>
	 * Files containing globalcontent are patched before they are stored.
	 * 
	 * @param destinationPath
	 *            String the location where the resource files are stored
	 * @throws IOException
	 *             when there is an error reading or writing the resource files
	 * @throws TechnicalException
	 *             when there is an error during the patching of the resources
	 *             containing global content
	 */
	abstract public void storeResources(String destinationPath) throws IOException, TechnicalException;

	/**
	 * Validates the given IMS Learning Design package.
	 * 
	 * <p>
	 * The package is not only validated versus the xml schemas but also against
	 * the ims-learning design semantics. The validation process tries to find
	 * as many errors and warnings as possible and stores all findings in the
	 * logger.
	 * 
	 * @return boolean true if the validation was successfully, otherwise the
	 *         method returns false
	 */
	public abstract boolean validate();

	/**
	 * Cleanup will will called immediately after the validation of the
	 * manifest. It provides a means for the implementing classes to cleanup
	 * after themselves.
	 */
	protected void cleanup() {
		// do nothing
	}

	/**
	 * Validates the given IMS Learning Design package.
	 * 
	 * <p>
	 * The package is not only validated versus the xml schemas but also against
	 * the ims-learning design semantics. The validation process tries to find
	 * as many errors and warnings as possible and stores all findings in the
	 * logger.
	 * 
	 * @param baseURI
	 *            the base uri used when additional schemas are referenced
	 * 
	 * @return boolean true if the validation was successfully, otherwise the
	 *         method returns false
	 */
	protected boolean validate(File baseURI) {
		boolean result = true;
		try {
			messageList.logInfo("Validation started.");

			// Step 1 unzip zipPackage
			messageList.logInfo("step 1 - Analysing package (" + contentPackage + ").");
			analysePackage();

			// Step 2 validating the XML manifest
			messageList.logInfo("step 2 - validating the manifest.");
			result &= manifest.validate(baseURI);
			
			// Step 2a cleanup after ourselves if needed
			cleanup();

			// Step 3 validate global content
			messageList.logInfo("step 3 - validating global content.");
			result &= validateGlobalContent();

			// Step 4 check a references to the resources
			messageList.logInfo("step 4 - checking if all files in package are referenced.");
			checkResources();
		} catch (Exception ex) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.error(ex);
			messageList.logException(ex);
			result = false;
		}
		if (result) {
			messageList.logInfo("Validation passed successfully.");
		} else {
			messageList.logInfo("Validation failed.");
		}
		return result;
	}

	/**
	 * Checks if for the specified resource an actual file resource is included
	 * in the content package.
	 * 
	 * @param aResource
	 *            String the resource to search the file for
	 * @return boolean true if a resource file is found, otherwise false
	 */
	protected boolean hasResource(URL resource) {
		// this resource represents a physical file so it can be converted to a
		// File
		PhysicalFile result = (PhysicalFile) files.get(getURIKey(resource));
		if (result != null) {
			result.incReferenceCount();
		}
		return (result != null);
	}

	/**
	 * Mark the resource file for having global content.
	 * 
	 * <p>
	 * Resources containing global content need special treatment when published
	 * or during runtime delivery.
	 * 
	 * @param resource
	 *            String the resource file that has global content
	 */
	protected void setFileAsGlobalContent(URL resource) {
		PhysicalFile result = (PhysicalFile) files.get(getURIKey(resource));
		if (result != null) {
			result.setHasGlobalContent();
		}
	}

	protected URI getURIKey(URL url) {
		return getURIKey(url.unescape());
	}
	
	protected URI getURIKey(String filename) {
		return new File(filename).toURI();
	}

	/**
	 * Returns the list containing all the logged messages.
	 * 
	 * @return MessageList the list containing all the logged messages
	 */
	public MessageList getMessageList() {
		return messageList;
	}

	/**
	 * Checks if all files contained in the content package are actually being
	 * referenced from the resource section of the manifest.
	 */
	protected void checkResources() {
		Iterator it = files.values().iterator();
		PhysicalFile file;
		while (it.hasNext()) {
			file = (PhysicalFile) it.next();
			if (!file.isReferenced()) {
				messageList.logWarning("File (" + file.getFilePath()
						+ ") is included in package, but is never referenced");
			}
		}
	}

	/**
	 * Returns the IMSManifest object contained in this package.
	 * 
	 * @return IMSLDManifest the IMSManifest object contained in this package
	 */
	public IMSLDManifest getIMSLDManifest() {
		return manifest;
	}

	/**
	 * Returns the manifest as a byte array, suitable to be picked up by a
	 * parser.
	 * 
	 * @return byte[] the manifest as a byte array, suitable to be picked up by
	 *         a parser
	 */
	public byte[] getManifest() {
		return manifest.getManifest();
	}

}
