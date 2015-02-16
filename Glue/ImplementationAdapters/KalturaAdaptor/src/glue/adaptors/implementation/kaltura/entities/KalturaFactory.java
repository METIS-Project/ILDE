/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Kaltura Adapter.
 * 
 * Kaltura Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Kaltura Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adaptors.implementation.kaltura.entities;

import glue.common.entities.instance.InstanceEntity;
import glue.common.entities.instance.InstanceEntityFactory;
import glue.common.server.Server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class KalturaFactory implements InstanceEntityFactory {

	@Override
	public InstanceEntity createLoadedInstanceEntity(int index, String title, Date updated) {
		return new Kaltura(index, title, updated);
	}

	@Override
	public InstanceEntity createNewInstanceEntity(String toolName, List<String> users, String callerUser, Map<String, String> specificParams, Element configuration) {
	
		// get the title for the new video
		String namespace = Server.getInstance().getInstanceNamespace();
		String title = configuration.getElementsByTagNameNS(namespace, "title").item(0).getTextContent();	// must be synchronized with GoogleDocsConfiguration.html ; a little too hardcoded
		if (title == null || title.length() <= 0) {
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Document title couldn't be found in the entity body");
		}
		
		// get the video, if contained in the message
		Element fileEl = (Element) configuration.getElementsByTagNameNS(namespace, "file").item(0);
		File tmpFile = null;
		String fileContent = null;
		String fileName = null;
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
//				Node mimeTypeAtt = atts.getNamedItemNS(namespace, "mediatype");	// and MIME type :O
//				if (mimeTypeAtt != null)
//					mimeType = mimeTypeAtt.getNodeValue();
				Node contentTypeAtt = atts.getNamedItemNS("http://www.w3.org/2001/XMLSchema-instance", "type");	// content data type; determines the content encoding	// WARNING!! "*" is not a valid wildcard for namespace name in this getNamedItemNS
				if (contentTypeAtt != null)
					contentType = contentTypeAtt.getNodeValue();
				else {
					throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Content type couldn't be found in the entity body");
				}
			}
			
			FileOutputStream fos = null;
			try {
				// video is dumped in a temporal file for use with Kaltura API; see code in glue.adaptors.implementation.gdata.entities.GoogleDocument  
				if (fileName != null && fileName.length() > 0)
					tmpFile = File.createTempFile("kaltura_", fileName.substring(Math.max(0, fileName.lastIndexOf("."))));	// built for keep the extension in any case; see Java API about File
				else if (fileContent != null && fileContent.length() > 0)
					tmpFile = File.createTempFile("kaltura_", ".txt");	// in case of emergency
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

					} else {
						// XML Schema encoding
						tmpFile.delete();
						throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Not available or not valid data type for file content");
					}
					fos.close();
				}
					
			} catch (IOException e) {
				if  (tmpFile != null && tmpFile.exists())
					tmpFile.delete();
				tmpFile = null;
				throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Fail while processing uploaded file", e);
				
			} 
		}
		
		Kaltura kaltura = null;
		// build the entity
		if (tmpFile != null && tmpFile.exists()) {
			// upload of an existing video
			kaltura = new Kaltura(title, tmpFile);
		} else{
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Fail while processing uploaded file. You have to upload a file");
		}
		
				
		return kaltura;
	}

}
