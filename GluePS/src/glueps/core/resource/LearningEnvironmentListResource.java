/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Glue!PS.
 * 
 * Glue!PS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Glue!PS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package glueps.core.resource;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glue.common.format.FormattedFeed;
import glue.common.resources.GLUEResource;

import org.restlet.Request;
import org.restlet.data.CharacterSet;
import org.restlet.data.Disposition;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;

import org.restlet.data.Status;
import org.restlet.engine.http.connector.ConnectedRequest;
import org.restlet.engine.http.connector.ServerConnection;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import org.restlet.resource.Get;
import org.xmldb.api.base.Collection;

import com.google.gson.Gson;


import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.IStaticVLEDeployer;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.model.Deploy;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.DeployEntity;
import glueps.core.persistence.entities.LearningEnvironmentEntity;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Resource 'list of tools' (tool implementations, indeed).
 * 
 * List of all the registered tool implementations available to create Gluelets. 
 * 
 * @author	 	David A. Velasco <davivel@gsic.uva.es>
 * @original	juaase
 * @version 	2010042000
 * @package 	glue.core.resources
 */

public class LearningEnvironmentListResource extends GLUEPSResource {
	
	private static final String NEW_LE_NAME_FIELD = "leName";
	private static final String NEW_LE_INSTALLATION_FIELD = "leInstallation";
	private static final String NEW_LE_USER_FIELD = "leUser";
	private static final String NEW_LE_PASSWORD_FIELD = "lePassword";
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");


	/**
	 * GET ToolImplementationsListResource
	 * 
	 * Returns a list with all registered tool implementations.
	 * 
	 * Method ::18:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	'Atomized' list of learning Environment known by GLUEletManager
	 */
	//@Get("atom")
    @Get("xml|html")
    public Representation getLearningEnvironmentsList() {
    	
   		logger.info("** GET learning Environments received");
   		
   		String userid = this.getRequest().getChallengeResponse().getIdentifier();
   		if(userid==null) throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to get the learning environment caller user");
    	
   		FormattedFeed feed = new FormattedFeed();
		String uri = getReference().getIdentifier();

		uri = doGluepsUriSubstitution(uri);
		
		// Atom standard elements
		feed.setId(uri);  
		feed.setTitle("List of Learning Environments (learning environment)");
		feed.addAuthor(FormatStatic.GSIC_NAME, null, null);
		feed.setSelfLink(null, uri);
		
		
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			List<LearningEnvironment> les = dbmanager.listLEObjects();
			if (les!=null)
			{
				for(LearningEnvironment le : les){
					//Check that the user is the owner of the LE
					if (le.getUserid().equals(userid))
					{
						LearningEnvironmentResource leItem = new LearningEnvironmentResource();
						leItem.setLearningEnvironment(le.getId(), getReference().toString());
						feed.getEntries().add(leItem.getLearningEnvironmentEntry());
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Internal error while trying to retrieve learning environments from DB", e);
		}
		
		
		Representation answer = feed.getRepresentation();
		
		try {
			logger.info("** GET LEARNING ENVIRONMENTS answer: \n" + (answer != null ? answer.getText() : "NULL"));
		} catch (IOException io) {
			throw new RuntimeException("Extremely weird failure while trying to log", io);
		}
		return answer;
		
	}
    
	/**
	 * GET ToolImplementationsListResource
	 * 
	 * Returns a list with all registered tool implementations.
	 * 
	 * Method ::18:: at Informe_041b_01_02_09.pdf
	 * 
	 * @return	'Atomized' list of learning Environment known by GLUEletManager
	 */
    @Get("json")
    public Representation getJsonLearningEnvironmentsList() {
    	
   		logger.info("** GET JSON learning Environments received");
   		Representation answer = null;
   		
   		String userid = this.getRequest().getChallengeResponse().getIdentifier();
   		if(userid==null) throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to get the learning environment caller user");
		
		ArrayList<LearningEnvironment> lesUser = new ArrayList<LearningEnvironment>();
		JpaManager dbmanager = JpaManager.getInstance();
		try {
			List<LearningEnvironment> les = dbmanager.listLEObjects();
			if (les!=null)
			{
				for(LearningEnvironment le : les){
					//Check that the user is the owner of the LE
					if (le.getUserid().equals(userid))
					{
						//We don't want to include for now the password with the LE information
						le.setCredsecret(null);
						
						String uri = getReference().toString();
						uri = doGluepsUriSubstitution(uri +"/" + le.getId());
						le.setId(uri);
						lesUser.add(le);
						//lesUser.add(URLifyLearningEnvironment(le, getReference().getParentRef().getIdentifier()));
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Internal error while trying to retrieve learning environments from DB", e);
		}
		
		// convert java object to JSON format,
		// and returned as JSON formatted string
		Gson gson = new Gson();
		String json = gson.toJson(lesUser);
   		 		
   		if (json != null){
   			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		
   		logger.info("** GET JSON LEARNING ENVIRONMENTS answer: \n" + (answer != null ? json : "NULL"));
   		return answer;  		
	}
   

    
	@Post
	public Representation postLearningEnvironment(Representation entity) {
		
		logger.info("** POST learning Environments received");
		
		Form form = new Form(entity);
		String leName 	= form.getFirstValue(NEW_LE_NAME_FIELD);
		String leInstallation 	= form.getFirstValue(NEW_LE_INSTALLATION_FIELD);
		String leUser 	= form.getFirstValue(NEW_LE_USER_FIELD);
		String lePassword 	= form.getFirstValue(NEW_LE_PASSWORD_FIELD);
		Representation answer = null;
		
		/// Checks parameter values
		String missingParameters = "";
		if (leName == null || leName.length() == 0)
			missingParameters += NEW_LE_NAME_FIELD + ", ";
		if (leInstallation == null || leInstallation.length() == 0) 
			missingParameters += NEW_LE_INSTALLATION_FIELD + ", ";
		if (missingParameters.length() > 0) {
			missingParameters = missingParameters.substring(0, missingParameters.length() - 2);			
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missingParameters);
		}
		
		String userid = this.getRequest().getChallengeResponse().getIdentifier();
		if(userid==null) throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to get the learning environment caller user");
   		JpaManager dbmanager = JpaManager.getInstance();
   		LearningEnvironmentInstallation leInst = dbmanager.findLEInstObjectById(leInstallation);
   		if (leInst==null) throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to get the LE Installation from the DB");
   		
		String leType = leInst.getType();
		if (leUser == null || leUser.length() == 0) 
			leUser = "";
		if (lePassword == null || lePassword.length() == 0) 
			lePassword = "";
		
		URL accessLocation = leInst.getAccessLocation();
		
		LearningEnvironment le;
		try {
			//We should receive the parameters with the values for showAR and show VG. By now, we set both values to false
			le = new LearningEnvironment(null, leName, leType, accessLocation, userid, leUser, lePassword, trimId(leInstallation),false,false);
			dbmanager.insertLearningEnvironment(le);
		}
		catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the generated deploy into the DB", e);
		}
		return answer;
	}
	
    /**
     * Update the database fields of every LearningEnvironmentEntity with the information contained in its Deploy xmlfile
     * @throws UnsupportedEncodingException 
     */
	/*@Put()
    public Representation updateLearningEnvironmentEntities(Representation entity){
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		if (app.getLetPutUpdateTables()){
			Representation answer = null;
	    	logger.info("** Starting updating glueps_learning_environments table");
	    	JpaManager dbmanager = JpaManager.getInstance();
	    	List<LearningEnvironmentEntity> leEntities = dbmanager.listLEs();
	    	if(leEntities!=null){
	    		for(LearningEnvironmentEntity lee : leEntities){
	    			LearningEnvironmentInstallation lei = dbmanager.findLEInstObjectByAccessLocation(lee.getAccessLocation().toString());
	    			if (lei!=null){
		    			lee.setInstallation(Long.valueOf(lei.getId()));
		    			//Make the changes permanent
		    			dbmanager.insertLearningEnvironment(lee);
	    			}	
	    		}
	    	}
	    	logger.info("** Finished updating glueps_learning_environments table");
	    	return answer;
	    }else{
	    	throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "updating glueps_learning_environments table is not allowed");
	    }
    }*/
    

}
