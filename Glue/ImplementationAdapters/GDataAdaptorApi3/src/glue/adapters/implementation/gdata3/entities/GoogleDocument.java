/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GData Adapter.
 * 
 * GData Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GData Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.gdata3.entities;


import glue.common.entities.instance.InstanceEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequest.RequestType;
import com.google.gdata.client.docs.DocsService;
import com.google.gdata.data.Category;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ResourceNotFoundException;
import com.google.gdata.util.ServiceException;


/**
 * Entity representing a document to be created with one of the Google Docs tool.
 * 
 * Keeps an Atom entry ready to post to Google Documents List service.
 *
 * Used for blank Documents, Presentations and Spreadsheets; and for preexisting documents
 * 
 * @author  David A. Velasco
 * @contributor Javier Enrique Hoyos Torio
 * @version 2012092501
 * @package glue.adapters.implementation.gdata3.entities
 */
public class GoogleDocument implements InstanceEntity {

	/// class attributes ///
	/** Helper for date conversion from Google 'DateFormat' */
	protected static final SimpleDateFormat dateTimeFormat822 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	
	
	/// attributes ///
	
	/** Document title */
	protected String title = null;
	
	/** Google Document type */
	protected String docType = null;
	
	/** File to upload, in the file system */
	protected File file = null;
	
	/** MIME type */
	protected String mimeType = null;
	
	/** URL to the Google Doc resource (entry) */
	protected String entryURL = null;
	
	/** Browser-friendly URL to the Google Doc */
	protected String htmlURL = null;
	
	/** GData entry corresponding to the document */
	protected DocumentListEntry entry = null;
	
	/** Local identifier */
	protected int index = -1;
	
	/** Date of last document update, as returned by Google Docs List service, in RFC 822 format */ 
	protected Date updated;
	

	/// methods ///
	
	/** 
	 * Constructor for creation of an empty document.
	 * 
	 * @param 	title	String	Title of the document.
	 * @param	doctype	String	GData category string identifying the type of document.
	 */
	public GoogleDocument(String title, String docType) {
		this.title = title;
		this.docType = docType;
		this.mimeType = "";
	}
	
	
	/**
	 * Constructor for upload of an existing document as a new Google Doc.
	 * 
	 * @param 	title		String		Title of the document.
	 * @param 	file 		File 		Accesor to the existing document in the file system.
	 * @param	mimeType	String		MIME type of the document; if null, constructor tries to find out from 'file'.
	 */
	public GoogleDocument(String title, File file, String mimeType) {
		this.title = title;
		this.file = file;
		this.mimeType = mimeType;
		if (this.mimeType != null && 
			(this.mimeType.indexOf("/") <= 0 || this.mimeType.indexOf("/") >= this.mimeType.length())
			)
			this.mimeType = null;
		if (this.mimeType == null)		// TODO better; mimeType is comepletely useless
			this.mimeType = DocumentListEntry.MediaType.fromFileName(this.file.getName()).getMimeType(); 
		else {
			try {
				DocumentListEntry.MediaType.valueOf(this.mimeType);
			} catch (IllegalArgumentException i) {
				this.mimeType = DocumentListEntry.MediaType.fromFileName(this.file.getName()).getMimeType();
			}
		}
	}
	
	
	/**
	 * Constructor for an already existing Google Doc.
	 * 
	 * @param 	index		int		
	 * @param	title		String
	 * @param	updated		Date
	 */
	public GoogleDocument(int index, String title, Date updated) {
		this.index = index;
		this.title = title;
		this.updated = updated;
		this.mimeType = "";
	}
	
	
	/**
	 * Effective creation of word processor document at Google Docs service.
	 */
	//@Override
	public void create(String callerUser, Map<String, String> specificParams) throws ResourceException {
		// search parameters
		String feedURL 		= specificParams.get("feedURL");	// URL to the Google Service feed where POST message must be sent
		String gDocsUser 	= specificParams.get("user");		// Google docs username
		String gDocsPass 	= specificParams.get("pass");		// Google docs password
		
		// get access to Google Documents List service
		DocsService ds = GDataServiceManager.getDocsService(gDocsUser, gDocsPass);
		if (ds == null)
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Google Docs service could not be connected");
		
		DocumentListEntry newDocument;
		try {
			if (file == null) {
				// creation 
				newDocument = new DocumentListEntry();
				newDocument.getCategories().remove(DocumentListEntry.CATEGORY);
				newDocument.getCategories().add(new Category(	com.google.gdata.util.Namespaces.gKind, 
																DocumentListFeed.DOCUMENT_NAMESPACE + "#" + docType,	// the 'type' param sent from GLUEletManager 
																docType));
				newDocument.setTitle(new PlainTextConstruct(title));

			} else {
				// upload an existing file
				newDocument = new DocumentListEntry();
				if (mimeType != null) {
					newDocument.setFile(file, mimeType);
				} else {
					newDocument.setFile(file, DocumentListEntry.MediaType.TXT.getMimeType());	// just to try something
				}
				newDocument.setTitle(new PlainTextConstruct(title));
			}

			// there it goes
			entry = ds.insert(new URL(feedURL), newDocument);
			
			// save result to provide persistence
			htmlURL = entry.getHtmlLink().getHref();
			entryURL = entry.getEditLink().getHref();
			
			title = entry.getTitle().getPlainText();
			try {
				updated = dateTimeFormat822.parse(entry.getUpdated().toStringRfc822());
			} catch (ParseException e) {
				updated = new Date();
				System.err.println("Warning: creation date couldn't be parsed; using current date");	// TODO use a logger
			}
			
		} catch (MalformedURLException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Error in the feed URL", e);
			
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while sending request or reading the feed",  e);
			
		} catch (ServiceException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while retrieving feed; maybe a problem with protocol/library version", e);
		}
		
		/// writer permissions for everybody (only with Google Documents List API v3.0)
		// TODO apply conditionally
		try {
			// code for public docs
			//ds.insert(new URL(entry.getAclFeedLink().getHref()), new AclScope(AclScope.Type.DEFAULT, null), AclRole.WRITER);	// DEFAULT means "everybody"
			
			// code for shared-with-key docs 
			String aclEntryStart = 		"<?xml version='1.0' encoding='UTF-8'?><entry xmlns='http://www.w3.org/2005/Atom' xmlns:gAcl='http://schemas.google.com/acl/2007'>"; 
			String aclEntryCategory = 		"<category scheme='http://schemas.google.com/g/2005#kind' term='http://schemas.google.com/acl/2007#accessRule'/>";
			String aclEntryRole = 			"<gAcl:withKey><gAcl:role value='writer' /></gAcl:withKey>";
			String aclEntryScope = 			"<gAcl:scope type='default' />";
			String aclEntryEnd = 		"</entry>";
			String aclEntrySt = aclEntryStart + aclEntryCategory + aclEntryRole + aclEntryScope + aclEntryEnd;

			/*
			BaseEntry<AclEntry> aclEntry = BaseEntry.readEntry(new ParseSource(new java.io.StringReader(aclEntrySt)), AclEntry.class, ds.getExtensionProfile()); 
			aclEntry = ds.insert(new URL(entry.getAclFeedLink().getHref()), aclEntry);
			*/
			
			GDataRequest request = ds.getRequestFactory().getRequest(RequestType.INSERT, new URL(entry.getAclFeedLink().getHref()), ContentType.ATOM_ENTRY);
			request.setHeader("GData-Version", "3.0");
			request.getRequestStream().write(aclEntrySt.getBytes());
			request.execute();
			
			String key = null;
			InputStream is = request.getResponseStream();
			try {
				if (is != null) {
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					dbf.setNamespaceAware(true);
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document doc = db.parse(is);
					Node withKey = doc.getElementsByTagNameNS("http://schemas.google.com/acl/2007", "withKey").item(0);
					if (withKey != null)
						key = ((Element)withKey).getAttribute("key");
				}

			} catch (SAXException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unexpected error while parsing answer from Google Docs");

			} catch (ParserConfigurationException e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unexpected error while creating parser to process answer from Google Docs");
			}
			
			if (key == null)
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Access key to URL could not be retrieved");
			
			if (htmlURL.contains("?"))
				htmlURL += "&authkey=" + key;
			else
				htmlURL += "?authkey=" + key; 
			
			
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while inserting writer permissions, watch the target URL", e);
			
		} catch (ServiceException e) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Error while inserting writer permissions, bad try", e);
		}
		
	}

	
	/**
	 * Effective deletion of a Google Document.
	 * 
	 * @param	specificParams
	 * @return					String	Always null 
	 */
	//@Override
	public String delete(Map<String, String> specificParams) throws ResourceException {
		// search parameters
		String user = specificParams.get("user");	// Google docs username
		String pass = specificParams.get("pass");	// Google docs password
		
		// get access to Google Documents List service
		DocsService ds = GDataServiceManager.getDocsService(user, pass);
		if (ds == null)
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Google Docs service could not be connected");

		// check the knowledge about resource URL 
		if (entryURL == null)
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		
		// request deletion to Google Docs service
		try {
			entry = ds.getEntry(new URL(entryURL), DocumentListEntry.class);
			entry.delete();
			entry = null;
			
		} catch(IOException io) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Communication error while accesing to the Google Docs service", io);
			
		} catch(ResourceNotFoundException nf) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, nf);
			
		} catch(ServiceException s) {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Delete request failed because a Google Docs service exception", s);
		}
		
		return null;
		
	}


	/** 
	 * Getter for title 
	 * 
	 * @return Document title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Getter for last update date
	 * 
	 * @return Last update date
	 */
	public Date getUpdated() {
		return updated;
	}

	
	/**
	 * Getter for browser-friendly URL to the document.
	 * 
	 * @param 	callerUser	String					Name of the user asking for the URL
	 * @param	params		Map<String, String>		List of specific parameters
	 * @return 										Browser-friendly URL 
	 */
	//@Override
	public String getHtmlURL(String callerUser, Map<String, String> specificParams) {
		return htmlURL;
	}


	/**
	 * Getter for local identifier
	 * 
	 * @return	int		Local identifier of document
	 */
	public int getIndex() {
		return index;
	}
	
	
	//@Override
	public String getAccessParams(String callerUser, Map<String, String> specificParams) {
		String user = specificParams.get("user");
		String pass = specificParams.get("pass");
		return "user=" + Reference.encode(user) + "&pass=" + Reference.encode(pass);
	}


	/**
	 * Getter for docType
	 * 
	 * @return Google category string identifying the type of document 
	 */
	public String getDocType() {
		return docType;
	}
	
	
	/** 
	 * Getter for file 
	 * 
	 * @return 	File to upload, in the file system 
	 */
	public File getFile() {
		return file;
	}
	
	
	/** 
	 * Getter for MIME type 
	 * 
	 * @return 	MIME type 
	 */
	public String mimeType() {
		return mimeType;
	}
	
	
	/** 
	 * Getter for URL to the GData entry representing the document
	 * 
	 * @return 	URL to the GData entry representing the document
	 */
	public String getEntryURL() {
		return entryURL;
	}
	
	
	/**
	 * Getter for browser-friendly URL to the document.
	 * 
	 * @return Browser-friendly URL to the document.
	 */
	public String getHtmlURL() {
		return htmlURL;
	}


	//@Override
	public void saveSpecificState(PrintStream out) {
		out.println(entryURL);
		out.println(htmlURL);
	}


	//@Override
	public void loadSpecificState(BufferedReader in) throws IOException {
		entryURL = in.readLine();
		htmlURL = in.readLine();
	}

	
	//@Override
	public void setIndex(int index) {
		this.index = index; 
	}


	//@Override
	public void setUsers(List<String> users, String callerUser, Map<String, String> specificParams) {
		// nothing to do
	}
	
}
