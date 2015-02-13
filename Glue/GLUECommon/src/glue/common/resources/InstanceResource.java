/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletCommon.
 * 
 * GlueletCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.common.resources;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import glue.common.entities.instance.InstanceEntity;
import glue.common.format.FormattedEntry;
import glue.common.resources.GLUEResource;
import glue.common.server.Server;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;


/**
 * Resource adapter instance.
 * 
 * @author	 	David A. Velasco
 * @version 	2012092501
 * @contributor Javier Enrique Hoyos Torio
 * @package 	glue.common.resources
 */

public abstract class InstanceResource extends GLUEResource {
	
	/// attributes ///

	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
	

	/** Instance data stored in the implementation adapter */
    protected InstanceEntity instance;

    
    /// abstract methods ///
    
	protected abstract String checkMissingParametersInDelete(Map<String, String> specificParams);
	
	//protected abstract String checkMissingParametersInGet(String user);
    
	protected abstract String checkMissingParametersInPost(String callerUser, List<String> users, Map<String, String> specificParams);
	
    
	/// full methods ///
    
    /**
     * Resource initialization.
     * 
     * Parse URI searching for the local id and extract info, if instance exists.
     */
    @Override
    protected void doInit() throws ResourceException {
        // get the "instanceId" attribute value taken from the URI template /instance/{instanceId}
        String instanceLocalIdString = (String) getRequest().getAttributes().get("instanceId");
        try {
        	int instanceLocalId = Integer.parseInt(instanceLocalIdString);
        	// get a controller for accessing the Gluelets entity and find it in the Internal Registry
        	instance = Server.getInstance().getInstanceEntityRepository().getInstanceEntity(instanceLocalId);
		} catch (NumberFormatException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Wrong identifier format in the URL", e);
		}
        
        // does it exist?
        setExisting(this.instance != null);
    }
    
    
	/**
	 * GET InstanceResource
	 * 
	 * Returns the instance data, including the browser friendly URL to the actual instance.
	 * 
	 * Method TO_ADD at Informe_041b_08_02_09.pdf
	 * 
	 * @return	'Atomized' gluelet info known by the implementation adapter
	 */
    //@Get("atom")
    @Get
    public Representation getInstance() {
    //public Entry toAtom()  {
        if(this.isExisting()) {
    		// search for URL parameters
    		Reference ref = getReference();
    		Form query = ref.getQueryAsForm();
    		String callerUser = query.getFirstValue("callerUser");
    		if (callerUser == null)
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "callerUser parameter not found");
        	
    		// check missing parameters
    		/*
    		String missing = checkMissingParametersInGet(userName);
    		if (missing != null)
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missing);
    		*/
            	
           	// required Atom fields
           	FormattedEntry entry = new FormattedEntry();
        	entry.setId(ref.getIdentifier());
           	entry.setTitle(instance.getTitle());
           	entry.setUpdated(instance.getUpdated());
           	
    		// this entry won't ever be in a feed (!!!), so it MUST contain an author
    		//entry.addAuthor(FormatStatic.GSIC_NAME, null, null);
           	entry.addAuthor(Server.getInstance().getAuthorName(), null, null);

           	// optional Atom fields
           	String htmlURL = instance.getHtmlURL(callerUser, null);
           	if (htmlURL == null || htmlURL.length() <= 0)
           		throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Lost URL");
           	entry.setAlternateLink("Browser friendly URL", htmlURL);
        	
        	return entry.getRepresentation();
	        	
        }
        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
    }

    
	/**
	 * POST InstanceResource
	 * 
	 * Modify the users list of the instance.
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
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Received entity couldn't be parsed", e);
    			
    		} finally {
   				discardRepresentation(entity);
    		}
    		
    		// search for common parameters
    		List<String> users = null; 			// list of users
    		String callerUser = null;			// current user name
    		String parameters = null;			// specific parameters
    		try {
    			users = inputEntry.getExtendedStructuredList("users");
    			callerUser = inputEntry.getExtendedTextChild("callerUser");
    			parameters = inputEntry.getExtendedTextChild("parameters");
    		} catch (RuntimeException e) {
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Error while reading common parameters", e);
    		}
    		
    		// search for specific parameters
    		Map<String, String> specificParams = decodeSpecificParams(parameters);

    		// check missing parameters
    		String missing = checkMissingParametersInPost(callerUser, users, specificParams);
    		if (missing != null)
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missing);

    		
    		// effective modification of the instance
    		Server.getInstance().getInstanceEntityRepository().updateInstanceUsers(instance.getIndex(), users, callerUser, specificParams);	// it should throw ResourceExceptions when necessary; let it bubble up!! ; will be processed by doCatch() in parent class
    		//instance.setUsers(users, callerUser, specificParams);		
    		//Server.getInstance().getInstanceEntityRepository().saveEntities();
    		
           	// required Atom fields
			FormattedEntry entry = new FormattedEntry();
			entry.setId(getReference().getIdentifier());
			entry.setTitle(instance.getTitle());
			entry.setUpdated(instance.getUpdated());	// it should be different
           	
			// this entry won't ever be in a feed (!!!), so it MUST contain an author
			entry.addAuthor(Server.getInstance().getAuthorName(), null, null);

			// optional Atom fields
			String htmlURL = instance.getHtmlURL(callerUser, specificParams);
			if (htmlURL == null || htmlURL.length() <= 0)
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Lost URL");
			entry.setAlternateLink("Browser friendly URL", htmlURL);
			setStatus(Status.SUCCESS_OK);

			Representation result = entry.getRepresentation();
				
   			try {
   				logger.info("** POST(modify low-REST)INSTANCE answer: \n" + (result != null ? result.getText() : "NULL"));
   			} catch (IOException io) {
   				throw new RuntimeException("Extremely weird failure while trying to log", io);
   			}
   			
        	return result;
	        	
        }
        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
    }

    
	/**
	 * DELETE Instance
	 * 
	 * Deletion of a GLUElet instance.
	 *
	 * Access to the tool implementation service in order to delete an existing instance.
	 * 
	 * Method ::4:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	Empty representation; HTTP Status according the deletion result.
	 */
	@Delete()
	public Representation delete() {
        if(this.isExisting()) {
    		// search for specific parameters
    		Map<String, String> specificParams = decodeSpecificParams(getReference());
    		
    		// check missing parameters
    		String missing = checkMissingParametersInDelete(specificParams);
    		if (missing != null)
    			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missing);
        	
    		// effective deletion of document at Google Docs
    		String result = instance.delete(specificParams);	// it must throw its own exceptions

    		// delete from Instances Repository
			Server.getInstance().getInstanceEntityRepository().deleteInstanceEntity(instance.getIndex());
    		
   			setStatus(Status.SUCCESS_OK);
    		return (result == null ? null : new StringRepresentation(result));
        }
        throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND);
	}


	/**
	 * 
	 * @param ref
	 * @return
	 */
	protected Map<String, String> decodeSpecificParams(Reference ref) {
		Form query = ref.getQueryAsForm();
		return query.getValuesMap();
	}
	
	/**
	 * Decodes a string with adapter-specific parameters, needed for instance creation.
	 * 
	 * @return	Map<String, String>		Set of name-value pairs with the decoded parameters. 
	 */
	protected Map<String, String> decodeSpecificParams(String parameters) throws ResourceException {
		HashMap<String, String> result = new HashMap<String, String>();
		if (parameters != null) {
			StringTokenizer tokenizer = new StringTokenizer(parameters, "&");
			while (tokenizer.hasMoreElements()) {
				String token = tokenizer.nextToken();
				int pos = token.indexOf("=");
				if (pos > 0) {
					result.put(token.substring(0, pos), Reference.decode(token.substring(pos+1)));
				}
			}
		}
		return result;
	}

	
}
