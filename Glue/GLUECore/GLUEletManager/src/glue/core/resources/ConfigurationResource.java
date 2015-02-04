/**
 This file is part of GlueletManager.

 GlueletManager is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GlueletManager is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GlueletManager for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GlueletManager is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GlueletManager and/or modify it under the
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
package glue.core.resources;

import java.io.IOException;
import java.util.logging.Logger;

import glue.common.format.FormattedEntry;
import glue.common.resources.GLUEResource;
import glue.core.controllers.JpaControllersManager;
import glue.core.entities.ImplementationAdapter;
import glue.core.entities.Tool;
import glue.core.entities.ToolImplementation;
import glue.core.entities.ImplementationModel;

import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.data.Reference;


/**
 * Resource configuration.
 *
 * Form to be filled for have a Gluelet ready to be used.
 * 
 * Represents actual configuration form known by an implementation adapter. 
 * 
 * @author	 	David A. Velasco <davivel@gsic.uva.es>
 * @contributor	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.core.resources
 */

public class ConfigurationResource extends GLUEResource {

	/// attributes ///
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	
	
	/** Tool implementation data in Internal Registry */
	protected ToolImplementation toolImplementation = null;
	
	/** Tool data in Internal Registry */
	protected Tool tool = null;

	/** Implementation model data in Internal Registry */
	protected ImplementationModel implementationModel = null;

	/** Implementation adapter data in Internal Registry */
	protected ImplementationAdapter implementationAdapter = null;

	
	/// methods ///
	
    /**
     * Resource initialization.
     * 
     * Parse URI searching for the local id of a tool and retrieves data about its corresponding implementation adapter from Internal Registry.  
     */
	@Override
	protected void doInit() throws ResourceException {
		String toolImpIdString = (String) getRequest().getAttributes().get("toolId");
		int toolImpLocalId;
		try {
			toolImpLocalId = Integer.parseInt(toolImpIdString);
		} catch (NumberFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Wrong identifier format in the URL", e);
		}
        
		// get the controller for accessing the GlueTools entity and find it in the Internal Registry
		toolImplementation = JpaControllersManager.getToolImplementationsController().findToolImplementation(toolImpLocalId);
        
		if (toolImplementation != null) {
			tool = JpaControllersManager.getToolsController().findTool(toolImplementation.getToolId());
			implementationModel = JpaControllersManager.getImplementationModelsController().findImplementationModel(toolImplementation.getModelId());
			if (implementationModel != null) {
				implementationAdapter = JpaControllersManager.getImplementationAdaptersController().findImplementationAdapter(implementationModel.getAdapterId());
			}
		}
        
		// Does it exist?
		setExisting(this.implementationAdapter != null);	// setting 'false' implies that REST methods won't start; server will respond with 404
			
	}

	
	/**
	 * GET ConfigurationResource
	 * 
	 * Returns a form to be filled in order to configure a Gluelet instance. The form is GOT from remote implementation adapter.
	 *
	 * Form in XForms format, contained in xhtml; no additional message container (yet).
	 * 
	 * Method ::25*:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	Configuration form to be filled, or null.
	 */
	@Get
	public Representation getConfiguration() throws ResourceException {
		
		logger.info("** GET CONFIGURATION received");

		Representation result = null;
		if (isExisting()) {
			// build URI for configuration at implementation adapter from data in the Internal Registry 
			String targetURI = implementationAdapter.getUrl() + "/configuration";
			// add params to the URI
			targetURI += "?tool=" + Reference.encode(tool.getName());
			// call to ::6:: method
			Representation impAdapterResponse = null;
			impAdapterResponse = getRemoteResource(targetURI);
			if (impAdapterResponse != null) {
				try {
					FormattedEntry entry = new FormattedEntry(impAdapterResponse.getText());	// AT THIS MOMENT, WE ARE LOSING TRACK OF THE REPRESENTATION SENT BY THE SERVER IN OK CASES <<<
					entry.setId(getReference().getIdentifier());
					result = entry.getRepresentation();		
				
				} catch (IOException e) {
					throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected error while processing response from implementation adapter", e);

    			} finally {
    				discardRepresentation(impAdapterResponse);
				}
				
			} else {
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected null response from implementation adapter");
			}
			
		} else {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}
		
		try {
			logger.info("** GET CONFIGURATION answer: \n" + (result != null ? result.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		
		return result;
    	
	}
	
	
	/**
	 * GETs a configuration form from a remote implementation adapter. 
	 * 
	 * Sets adequate status for subsequent response.
	 * 
	 * @param	targetURI String	URL to the remote configuration to be GOT
	 * @return	Configuration form as sent by the implementation adapter, or null in case of fail.
	 */
	protected Representation getRemoteResource(String targetURI) throws ResourceException {
		
		Status status = null;	
		Representation response = null;
		ClientResource remoteResource = new ClientResource(targetURI);
		try {
			response = remoteResource.get();
			status = remoteResource.getStatus();			
			if (status.equals(Status.SUCCESS_OK))
				return response;	// right exit
				
		} catch (ResourceException r) {
			discardRepresentation(remoteResource.getResponseEntity());	// the getResponseEntitiy() call is ABSOLUTELY NECESSARY to guarantee the right release of the remote connection
			throw convertResourceException(r);
		} 
		
		// HTTP code is 2xx (but not 200) ; that's not right
		discardRepresentation(response);
		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from implementation adapter, HTTP status " + status);
	}
	
}