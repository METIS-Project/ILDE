/**
 This file is part of GDataAdapterApi3.

 GDataAdapterApi3 is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GDataAdapterApi3 is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GDataAdapterApi3 for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GDataAdapterApi3 is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GDataAdapterApi3 and/or modify it under the
   terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>
 ]
*/
package glue.adapters.implementation.gdata3.entities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.List;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.google.gdata.util.common.util.Base64;
import com.google.gdata.util.common.util.Base64DecoderException;

import glue.common.entities.instance.InstanceEntity;
import glue.common.entities.instance.InstanceEntityFactory;
import glue.common.server.Server;

public class GoogleDocumentFactory implements InstanceEntityFactory {

	//@Override
	public InstanceEntity createLoadedInstanceEntity(int index, String title, Date updated) {
		return new GoogleDocument(index, title, updated);
	}

	//@Override
	public InstanceEntity createNewInstanceEntity(String toolName, List<String> users, String callerUser, Map<String, String> specificParams, Element configuration) {
		// select the tool to use; ignored for preexisting documents, but required by protocol
		String type = null;
		if (toolName.equals("Google Documents API 3") || toolName.equals("Google Documents"))
			type = GDataServiceManager.DOC_TYPE;
		else if (toolName.equals("Google Spreadsheets API 3") || toolName.equals("Google Spreadsheets"))
			type = GDataServiceManager.SPR_TYPE;
		else if (toolName.equals("Google Presentations API 3") || toolName.equals("Google Presentations"))
			type = GDataServiceManager.PRE_TYPE;
		else if (toolName.equals("Google Drawings API 3") || toolName.equals("Google Drawings"))
			type = GDataServiceManager.DRA_TYPE;
		else {
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Implementation adapter can't handle tool '" + toolName + "'");
		}
		
		// get the title for the new document
		String namespace = Server.getInstance().getInstanceNamespace();
		String title = configuration.getElementsByTagNameNS(namespace, "title").item(0).getTextContent();	// must be synchronized with GoogleDocsConfiguration.html ; a little too hardcoded
		if (title == null || title.length() <= 0) {
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Document title couldn't be found in the entity body");
		}
		
		// get the document, if contained in the message
		Element fileEl = (Element) configuration.getElementsByTagNameNS(namespace, "file").item(0);
		File tmpFile = null;
		String fileContent = null;
		String fileName = null;
		String mimeType = null;
		String contentType = null;
		if (fileEl != null) {	
			// extract content ; TODO don't assume content is in the XML, most general case is use of a multipart message (not implemented yet)
			fileContent = fileEl.getTextContent();
			
			// extract metadata
			NamedNodeMap atts = fileEl.getAttributes();
			if (atts.getLength() > 0) {
				Node fileNameAtt = atts.getNamedItemNS(namespace, "filename");	// file name ;-)
				if (fileNameAtt != null)
					fileName = fileNameAtt.getNodeValue();
				Node mimeTypeAtt = atts.getNamedItemNS(namespace, "mediatype");	// and MIME type :O
				if (mimeTypeAtt != null)
					mimeType = mimeTypeAtt.getNodeValue();
				Node contentTypeAtt = atts.getNamedItemNS("http://www.w3.org/2001/XMLSchema-instance", "type");	// content data type; determines the content encoding	// WARNING!! "*" is not a valid wildcard for namespace name in this getNamedItemNS
				if (contentTypeAtt != null)
					contentType = contentTypeAtt.getNodeValue();
				else {
					throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Content type couldn't be found in the entity body");
				}
			}
			
			FileOutputStream fos = null;
			try {
				// document is dumped in a temporal file for use with Google Documents List API; see code in glue.adapters.implementation.gdata.entities.GoogleDocument  
				if (fileName != null && fileName.length() > 0)
					tmpFile = File.createTempFile("GDA", fileName.substring(Math.max(0, fileName.lastIndexOf("."))));	// built for keep the extension in any case; see Java API about File
				else if (fileContent != null && fileContent.length() > 0)
					tmpFile = File.createTempFile("GDA", ".txt");	// in case of emergency
				else
					;	// no real content, no name; EMPTY FILE
				
				if (tmpFile != null) {
					// decode the file content while dumping
					fos = new FileOutputStream(tmpFile);
					long length = fileContent.length();
					if (contentType.endsWith("hexBinary")) {
						// XML Schema hexBinary decoding
						for (int i=0; i<length; i+=2) {
							int b = Integer.decode("0x" + fileContent.substring(i, i+2));	// this must be awful for memory
							fos.write(b);
						}

					} else if (contentType.endsWith("base64Binary")) {
						// XML Schema base64Binary decoding
						fos.write(Base64.decode(fileContent));		// a little nasty trick - using a not visible class in GData Java library
																	// TODO - search for a visible class to use, probably in other library; or write it (copy!!)

					} else {
						// XML Schema encoding
						tmpFile.delete();
						throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Not available or not valid data type for file content");
					}
					fos.close();
				}
					
			} catch (Base64DecoderException e) {
				if  (tmpFile != null && tmpFile.exists())
					tmpFile.delete();
				tmpFile = null;
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Fail while decoding uploaded file", e);
				
			} catch (IOException e) {
				if  (tmpFile != null && tmpFile.exists())
					tmpFile.delete();
				tmpFile = null;
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Fail while processing uploaded file", e);
				
			} 
		}

		
		// build the entity
		GoogleDocument doc = null;
		if (tmpFile != null && tmpFile.exists()) {
			// upload of an existing document
			doc = new GoogleDocument(title, tmpFile, mimeType);
		} else { 
			// creation of an empty document
			doc = new GoogleDocument(title, type);
		}
		
		return doc;
	}

}
