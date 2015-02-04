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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import glue.common.format.FormattedEntry;
import glue.common.resources.GLUEResource;
import glue.core.controllers.JpaControllersManager;
import glue.core.entities.Gluelet;
import glue.core.entities.ImplementationAdapter;
import glue.core.entities.ToolImplementation;
import glue.core.entities.Tool;
import glue.core.entities.ImplementationModel;

import org.restlet.data.Status;
import org.restlet.ext.atom.Person;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;


/**
 * Resource Gluelet instance factory, responsible for the creation of Gluelet instances. 
 * 
 * @author	 	David A. Velasco <davivel@gsic.uva.es>
 * @contributor	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.core.resources
 */

public class InstanceFactoryResource extends GLUEResource {
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");


	/// attributes ///
	
	/** Local id. Integer used as identifier in table of tool implementations, in Internal Registry */
	protected int toolImpLocalId;
	
	/** Local id. Integer used as identifier in table of (implementation) models, in Internal Registry */
	protected int modelLocalId;
	
    /** Implementation model data in Internal Registry */
	protected ImplementationModel implementationModel = null;
	
	/** Implementation adapter data in Internal Registry */
	protected ImplementationAdapter implementationAdapter = null;
	 
	
	/// methods ///
	
	/**
	 * POST InstanceFactoryResource
	 * 
	 * Creation and configuration of a Gluelet instance.
	 * 
	 * Dispatch the configuration data to the proper implementation adapter in order to trigger the actual creation.
	 *
	 * Method ::25:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	URI to new Gluelet resource, or null.
	 */
	@Post()
	public Representation createInstance(Representation entity) {
		
		// load Atom+GLUE formatted entity
		FormattedEntry inputEntry = null;
		try {
			String entityText = entity.getText();
			inputEntry = new FormattedEntry(entityText);
			//logger.info("** POST INSTANCE received: \n" + entityText);
			logger.info("** POST INSTANCE received: \n");
        	
		} catch (IOException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request couldn't be parsed", e);
			
		} finally {
			discardRepresentation(entity);
		} 
		
		// search for tool parameter
		try {
			String toolImpId = inputEntry.getExtendedTextChild("tool");
			String toolImpLocalIdString = toolImpId.substring(toolImpId.lastIndexOf("/")+1);
			toolImpLocalId = Integer.parseInt(toolImpLocalIdString);
			
		} catch (NumberFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Tool parameter not found", e);
		}
		
		
		// extract info for tool implementation from Internal Registry
		ToolImplementation toolImplementation = JpaControllersManager.getToolImplementationsController().findToolImplementation(toolImpLocalId);
		if (toolImplementation == null)
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Wrong tool implementation identifier " + toolImpLocalId);

		// search for associated implementation model
		int modelLocalId = toolImplementation.getModelId();
		implementationModel = JpaControllersManager.getImplementationModelsController().findImplementationModel(modelLocalId);
		if (implementationModel == null)
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "No implementation model found for tool implementation " + toolImpLocalId);
        
		// search URL to associated implementation Adapter
		implementationAdapter = JpaControllersManager.getImplementationAdaptersController().findImplementationAdapter(implementationModel.getAdapterId()); 

		// complete URL to implementation adapter
		String impAdapterURI = implementationAdapter.getUrl() + "/instance";
		
		// create new entity to POST
		Tool tool = JpaControllersManager.getToolsController().findTool(toolImplementation.getToolId());
		FormattedEntry outputEntry = new FormattedEntry();
		outputEntry.addExtendedTextChild("toolName", tool.getName());
		String params = toolImplementation.getParameters();
		if (params != null)
			outputEntry.addExtendedTextChild("parameters", params);
		outputEntry.copyExtendedElement(inputEntry, "configuration");
		//outputEntry.copyExtendedElement(inputEntry, "students");
		outputEntry.copyExtendedElement(inputEntry, "users");
		outputEntry.copyExtendedElement(inputEntry, "callerUser");
		
		// request to implementation adapter
		Representation impAdapterResponse = null;
		impAdapterResponse = postRemoteCreation(impAdapterURI, outputEntry.getRepresentation());
		
		Representation result = null;
		if (impAdapterResponse != null) {
			try {
				// wrap adapter response
				FormattedEntry response = new FormattedEntry(impAdapterResponse.getText());
				
				// create gluelet instance
				Gluelet gluelet = new Gluelet();
				// local reference to tool implementation used for creation of actual instance 
				gluelet.setToolImplementationId(toolImpLocalId);
				
				// save the URI to the actual instance
				gluelet.setUrl(response.getId());

				// TODO VLE identifier
				gluelet.setVle("TODO");
				
				// save creation date
				gluelet.setUpdated(new Date());
				
				// save additional parameters for future interactions with Implementation Adapter
				gluelet.setParameters(response.getExtendedTextChild("glueletParams"));
				
				// insertion of gluelet info in Gluelet Repository; gluelet id is automatically set by database
				JpaControllersManager.getGlueletsController().create(gluelet);
				
				// build the Atom-formatted answer
				result = resultForCreateInstance(gluelet, response);
					
			} catch (IOException e) {
				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected error while processing response from implementation adapter", e);

			} finally {
				discardRepresentation(impAdapterResponse);
			}
				
		} else {
			throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected null response from implementation adapter");
		}
		
		try {
			logger.info("** POST INSTANCE answer: \n" + (result != null ? result.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		
		return result;
			
	}
	

	/**
	 * POSTs configuration data for the actual creation of a new instance.
	 * 
	 * Sets adequate status for subsequent response.
	 * 
	 * @param	targetUri 	String			URL to the remote instance factory
	 * @param	rep			Representation 	Foramtted data to be sent as parameters
	 * @return	URL to the new instance, or null in case of fail.
	 */
	protected Representation postRemoteCreation(String targetUri, Representation rep) throws ResourceException {
		
		Status status = null;	
		Representation response = null;
		ClientResource remoteResource = new ClientResource(targetUri);
		try {
			response = remoteResource.post(rep);
			status = remoteResource.getStatus();
			if (status.equals(Status.SUCCESS_CREATED)) {
				setStatus(Status.SUCCESS_CREATED);
				return response;
			}
			
		} catch (ResourceException r) {
			discardRepresentation(remoteResource.getResponseEntity());	// the getResponseEntity() call is ABSOLUTELY NECESSARY to guarantee the right release of the remote connection
			throw convertResourceException(r);
		}
		// HTTP code is 2xx (but not 201) ; that's not right
		discardRepresentation(response);
		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from implementation adapter, HTTP status " + status);
		
	}
	
	
	/*-*
	 * ::26:: ; without interest right now
	 * @return
	 */
	/*@Get("xhtml")
	public Representation toXhtml() {
	}*/

	
	/**
	 * Build an Atom-formatted representation to return as result of a successful GLUElet creation. 
	 * 
	 * @param 	googleResult	Message returned by Google Docs List service as result of the creation.
	 * @return					Atom-formated representation to return to clients (GLUEletManager), with the needed data for future use of the document.
	 */
	protected Representation resultForCreateInstance(Gluelet gluelet, FormattedEntry adapterResponse) {
		// the important data: URL to a gluelet resource - TODO maybe a more independent way
		String glueletURL = getReference().getBaseRef().getIdentifier() + "/" + gluelet.getId();

		// creation
		FormattedEntry entry = new FormattedEntry();
		
		// entries MUST contain an Id
		entry.setId(glueletURL);
		
		// entries MUST contain an update date 
		entry.setUpdated(new Date());
		
		// entries MUST contain a title
		entry.setTitle(adapterResponse.getTitle());
		
		// this entry won't ever be in a feed (!!!), so it MUST contain an author
		List<Person> authors = adapterResponse.getAuthors();
		Iterator<Person> authorIt = authors.iterator();
		while (authorIt.hasNext()) {
			Person author = authorIt.next();
			entry.addAuthor(author.getName(), author.getEmail(), author.getUri().getIdentifier());
		}
		
		// entries MUST contain an alternate link when don't contain a <content> element
		entry.setAlternateLink("", glueletURL);
    	
		// get the built representation
		return entry.getRepresentation();
	}

	
}
