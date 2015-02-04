package glueps.core.resource;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedFeed;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Get;

import com.google.gson.Gson;

import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.persistence.JpaManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Resource 'list of learning environment installations'
 * 
 * List of all the learning environment installations
 * 
 * @author	 	Javier E. Hoyos
 * @version 	2013260200
 * @package 	glueps.core.resources
 */

public class LearningEnvironmentInstallationListResource extends GLUEPSResource {
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");


	/**
	 * GET LearningEnvironmentInstallationListResource
	 * 
	 * Returns a list with all the LearningEnvironment installations
	 * 
	 * @return	'Atomized' list of learning Environments known by Glue!PS
	 */
	//@Get("atom")
    @Get("xml|html")
    public Representation getLearningEnvironmentInstallationList() {
    	
   		logger.info("** GET learning Environments Installations received");
    	
   		FormattedFeed feed = new FormattedFeed();
		String uri = getReference().getIdentifier();

		uri = doGluepsUriSubstitution(uri);
		
		// Atom standard elements
		feed.setId(uri);  
		feed.setTitle("List of Learning Environments Installations (learning environment installations)");
		feed.addAuthor(FormatStatic.GSIC_NAME, null, null);
		feed.setSelfLink(null, uri);
		
		
		JpaManager dbmanager = JpaManager.getInstance();
		try{
			List<LearningEnvironmentInstallation> leis = dbmanager.listLEInstObjects();
			for(LearningEnvironmentInstallation leInst : leis){
				LearningEnvironmentInstallationResource leiItem = new LearningEnvironmentInstallationResource();
				leiItem.setLearningEnvironmentInstallation(leInst.getId(), getReference().toString());
				feed.getEntries().add(leiItem.getLearningEnvironmentInstallationEntry());
			}	
		}
		catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Internal error while trying to retrieve learning environment installations from DB", e);
		}
		
		Representation answer = feed.getRepresentation();
		
		try {
			logger.info("** GET LEARNING ENVIRONMENTS INSTALLATIONS answer: \n" + (answer != null ? answer.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		return answer;
		
	}
    
	/**
	 * GET LearningEnvironmentInstallationListResource
	 * 
	 * Returns a list with all the LearningEnvironment installations
	 * 
	 * @return	'Atomized' list of learning Environments known by Glue!PS
	 */
    @Get("json")
    public Representation getJsonLearningEnvironmentInstallationList() {
    	
   		logger.info("** GET JSON learning Environments Installations received");
   		Representation answer = null;

		JpaManager dbmanager = JpaManager.getInstance();
		ArrayList<LearningEnvironmentInstallation> leInstallations = new ArrayList<LearningEnvironmentInstallation>();
		try{
			List<LearningEnvironmentInstallation> leis = dbmanager.listLEInstObjects();
			for(LearningEnvironmentInstallation leInst : leis){
				leInstallations.add(URLifyLearningEnvironmentInstallation(leInst, getReference().getParentRef().getIdentifier()));
			}
		}
		catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Internal error while trying to retrieve learning environment installations from DB", e);
		}
		
		// convert java object to JSON format,
		// and returned as JSON formatted string
		Gson gson = new Gson();
		String json = gson.toJson(leInstallations);
   		 		
   		if (json != null){
   			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
		
		logger.info("** GET JSON learning Environments Installations answer: \n" + (answer != null ? json : "NULL"));
		return answer;
		
	}
    

}
