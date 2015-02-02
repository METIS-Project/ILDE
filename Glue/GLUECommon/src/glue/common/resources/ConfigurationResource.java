/**
 This file is part of GLUECommon.

 GLUECommon is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GLUECommon is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GLUECommon for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GLUECommon is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GLUECommon and/or modify it under the
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
package glue.common.resources;

import glue.common.entities.configuration.Configuration;
import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.resources.GLUEResource;
import glue.common.server.Server;

import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;


/**
 * Resource configuration.
 *
 * Form to be filled for creating a new Google Document.
 * 
 * Represents actual configuration form kept as a XHTML file in the file system. 
 * 
 * @author	 	David A. Velasco
 * @contributor Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.common.resources
 */

public class ConfigurationResource extends GLUEResource {

	
	/// attributes ///
	
	/** Local id, String used as identifier in forms index, at Application */
	protected String localId = null;
	
	/** Configuration entity that provides access to real configuration form data */
	protected Configuration configuration = null;


	
	/// methods ///

    /**
     * Resource initialization.
     * 
     * Parses URI searching for the local id of a configuration and requests the configuration form data to Application.   
     */
	@Override
	protected void doInit() throws ResourceException {
		// config id extraction (optional parameter)
		localId = (String) getRequest().getAttributes().get("configId");	// Get the "configId" attribute value taken from the URI template
		// tool extraction (mandatory parameter)
		Reference ref = getReference();
		Form query = ref.getQueryAsForm();
		String tool = query.getFirstValue("tool");
		if (tool == null || tool.length() <= 0)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Tool parameter not found");
		
		// get Configuration object to access proper configuration file
		configuration = Server.getInstance().getConfigurationRepository().getConfiguration(tool, localId);
		
		setExisting(configuration != null);
	}
	
	
	/**
	 * GET ConfigurationResource
	 * 
	 * Returns a form to be filled in order to create a new Google Document instance.
	 *
	 * Form in XForms format, hosted by XHTML; no additional message container (yet).
	 * 
	 * Methods ::6:: and ::10:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	Configuration form to be filled and 200 code, or null and error HTTP code in case of fail.
	 */
	@Get
	public Representation getConfiguration(){
		Representation rep = null;
		if (isExisting()) {
			// creation of formatted response
			FormattedEntry entry = new FormattedEntry();
				
			// entries MUST contain an Id
			entry.setId(getReference().getIdentifier());
				
			// entries MUST contain an update date 
			entry.setUpdated(new Date());
				
			// entries MUST contain a title
			String name = Server.getInstance().getAppName();
			if (localId == null)
				entry.setTitle("Default configuration definition from " + name);
			else
				entry.setTitle("Configuration definition " + localId + " from " + name);
				
			// this entry won't ever be in a feed (!!!), so it MUST contain an author
			entry.addAuthor(FormatStatic.GSIC_NAME, null, null);
				
			if (configuration.isEmpty()) {
				entry.setTextContent("");

			} else {
				try {
					DocumentBuilder docB = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document conf = docB.parse(configuration.getAsStream());
					if (conf != null) {
						// <content>
						entry.setXhtmlContent(conf);
					
					} else
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Corrupt configuration form");
				
				} catch (ParserConfigurationException e) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while creating an empty DOM document for parsing configuration form", e); 
			
				} catch (IOException e) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "File access error while parsing configuration form", e);
				
				} catch (SAXException e) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while parsing configuration form", e);
				}
			}
			
			// get the built representation
			rep = entry.getRepresentation();
			
		} else {
			setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		}
		return rep;
	}
	
}