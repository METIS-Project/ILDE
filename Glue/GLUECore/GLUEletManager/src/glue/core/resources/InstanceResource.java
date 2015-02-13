/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletManager.
 * 
 * GlueletManager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletManager is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.core.resources;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import glue.common.format.FormattedEntry;
import glue.common.resources.GLUEResource;
import glue.core.controllers.JpaControllersManager;
import glue.core.controllers.exceptions.NonexistentEntityException;
import glue.core.entities.Gluelet;
import glue.core.entities.ToolImplementation;

import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;


/**
 * Resource GLUElet instance.
 * 
 * @author	 	David A. Velasco <davivel@gsic.uva.es>
 * @contributor	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.core.resources
 */

public class InstanceResource extends GLUEResource {
	
	/// attributes ///
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	

	/** Gluelet (instance) data stored in the Instances Repository */
    protected Gluelet instance;

    
	/// methods ///
    
    /**
     * Resource initialization.
     * 
     * Parse URI searching for the local id and extract info from Internal Registry, if gluelet exists.
     */
    @Override
    protected void doInit() throws ResourceException {
   		// get the "instanceId" attribute value taken from the URI template /instance/{instanceId}
   		String instanceLocalIdString = (String) getRequest().getAttributes().get("instanceId");
   		int instanceLocalId;
   		try {
   			instanceLocalId = Integer.parseInt(instanceLocalIdString);
		} catch (NumberFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Wrong identifier format in the URL", e);
		}

   		// get a controller for accessing the Gluelets entity and find it in the Internal Registry
   		instance = JpaControllersManager.getGlueletsController().findGluelet(instanceLocalId);	
        
   		// does it exist?
   		setExisting(this.instance != null);
    }
    
    
	/**
	 * GET InstanceResource
	 * 
	 * Returns the URL to the actual instance.
	 * 
	 * Method ::30:: at Informe_041b_08_02_09.pdf
	 * 
	 * @return	'Atomized' gluelet info known by GLUEletManager
	 */
    //@Get
    @Get("atom")
    public Representation getInstance() {
    	
    	logger.info("** GET INSTANCE received");
		Representation result = null;
    	
    	if(this.isExisting()) {

    		// search for URL parameters
    		Reference ref = getReference();
    		Form query = ref.getQueryAsForm();
    		String callerUser = query.getFirstValue("callerUser");
    		if (callerUser == null)
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "callerUser parameter not found");
			
    		// call to NEW method
    		Representation impAdapterResponse = getRemoteResource(instance.getUrl() + "?callerUser=" + Reference.encode(callerUser));    	
    		if (impAdapterResponse != null) {
    			try {
    				// wrap adapter response
    				FormattedEntry response = new FormattedEntry(impAdapterResponse.getText());	// AT THIS MOMENT, WE ARE LOSING TRACK OF THE REPRESENTATION SENT BY THE SERVER IN OK CASES <<<

    				// change id
    				response.setId(getReference().getIdentifier());
					
    				// and that's all; build the Atom-formatted answer
    				result = response.getRepresentation();
    					
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
    		logger.info("** GET INSTANCE answer: \n" + (result != null ? result.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		return result;
			
    }

    
	/**
	 * GET HTML InstanceResource
	 * 
	 * Returns the URL to the actual instance.
	 * 
	 * Method ::30:: at Informe_041b_08_02_09.pdf (this representation possibly is not documented)
	 * 
	 * @return	HTML representation of the instance URL known by GLUEletManager
	 */
    @Get("html")
    public Representation getHTMLInstance() {
    	
    	logger.info("** GET HTML INSTANCE received");
		Representation result = null;
    	
    	if(this.isExisting()) {

    		// search for URL parameters
    		Reference ref = getReference();
    		Form query = ref.getQueryAsForm();
    		String callerUser = query.getFirstValue("callerUser");
    		if (callerUser == null)
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "callerUser parameter not found");
			
    		// call to NEW method
    		Representation impAdapterResponse = getRemoteResource(instance.getUrl() + "?callerUser=" + Reference.encode(callerUser));    	
    		if (impAdapterResponse != null) {
    			try {
    				// wrap adapter response
    				FormattedEntry response = new FormattedEntry(impAdapterResponse.getText());	// AT THIS MOMENT, WE ARE LOSING TRACK OF THE REPRESENTATION SENT BY THE SERVER IN OK CASES <<<

//    				// change id
//    				response.setId(getReference().getIdentifier());
//    				// and that's all; build the Atom-formatted answer
//    				result = response.getRepresentation();
    					
    				//look for the URL and title in the response
    				String title = response.getTitle();
    				
    				List<String> links = response.getLinks();
    				
    				//build the HTML representation with the URL
    				result = buildHTMLInstanceRepresentation(links.get(0), title);
    				
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
    		logger.info("** GET HTML INSTANCE answer: \n" + (result != null ? result.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		return result;
			
    }

    
	private Representation buildHTMLInstanceRepresentation(String url,
			String title) throws IOException {
		
		String htmlContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" dir=\"ltr\"><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n<title>" + title + "</title>\n</head>\n<body onload=\"window.location.replace('"+url+"')\">\n</body>\n</html>\n";
		StringRepresentation rep = new StringRepresentation((CharSequence) htmlContent, MediaType.TEXT_HTML);
		rep.setCharacterSet(CharacterSet.UTF_8);
		
		return rep;
	}


    
	/**
	 * DELETE InstanceResource
	 * 
	 * Delete the GLUElet.
	 * 
	 * Method ::32:: at Informe_041b_08_02_09.pdf
	 * 
	 * @return	HTTP succes status (200, 202 or 204) in case of success, error status in other case 
	 */
    @Delete()
    public Representation delete()  {
    		
   		logger.info("** DELETE INSTANCE received");
    	
   		if(this.isExisting()) {
   			String targetURI = instance.getUrl();
        	
   			// add params to the URL
   			String glueletParams = instance.getParameters();
   			if (glueletParams != null)
   				targetURI += "?" + glueletParams;	// no aditional encoding is required for 'glueletParams'; the are saved URL-encoded in the data base
			
   			// call to ::4:: method
   			Status st = deleteRemoteResource(targetURI);
			
   			// set status result
   			getResponse().setStatus(st);
			
   			// delete in GLUEletManager database!!
   			if (st.isSuccess()) {
   				try {
   					JpaControllersManager.getGlueletsController().destroy(instance.getId());
   					instance = null;
   					setExisting(false);
					
   					logger.info("** DELETE INSTANCE complete");
					
   				} catch (NonexistentEntityException e) {
    					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Wrong instance identifier (not existent in Instances Repository)", e);
   				}
   			}
    			
   		} else {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}
    		
   		return null;
        
    }
    
    
	/**
	 * POST InstanceResource
	 * 
	 * Modify the users list of the GLUElet.
	 * 
	 * @return	HTTP succes status (200, 202 or 204) in case of success, error status in other case 
	 */
    @Post()
    public Representation modifyUsersList(Representation entity)  {
    		
   		if(this.isExisting()) {
   			// load Atom+GLUE formatted entity
   			FormattedEntry inputEntry = null;
   			try {
   				String entityText = entity.getText();
   				inputEntry = new FormattedEntry(entityText);
   				logger.info("** POST(modify low-REST) INSTANCE received: \n" + entityText);
   	        	
   			} catch (IOException e) {
   				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Request couldn't be parsed", e);
   				
   			} finally { 
   				discardRepresentation(entity);
   			}
   			
   			String targetURI = instance.getUrl();
        	
   			// create new entity to rePOST
   			FormattedEntry outputEntry = new FormattedEntry();
   			String glueletParams = instance.getParameters();
   			ToolImplementation toolImplementation = JpaControllersManager.getToolImplementationsController().findToolImplementation(instance.getToolImplementationId());
   			String postParams = toolImplementation.getParameters();
   			if (glueletParams != null) {
   				if (postParams != null)
   					glueletParams += "&" + postParams;
   			} else { 
   				glueletParams = postParams;
   			}
   			if (glueletParams != null)
   				outputEntry.addExtendedTextChild("parameters", glueletParams);
   			//outputEntry.copyExtendedElement(inputEntry, "students");
   			outputEntry.copyExtendedElement(inputEntry, "users");
   			outputEntry.copyExtendedElement(inputEntry, "callerUser");
			
   			// call to remote method
   			Representation impAdapterResponse = null;
   			impAdapterResponse = postRemoteCreation(targetURI, outputEntry.getRepresentation());
   			
   			
   			Representation result = null;
    		if (impAdapterResponse != null) {
    			try {
    				// wrap adapter response
    				FormattedEntry response = new FormattedEntry(impAdapterResponse.getText());	// AT THIS MOMENT, WE ARE LOSING TRACK OF THE REPRESENTATION SENT BY THE SERVER IN OK CASES <<<

    				// change id
    				response.setId(getReference().getIdentifier());
					
    				// and that's all; build the Atom-formatted answer
    				result = response.getRepresentation();
    					
    			} catch (IOException e) {
    				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected error while processing response from implementation adapter", e);
    				
    			} finally {
    				discardRepresentation(impAdapterResponse);
    			}
			
   			} else {
   				throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected null response from implementation adapter");
   			}
   			
   			
   			try {
   				logger.info("** POST(modify low-REST)INSTANCE answer: \n" + (result != null ? result.getText() : "NULL"));
   			} catch (IOException io) {
   				throw new RuntimeException("Extremely weird failure while trying to log", io);
   			}
   			
   			return result;
   			
   			/**/
   			
   		} else {
			throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
		}
        
    }
    
    
	/**
	 * GETs an instance resource from a remote implementation adapter. 
	 * 
	 * Sets adequate status for subsequent response.
	 * 
	 * @param	targetURI String	URL to the remote instance to be GOT
	 * @return	Instance data as sent by the implementation adapter, or null in case of fail.
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
			discardRepresentation(remoteResource.getResponseEntity());
			throw convertResourceException(r);
		}
		
		// HTTP code is 2xx (but not 200) ; that's not right
		discardRepresentation(response);
		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from implementation adapter, HTTP status " + status);
		
	}
    
	
	/**
	 * POSTs a new list of students for the actual modification of an existing instance.
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
			if (status.equals(Status.SUCCESS_CREATED)/* && remoteResource.getResponseEntity().isAvailable()*/) {
				setStatus(Status.SUCCESS_CREATED);
				return response;
			}
			if (status.equals(Status.SUCCESS_OK) /*&& remoteResource.getResponseEntity().isAvailable()*/) {
				setStatus(Status.SUCCESS_OK);
				return response;
			}
			
		} catch (ResourceException r) {
			discardRepresentation(remoteResource.getResponseEntity());	// the getResponseEntitiy() call is ABSOLUTELY NECESSARY to guarantee the right release of the remote connection
			throw convertResourceException(r);
		} 
		
		// HTTP code is 2xx (but not 201) ; that's not right
		discardRepresentation(response);
		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from implementation adapter, HTTP status " + status);
		
	}
	
	

    
	/**
	 * Delete the actual GLUElet.
	 * 
	 * @param	targetUri 	String		URL to delete the the remote resource
	 * @return	HTTP status for the subsequence response.
	 */
    protected Status deleteRemoteResource(String targetURI) throws ResourceException {
    	
		Status status = null;	
		ClientResource remoteResource = new ClientResource(targetURI);
		try {
			remoteResource.delete();
			status = remoteResource.getStatus();
			if (status.equals(Status.SUCCESS_OK) ||
				status.equals(Status.SUCCESS_ACCEPTED) ||
				status.equals(Status.SUCCESS_NO_CONTENT)) {
				
				return status;
			}
			
		} catch (ResourceException r) {
			throw convertResourceException(r);
			
		} finally  {	
			discardRepresentation(remoteResource.getResponseEntity());
		}
		
		// HTTP code is a not accepted 2xx
		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from implementation adapter, HTTP status " + status);
    }

}
