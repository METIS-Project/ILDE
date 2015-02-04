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

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import glue.common.entities.instance.InstanceEntity;
import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.resources.GLUEResource;
import glue.common.server.Server;

import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Element;


/**
 * Resource instance factory, responsible for the creation of Gluelet instances corresponding to Google Documents.
 * 
 * @author	 	David A. Velasco
 * @contributor	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.common.resources
 */
public abstract class InstanceFactoryResource extends GLUEResource {
	

	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");



	/// abstract methods ///
	protected abstract String checkMissingParameters(String toolName, List<String> users, String callerUser, Map<String,String> specificParams);

	
	/// full methods ///
	
	/**
	 * POST InstanceFactoryResource
	 * 
	 * Creation and configuration of a Google Document.
	 *
	 * Access to Google Documents List service to create a new document, or upload an existing one.
	 * 
	 * With no handling of credentials yet. 
	 * 
	 * Method ::1:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	URI to new Google Document and 201 code, or null and error code in case of fail.
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
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Received entity couldn't be parsed", e);
			
		} finally {
			discardRepresentation(entity);
		}
		
		// search for common parameters
		String toolName = null;				// tool implementation name
		List<String> users = null; 			// list of users
		String callerUser = null;			// current user's name
		String parameters = null;			// specific parameters
		try {
			toolName = inputEntry.getExtendedTextChild("toolName");
			users = inputEntry.getExtendedStructuredList("users");
			callerUser = inputEntry.getExtendedTextChild("callerUser");
			parameters = inputEntry.getExtendedTextChild("parameters");
		} catch (RuntimeException e) {
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Error while reading common parameters", e);
		}
		
		// search for specific parameters
		Map<String, String> specificParams = decodeSpecificParams(parameters);

		// check missing parameters
		//String missing = checkMissingParameters(toolName, students, teacher, specificParams);
		String missing = checkMissingParameters(toolName, users, callerUser, specificParams);
		if (missing != null)
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missing);

		// process configuration form
		Element configEl = inputEntry.getExtendedStructuredChild("configuration");
		if (configEl == null)
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Configuration data not found");
		InstanceEntity ent =  Server.getInstance().getInstanceEntityFactory().createNewInstanceEntity(toolName, users, callerUser, specificParams, configEl); 
		
		// effective creation of new instance
		ent.create(callerUser, specificParams);		// it should throw ResourceExceptions when necessary; let it bubble up!! ; will be processed by doCatch() in parent class
		
		// add the new instance to the Instance Repository
		Server.getInstance().getInstanceEntityRepository().addInstanceEntity(ent);
		
		// return proper answer
		Representation rep = resultForCreateInstance(ent, callerUser, specificParams);	// build and return the Atom-formatted answer; 
		setStatus(Status.SUCCESS_CREATED);

		try {
			logger.info("** POST INSTANCE answer: \n" + (rep != null ? rep.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		
		return rep; 
		
	}
	

	/**
	 * Build an Atom-formatted representation to return as result of a successful instance creation. 
	 * 
	 * @param 	entity		Entity associated to the Google Doc after a successful creation
	 * @return				Atom-formated representation to return to clients (GLUEletManager), with the needed data for future use of the document.
	 */
	protected Representation resultForCreateInstance(InstanceEntity entity, String callerUser, Map<String, String> specificParams){
		// the important data
		String htmlURL = entity.getHtmlURL(callerUser, specificParams);
		int index = entity.getIndex();

		// creation
		FormattedEntry entry = new FormattedEntry();
	
		// entries MUST contain an Id
		//entry.setId(htmlURL);
		entry.setId(getReference().getIdentifier()+ "/" + index);
	
		// entries MUST contain an update date 
		entry.setUpdated(entity.getUpdated());
	
		// entries MUST contain a title
		entry.setTitle(entity.getTitle());
	
		// this entry won't ever be in a feed (!!!), so it MUST contain an author
		entry.addAuthor(FormatStatic.GSIC_NAME, null, null);
	
		// entries MUST contain an alternate link when don't contain a content element
		entry.setAlternateLink("HTML accesible URL", htmlURL);
	
		// gluelet parameters
		//String accessParams = entity.getAccessParams(teacher, specificParams);
		String accessParams = entity.getAccessParams(callerUser, specificParams);
		entry.addExtendedTextChild("glueletParams", (accessParams==null ? "" : accessParams));

		// get the built representation
		return entry.getRepresentation();
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
