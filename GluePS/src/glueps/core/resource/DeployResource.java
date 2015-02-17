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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glueps.adaptors.vle.IDynamicVLEDeployer;
import glueps.adaptors.vle.IStaticVLEDeployer;
import glueps.adaptors.vle.IVLEAdaptor;
import glueps.adaptors.vle.VLEAdaptorFactory;
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.adaptors.vle.moodledyn.MoodleDynAdaptor;
import glueps.adaptors.vle.moodledyn.MoodleDynAdaptor21v;
import glueps.core.controllers.exist.Exist;
import glueps.core.gluepsManager.GLUEPSManagerApplication;
import glueps.core.gluepsManager.GLUEPSManagerServerMain;
import glueps.core.model.Course;
import glueps.core.model.Deploy;
import glueps.core.model.Design;
import glueps.core.model.Group;
import glueps.core.model.LearningEnvironment;
import glueps.core.model.Participant;
import glueps.core.model.Resource;
import glueps.core.model.ToolInstance;
import glueps.core.persistence.JpaManager;
import glueps.core.persistence.entities.DeployEntity;
import glueps.core.persistence.entities.DeployVersionEntity;
import glueps.core.persistence.entities.SectokenEntity;
import glueps.core.service.inprocess.InProcessInfo;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.data.CacheDirective;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.CharacterSet;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;





/**
 * Resource Deploy.
 * 
 * Information about a registered Deploy, available to create Gluelets. 
 * 
 * @author	 	Juan Carlos A. <jcalvarezgsic@gsic.uva.es>
 * @package 	glueps.core.resources
 */

public class DeployResource extends GLUEPSResource {
	
/// attributes ///
	
	/** 
	 * Logger 
	 * 
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
 
	
	/** Local id., without extension */
    protected String deployId;
    
    protected String feedReference;
    
    protected Deploy deploy;
    
	private static final String NEW_DEPLOY_SEC_TOKEN = "sectoken";
	private static final String NEW_DEPLOY_TITLE_FIELD = "NewDeployTitleName";
    
    
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "toolId" attribute value taken from the URI template /deploy/{deployId}
   		this.deployId = trimId((String) getRequest().getAttributes().get("deployId"));
   		
   		logger.info("Initializing deploy resource "+this.deployId);
   		
   		JpaManager dbmanager = JpaManager.getInstance();
   		deploy = dbmanager.findDeployObjectById(this.deployId);

   		// does it exist?
		setExisting(this.deploy != null);	// setting 'false' implies that REST methods won't start; server will respond with 404
    }
    
	/**
	 * Clone a deploy creating a new one but without importing the created tool instances
	 */
	@Post()
	public Representation postDeploy(Representation entity) {
		Representation answer = null;
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();  	
	    //Default authorization: if the caller is not the owner, we do not allow actions on the resource
	    String login = this.getRequest().getChallengeResponse().getIdentifier();
	    if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");	
	    Form form = new Form(entity);
	    String sectoken = form.getFirstValue(NEW_DEPLOY_SEC_TOKEN);
	    String title = form.getFirstValue(NEW_DEPLOY_TITLE_FIELD);
		if (title==null || title.equals("")){
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, NEW_DEPLOY_TITLE_FIELD + " parameter with the deploy name is missing");
		}
		
		//Set the upload and schemas paths
		String UPLOAD_DIRECTORY = app.getAppPath() + "/uploaded/";

		// We create the newly uploaded deployment folder where
		// the deployment will be stored
		String newDeployId, newDeployUploadDir;
		do {
			newDeployId = deploy.getDesign().getId() + Deploy.ID_SEPARATOR + (new Random()).hashCode();
			newDeployUploadDir = UPLOAD_DIRECTORY + newDeployId + File.separator;
		} while ((new File(newDeployUploadDir)).exists());

		// We store the deployment file
		(new File(newDeployUploadDir)).mkdirs();
		String xmlDeploy = generateXML(deploy, glueps.core.model.Deploy.class);
		File fileDeploy = new File(newDeployUploadDir, newDeployId + "_original.xml");
		FileWriter fr = null;
		BufferedWriter wr = null;
		try {
			fr = new FileWriter(fileDeploy);
			wr = new BufferedWriter(fr);
			wr.write(xmlDeploy);
			wr.close();
			fr.close();
		} catch (IOException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to save the original deploy", e);
		}
        JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(glueps.core.model.Deploy.class);
	        Unmarshaller u = jc.createUnmarshaller();
	        Deploy newDeploy = (Deploy)u.unmarshal(new StringReader(xmlDeploy));
	        newDeploy.setId(newDeployId);
	        newDeploy.setName(title);
	        
			if (newDeploy.getParticipants()!=null) {
				for (Participant p: newDeploy.getParticipants()){
					p.setDeployId(newDeployId);
				}
			}
			if (newDeploy.getGroups()!=null){
				for (Group g: newDeploy.getGroups()){
					g.setDeployId(newDeployId);
				}
			}
			
	    	if (newDeploy.getToolInstances()!=null){
		        for (ToolInstance ti : newDeploy.getToolInstances())
		        {
		        	ti.setLocation(null);
		        }
	        }
	        newDeploy.setStaticDeployURL(null);
	        newDeploy.setLiveDeployURL(null);
	        
	        newDeploy.setInProcess(false);
	        //Set the current timestamp
	        newDeploy.setTimestamp(new Date());
	        
			//We store the deurlified deploy in DB
			JpaManager dbmanager = JpaManager.getInstance();
			try {
				dbmanager.insertDeploy(newDeploy);
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the generated deploy into the DB", e);
			}
			
			//Save the sectoken in the database
			if (sectoken!=null){
				SectokenEntity sectEnt = new SectokenEntity(deployId, sectoken);
				dbmanager.insertSectoken(sectEnt);
			}
		    
	   		//We create a new deploy where all the adequate ids are URLs
	   		Deploy urlNewDeploy;
			try {
				urlNewDeploy = URLifyDeploy(newDeploy,doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while processing the generated deploy to return", e);
			}
			Gson gson = new Gson();
			String json = gson.toJson(urlNewDeploy);
			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
	   		logger.info("** CLONE DEPLOY JSON answer: \n" + (answer != null ? json : "NULL"));
			
		} catch (JAXBException e) {
			throw new ResourceException(Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY, "Failed to parse the original deploy"); 
		}		
   		return answer;  		
	}   

//    @Get
    @Get("xml|html")
    public Representation getDeploy()  {
	    if (getReference().getIdentifier().contains("/ldshakedata")){
	    		return getLdshakedata();
	    }
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
    	if (app.getLdShakeMode()==false){
	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
			String login = this.getRequest().getChallengeResponse().getIdentifier();
			if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
    	}
    	else{
	    		if (this.getRequest().getChallengeResponse()!=null){
	    	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
	    			String login = this.getRequest().getChallengeResponse().getIdentifier();
	    			if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
	    		}
	    		else{
	    			//Check if a sectoken has been provided
	    			Reference ref = getReference();
	    			Form query = ref.getQueryAsForm();
	    			String sectoken = query.getFirstValue("sectoken");
	    			if (sectoken!=null){
	    				JpaManager dbmanager = JpaManager.getInstance();
	    				SectokenEntity ste = dbmanager.findSectokenById(deployId);
	    				if (ste==null || !ste.getSectoken().equals(sectoken)){
	    					throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
	    				}
	    			}else{
	    				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
	    			}
	    		}
    	}
    	
	    if (getReference().getIdentifier().contains("/metadata")){
   	    		return getMetadata();
   	    }
	    
	    if(deploy.isInProcess()) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "The deployment is being created. Please try getting it again later");
    	
    	//If they ask about the zip, we generate it (if not done already) and redirect
    	//if(getReference().getIdentifier().endsWith("/static")) return this.getStaticDeploy();
    	//else if (getReference().getIdentifier().endsWith("/live")) return this.getLiveDeploy();
    	if(getReference().getIdentifier().contains("/static")) return this.getStaticDeploy();
    	else if (getReference().getIdentifier().contains("/live")) return this.getLiveDeploy();
    	else{//They are asking for the default representation (xml)
      		logger.info("** GET DEPLOY received");

       		Representation answer = null;
       		
       		//We create a new deploy where all the adequate ids are URLs
       		Deploy urlDeploy;
			try {
				urlDeploy = URLifyDeploy(deploy,doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
			} catch (Exception e) {
				throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to format the requested deploy", e);
			}
       		
       		String xmlfile = generateXML(urlDeploy, glueps.core.model.Deploy.class);
        	
    		answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
    		answer.setCharacterSet(CharacterSet.UTF_8);
       		       		
       		logger.info("** GET DEPLOY answer: \n" + (answer != null ? xmlfile : "NULL"));
       		return answer;  		
    
    	}
    }



	@Get("json")
    public Representation getJsonDeploy()  {
	    if (getReference().getIdentifier().contains("/ldshakedata")){
    		return getLdshakedata();
	    }
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
    	if (app.getLdShakeMode()==false){
	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
			String login = this.getRequest().getChallengeResponse().getIdentifier();
			if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
    	}
    	else{
			if (this.getRequest().getChallengeResponse()!=null){
		    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
				String login = this.getRequest().getChallengeResponse().getIdentifier();
				if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
			}
			else{
				//Check the sectoken
				Reference ref = getReference();
				Form query = ref.getQueryAsForm();
				String sectoken = query.getFirstValue("sectoken");
				if (sectoken!=null){
					JpaManager dbmanager = JpaManager.getInstance();
					SectokenEntity ste = dbmanager.findSectokenById(deployId);
					if (ste==null || !ste.getSectoken().equals(sectoken)){
						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
					}
				}
				else{
					throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
				}
			}
		}
    	
	    if (getReference().getIdentifier().contains("/metadata")){
	    		return getMetadata();
	    }
    	
       	logger.info("** GET JSON DEPLOY received");
       	
       	if(deploy.isInProcess()) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "The deployment is being created. Please try getting it again later");

       	Representation answer = null;

       	//We create a new deploy where all the adequate ids are URLs
       	Deploy urlifiedDeploy;
		try {
			urlifiedDeploy = URLifyDeploy(deploy,doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to format the requested deploy", e);
		}
       		
        Gson gson = new Gson();
        	 
    	// convert java object to JSON format,
    	// and returned as JSON formatted string
    	String json = gson.toJson(urlifiedDeploy);
       		
       		
       	if (json != null){
    		answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
    		answer.setCharacterSet(CharacterSet.UTF_8);
       	}
       		
       	logger.info("** GET JSON DEPLOY answer: \n" + (answer != null ? json : "NULL"));
       	return answer;
	}
	
	private Representation getMetadata(){
		logger.info("** GET METADATA DEPLOY received");
		
		Representation answer = null;
		JpaManager dbmanager = JpaManager.getInstance();
		DeployVersionEntity version = dbmanager.findLastValidDeployVersion(deployId);
		HashMap<String, String> prop = getPropertiesJsonMetadata(version);
		Gson gson = new Gson();	
		String json = gson.toJson(prop);		
   		if (json != null){
			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		logger.info("** GET METADATA DEPLOY answer: \n" + (answer != null ? json : "NULL"));
		return answer;
	}
	
    /**
     * Get the metadata information of the deploy version
     * @param depVe The deploy version whose metadata information is going to be gotten
     * @return The metadata information as a HashMap<String, String> of pairs property,value
     */
	private HashMap<String, String> getPropertiesJsonMetadata(DeployVersionEntity depVe) {
		HashMap<String, String> prop = new HashMap<String, String>();
		prop.put("deployid", depVe.getDeployid());
		prop.put("version", String.valueOf(depVe.getVersion()));
		prop.put("valid", String.valueOf(depVe.getValid()));
		prop.put("undoalert", String.valueOf(depVe.getUndoalert()));
		prop.put("next", String.valueOf(depVe.getNext()));
		return prop;
	}
	
	private Representation getLdshakedata(){
		logger.info("** GET LDSHAKEDATA DEPLOY received");
		
		Representation answer = null;
		JpaManager dbmanager = JpaManager.getInstance();
		Deploy deploy = dbmanager.findDeployObjectById(deployId);
		String ldshakeFrameOrigin = (deploy.getLdshakeFrameOrigin()!=null) ? deploy.getLdshakeFrameOrigin() : "";
		HashMap<String,String> hm = new HashMap<String,String>();
		hm.put("ldshakeFrameOrigin", ldshakeFrameOrigin);
		Gson gson = new Gson();
		String json = gson.toJson(hm);	   		
   		if (json != null){
			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		logger.info("** GET LDSHAKEDATA DEPLOY answer: \n" + (answer != null ? json : "NULL"));
		return answer;
	}
	
	private Representation undoDeploy(){
		Deploy oldDeploy = deploy;
   		JpaManager dbmanager = JpaManager.getInstance();
   		//Undo operation in the database
   		dbmanager.undoDeployVersion(deployId);
   		//get the new deploy version
   		deploy = dbmanager.findDeployObjectById(deployId);
   		DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
   		DeployVersionEntity oldDeployVersion = dbmanager.findDeployVersionById(deployId, newDeployVersion.getNext());
		ArrayList<ToolInstance> instances = oldDeploy.getToolInstances();
		if(instances!=null){
			//Delete the tool instances that were created in the last version of the deploy
			//If we redo, we will need to create them again
			for(Iterator<ToolInstance> it = instances.iterator();it.hasNext();){
				ToolInstance instanceOld = it.next();
				if(instanceOld.getLocation()!=null){
					ToolInstance instanceNew = deploy.getToolInstanceById(instanceOld.getId());
					//Check if the tool instance was created in the last version (the location value is null now)
					if (instanceNew!=null && instanceNew.getLocation()== null){
						//Delete the tool instance
						Status st = deleteGMResource(instanceOld.getLocation().toString());
						//getResponse().setStatus(st);						
			   			//if (st.isSuccess()) {
			   				instanceOld.setLocation(null);
			   			//}
					}
				}				
			}
		}
		instances = deploy.getToolInstances();
		if (instances!=null){
			//Delete in the new current version the tool instances that were deleted in the last version of the deploy
			//The undo operation does not recover the tool instances deleted
			for(Iterator<ToolInstance> it = instances.iterator();it.hasNext();){
				ToolInstance instanceNew = it.next();
				if(instanceNew.getLocation()!=null){
					ToolInstance instanceOld = oldDeploy.getToolInstanceById(instanceNew.getId());
					//Check if the tool instance was deleted in the last version (the location value is null now)
					if (instanceOld!=null && instanceOld.getLocation()== null){
						//Delete the tool instance
						Status st = deleteGMResource(instanceNew.getLocation().toString());
						//getResponse().setStatus(st);						
			   			//if (st.isSuccess()) {
			   				instanceNew.setLocation(null);
			   			//}
					}
				}				
			}
		}		
		//Update the old deploy version in the database	
		String updatedXmlfile = generateXML(oldDeploy, Deploy.class);
		try {
			oldDeployVersion.setXmlfile(updatedXmlfile.getBytes("UTF-8"));
			oldDeployVersion.setUndoalert(false);
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to encode the xml file", e);
		}
		dbmanager.updateDeployVersion(oldDeployVersion);
		
		//Update the new deploy version in the database
		updatedXmlfile = generateXML(deploy, Deploy.class);
		try {
			newDeployVersion.setXmlfile(updatedXmlfile.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to encode the xml file", e);
		}
		//Call to the update function to update the xml field properly
		dbmanager.updateDeployVersion(newDeployVersion);
  		
   		
   		logger.info("** PUT UNDO DEPLOY received");

   		Representation answer = null;

   		//We create a new deploy where all the adequate ids are URLs
   		Deploy urlifiedDeploy;
		try {
			urlifiedDeploy = URLifyDeploy(deploy,doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getParentRef().getIdentifier()));
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to format the requested deploy", e);
		}
   		
   		String xmlfile = generateXML(urlifiedDeploy, glueps.core.model.Deploy.class);
    	
		answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
		answer.setCharacterSet(CharacterSet.UTF_8);
   		       		
   		logger.info("** PUT UNDO DEPLOY answer: \n" + (answer != null ? xmlfile : "NULL"));
   		return answer;  
	}
	
	private Representation undoJsonDeploy(){
		Deploy oldDeploy = deploy;
   		JpaManager dbmanager = JpaManager.getInstance();
   		//Undo operation in the database
   		dbmanager.undoDeployVersion(deployId);
   		//get the new deploy version
   		deploy = dbmanager.findDeployObjectById(deployId);
   		DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
   		DeployVersionEntity oldDeployVersion = dbmanager.findDeployVersionById(deployId, newDeployVersion.getNext());
		ArrayList<ToolInstance> instances = oldDeploy.getToolInstances();
		if(instances!=null){
			//Delete the tool instances that were created in the last version of the deploy
			//If we redo, we will need to create them again
			for(Iterator<ToolInstance> it = instances.iterator();it.hasNext();){
				ToolInstance instanceOld = it.next();
				if(instanceOld.getLocation()!=null){
					ToolInstance instanceNew = deploy.getToolInstanceById(instanceOld.getId());
					//Check if the tool instance was created in the last version (the location value is null now)
					if (instanceNew!=null && instanceNew.getLocation()== null){
						//Delete the tool instance
						Status st = deleteGMResource(instanceOld.getLocation().toString());
						//getResponse().setStatus(st);						
			   			//if (st.isSuccess()) {
			   				instanceOld.setLocation(null);
			   			//}
					}
				}				
			}
		}
		instances = deploy.getToolInstances();
		if (instances!=null){
			//Delete in the new current version the tool instances that were deleted in the last version of the deploy
			//The undo operation does not recover the tool instances deleted
			for(Iterator<ToolInstance> it = instances.iterator();it.hasNext();){
				ToolInstance instanceNew = it.next();
				if(instanceNew.getLocation()!=null){
					ToolInstance instanceOld = oldDeploy.getToolInstanceById(instanceNew.getId());
					//Check if the tool instance was deleted in the last version (the location value is null now)
					if (instanceOld!=null && instanceOld.getLocation()== null){
						//Delete the tool instance
						Status st = deleteGMResource(instanceNew.getLocation().toString());
						//getResponse().setStatus(st);						
			   			//if (st.isSuccess()) {
			   				instanceNew.setLocation(null);
			   			//}
					}
				}				
			}
		}
		//Update the old deploy version in the database
		String updatedXmlfile = generateXML(oldDeploy, Deploy.class);
		try {
			oldDeployVersion.setXmlfile(updatedXmlfile.getBytes("UTF-8"));
			oldDeployVersion.setUndoalert(false);
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to encode the xml file", e);
		}
		//Call to the update function to update the xml field properly
		dbmanager.updateDeployVersion(oldDeployVersion);
		
		//Update the new deploy version in the database
		updatedXmlfile = generateXML(deploy, Deploy.class);
		try {
			newDeployVersion.setXmlfile(updatedXmlfile.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to encode the xml file", e);
		}
		//Call to the update function to update the xml field properly
		dbmanager.updateDeployVersion(newDeployVersion);
   		
   		logger.info("** PUT JSON UNDO DEPLOY received");

   		Representation answer = null;

   		//We create a new deploy where all the adequate ids are URLs
   		Deploy urlifiedDeploy;
		try {
			urlifiedDeploy = URLifyDeploy(deploy,doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getParentRef().getIdentifier()));
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to format the requested deploy", e);
		}
   		
    	Gson gson = new Gson();
    	 
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(urlifiedDeploy);
   		   		
   		if (json != null){
			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		logger.info("** PUT JSON UNDO DEPLOY answer: \n" + (answer != null ? json : "NULL"));
   		return answer;  
	}
	
	private Representation redoDeploy(){
		Deploy oldDeploy = deploy;
   		JpaManager dbmanager = JpaManager.getInstance();
   		//Redo operation in the database
   		dbmanager.redoDeployVersion(deployId);
   		//get the new deploy version
   		deploy = dbmanager.findDeployObjectById(deployId);
   		DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
		ArrayList<ToolInstance> instances = deploy.getToolInstances();
		if(instances!=null){
			for(Iterator<ToolInstance> it = instances.iterator();it.hasNext();){
				ToolInstance instanceNew = it.next();
				if(instanceNew.getLocation()!=null){
					ToolInstance instanceOld = oldDeploy.getToolInstanceById(instanceNew.getId());
					if (instanceOld!=null && instanceOld.getLocation()== null){
						//Delete the tool instance. It is supposed to have been deleted in an undo operation
						/*Status st = deleteGMResource(instanceNew.getLocation().toString());
						getResponse().setStatus(st);						
			   			if (st.isSuccess()) {
			   				instanceNew.setLocation(null);
			   			}*/
						instanceNew.setLocation(null);
					}
				}				
			}
		}
		String updatedXmlfile = generateXML(deploy, Deploy.class);
		try {
			newDeployVersion.setXmlfile(updatedXmlfile.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to encode the xml file", e);
		}
		//Call to the update function to update the xml field properly
		dbmanager.updateDeployVersion(newDeployVersion);
   		
   		logger.info("** PUT REDO DEPLOY received");

   		Representation answer = null;

   		//We create a new deploy where all the adequate ids are URLs
   		Deploy urlifiedDeploy;
		try {
			urlifiedDeploy = URLifyDeploy(deploy,doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getParentRef().getIdentifier()));
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to format the requested deploy", e);
		}
   		
		
   		String xmlfile = generateXML(urlifiedDeploy, glueps.core.model.Deploy.class);
    	
		answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
		answer.setCharacterSet(CharacterSet.UTF_8);
   		       		
   		logger.info("** PUT REDO DEPLOY answer: \n" + (answer != null ? xmlfile : "NULL"));
   		return answer;
	}
	
	private Representation redoJsonDeploy(){
		Deploy oldDeploy = deploy;
   		JpaManager dbmanager = JpaManager.getInstance();
   		//Redo operation in the database
   		dbmanager.redoDeployVersion(deployId);
   		//get the new deploy version
   		deploy = dbmanager.findDeployObjectById(deployId);
   		DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
		ArrayList<ToolInstance> instances = deploy.getToolInstances();
		if(instances!=null){
			for(Iterator<ToolInstance> it = instances.iterator();it.hasNext();){
				ToolInstance instanceNew = it.next();
				if(instanceNew.getLocation()!=null){
					ToolInstance instanceOld = oldDeploy.getToolInstanceById(instanceNew.getId());
					if (instanceOld!=null && instanceOld.getLocation()== null){
						//Delete the tool instance. It is supposed to have been deleted in an undo operation
						/*Status st = deleteGMResource(instanceNew.getLocation().toString());
						getResponse().setStatus(st);						
			   			if (st.isSuccess()) {
			   				instanceNew.setLocation(null);
			   			}*/
						instanceNew.setLocation(null);
					}
				}				
			}
		}
		String updatedXmlfile = generateXML(deploy, Deploy.class);
		try {
			newDeployVersion.setXmlfile(updatedXmlfile.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to encode the xml file", e);
		}
		//Call to the update function to update the xml field properly
		dbmanager.updateDeployVersion(newDeployVersion);
   		
   		logger.info("** PUT JSON REDO DEPLOY received");

   		Representation answer = null;

   		//We create a new deploy where all the adequate ids are URLs
   		Deploy urlifiedDeploy;
		try {
			urlifiedDeploy = URLifyDeploy(deploy,doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getParentRef().getIdentifier()));
		} catch (Exception e) {
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to format the requested deploy", e);
		}
   		
    	Gson gson = new Gson();
    	 
		// convert java object to JSON format,
		// and returned as JSON formatted string
		String json = gson.toJson(urlifiedDeploy);
   		
   		
   		if (json != null){
			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
   		
   		logger.info("** GET JSON REDO DEPLOY answer: \n" + (answer != null ? json : "NULL"));
   		return answer;  
	}
		
		
    @Delete
    public Representation deleteDeploy() {
    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
    	if (doAuthentication){
	    	if (app.getLdShakeMode()==false){    	
	    	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
	    			String login = this.getRequest().getChallengeResponse().getIdentifier();
	    			if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
	    	}
	    	else{
				if (this.getRequest().getChallengeResponse()!=null){		    	
				    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
						String login = this.getRequest().getChallengeResponse().getIdentifier();
						if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
				}
				else{
					//Check the sectoken
					Reference ref = getReference();
					Form query = ref.getQueryAsForm();
					String sectoken = query.getFirstValue("sectoken");
					if (sectoken!=null){
						JpaManager dbmanager = JpaManager.getInstance();
						SectokenEntity ste = dbmanager.findSectokenById(deployId);
						if (ste==null || !ste.getSectoken().equals(sectoken)){
							throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
						}
					}
					else{
						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
					}
				}
			}
    	}
		//WE check that the deploy is not in process, and that the request does not try to override the (faulty) in process mechanism
    	String override = this.getQuery()!=null ? this.getQuery().getValues("override") : null;
    	boolean deployInProcess = false;
    	if (deploy.getLearningEnvironment()!=null){
    		deployInProcess = GLUEPSManagerServerMain.ips.askDeployInProcess(new InProcessInfo(deploy.getId(),deploy.getLearningEnvironment().getAccessLocation().toString(),deploy.getCourse().getId()));
    	}
		if(deployInProcess && override==null) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "The deployment is in process. Please try modifying it again later");

    	logger.info("** DELETE DEPLOY received");
  	
		//GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";

		
		//We delete all (external) instances
		// For now, we do it by calling to the toolInstancesResource
		ArrayList<ToolInstance> instances = deploy.getToolInstances();
		if(instances!=null){
			for(Iterator<ToolInstance> it = instances.iterator();it.hasNext();){
				ToolInstance instance = it.next();
				Resource res = deploy.getDesign().getResourceById(instance.getResourceId());
				if(res.getToolKind().equals(Resource.TOOL_KIND_EXTERNAL) && instance.getLocation()!=null && !instance.isRedirection(deploy)){//We just delete the non-redirected external instances
					try {
				   		logger.info("Trying to delete toolInstance "+instance.getId()+".");
				   		deleteLocalToolInstance(trimId(instance.getId()));
					} catch (Exception e) {
				   		logger.info("Exception while trying to delete toolInstance "+instance.getId()+". We continue");
					}
					
				}
				
			}
		}
		
		//if it is a dynamic deploy, we undeploy it
   		VLEAdaptorFactory fact = new VLEAdaptorFactory(app);
   		IVLEAdaptor adaptor = fact.getVLEAdaptor(deploy.getLearningEnvironment());
   		//If the adaptor does not support 
   		if(adaptor instanceof IDynamicVLEDeployer){
   			IDynamicVLEDeployer dynAdaptor = (IDynamicVLEDeployer) adaptor;
			
   			//Supposedly, this method already updates the live deploy URL to the right value
   			deploy = dynAdaptor.undeploy(deploy.getLearningEnvironment().getAccessLocation().toString(), deploy);
   			
   			//TODO: what happens if we could not delete all pages? orphan pages may remain...
   			//if(deploy.getLiveDeployURL()!=null) System.err.println("There was some error when undeploying the wiki. Some orphan pages may remain. Please delete them ")
   		}
   		
		
		//We delete the uploaded files
		File deployDir = new File(UPLOAD_DIRECTORY+this.deployId);
		if(!deployDir.exists() || !deployDir.isDirectory()){
	   		logger.info("The deploy directory does not exist! we do nothing...");			
		}else{
			try {
				FileUtils.deleteDirectory(deployDir);
			} catch (IOException e) {
		   		logger.info("Error while deleting the directory. We continue...");			
				e.printStackTrace();
			}
		}
		
		JpaManager dbmanager = JpaManager.getInstance();
		//delete the deploy
		dbmanager.deleteDeploy(deployId);
		//delete all the versions of this deploy
		dbmanager.deleteDeployVersions(deployId);
		
		//Check if we have to delete the design as well
		if (doAuthentication){
			Reference ref = getReference();
			Form query = ref.getQueryAsForm();
			String deleteParent = query.getFirstValue("deleteParent");
			if (deleteParent!=null && Boolean.parseBoolean(deleteParent)==true){
				//Delete the parent design, unless there are other sibling deploys hanging from it
				String designid = deploy.getDesign().getId();
				try {
					List<Deploy> deployObjects = dbmanager.listDeployObjectsByDesignId(designid);
					if (deployObjects!=null && deployObjects.size()==0){
						DesignResource dr = new DesignResource();
						dr.setDesign(designid, null);
						dr.deleteDesign();
					}
				} catch (UnsupportedEncodingException e) {
					logger.info("Exception while trying to delete design "+ designid +". We continue");
				}
			}
		}
		
		if (app.getLdShakeMode()==true){
			dbmanager.deleteSectoken(deployId);
		}
		
   		logger.info("** DELETE DEPLOY completed");
   		return null;
	}    
    

    
    private void deleteLocalToolInstance(String id) {
		ToolInstanceResource toolRes = new ToolInstanceResource();
		toolRes.setToolInstance(this.deployId, id);
		Representation rep = toolRes.deleteToolInstance();//if it goes ok, we do nothing (if not, it throws exception)
	}

	protected Status deleteGMResource(String targetURI) throws ResourceException {
		int response=500;
		try {
			response = doDeleteFromURL(targetURI);
			return new Status(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Status.SERVER_ERROR_INTERNAL;
		}
	}

    
//  private Status deleteLocalResource(String path) {
//		
//  	
//  	Status status = null;	
//		Representation response = null;
//		
//		Component component = new Component();
//		Client client = new Client(Protocol.RIAP);
//		// Timeout 0 means that timeout is infinite
//		client.setConnectTimeout(0);
//		
//		component.getClients().add(client);
//		try {
//			component.start();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		ClientResource localResource = new ClientResource("riap://host/GLUEPSManager"+path);
//		try {
//			localResource.delete();
//			response = localResource.getResponseEntity();
//			status = localResource.getStatus();
//			if (status.equals(Status.SUCCESS_OK) ||
//				status.equals(Status.SUCCESS_ACCEPTED) ||
//				status.equals(Status.SUCCESS_NO_CONTENT)) {
//					
//				return status;
//			}
//			
//		} catch (ResourceException r) {
//			r.printStackTrace();
//			ResourceException re = convertResourceException(r);
//
//			throw re;
//		} finally {
////			Representation entity = localResource.getResponseEntity();
////			if (entity != null)
////				entity.release();
////			localResource.release();
//
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(localResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aqu� es desde la ejecuci�n de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//
//		}
//		
//		// HTTP code is a not accepted 2xx
//		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from GLUEPS, HTTP status " + status);
//
//		
//	}
    
    private Status deleteRemoteResource(String url){
    	
    	try {
			int response = doDeleteFromURL(url);
			return new Status(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Status.SERVER_ERROR_INTERNAL;
		}
    	
    	
    	
    	
    }
    
    
//  private Status deleteRemoteResource(String url) {
//
//  	Status status = null;	
//		Representation response = null;
//	
////		Component component = new Component();
////		Client client = new Client(Protocol.HTTP);
////		// Timeout 0 means that timeout is infinite
////		client.setConnectTimeout(0);
////		component.getClients().add(client);
////		try {
////			component.start();
////		} catch (Exception e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
//
//		
//		//ClientResource remoteResource = new ClientResource(component.getContext().createChildContext(), url);
//		//New code equivalent mimicking GLUEletManager changes for connection stability
//		ClientResource remoteResource = new ClientResource(url);
//		
//		try {
//			remoteResource.delete();
//			response = remoteResource.getResponseEntity();
//			status = remoteResource.getStatus();
//			if (status.equals(Status.SUCCESS_OK) ||
//				status.equals(Status.SUCCESS_ACCEPTED) ||
//				status.equals(Status.SUCCESS_NO_CONTENT)) {
//					
//				return status;
//			}
//			
//		} catch (ResourceException r) {
//			ResourceException re = convertResourceException(r);
//			
//			throw re;
//		} finally {
////			Representation entity = remoteResource.getResponseEntity();
////			if (entity != null)
////				entity.release();
////			remoteResource.release();
//			
//			//New code equivalent mimicking GLUEletManager changes for connection stability
//			discardRepresentation(remoteResource.getResponseEntity());    /// **** LA CLAVE: "VOLVER A" EXTRAER LA ENTIDAD RESPUESTA DEL 
//																		///CLIENTRESOURCE; si se llega aquí es desde la ejecución de .get(), 
//																		///por lo que la variable 'response' NO SE HA ASIGNADO
//			
//		}
//		
//		// HTTP code is a not accepted 2xx
//		throw new ResourceException(Status.SERVER_ERROR_BAD_GATEWAY, "Unexpected answer from GLUEPS, HTTP status " + status);
//
//	}
    
    
    private Representation getLiveDeploy() {
		logger.info("** GET DEPLOY (live) received");

		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
		String tmpDir = UPLOAD_DIRECTORY+"/temp/zips/";
		String TEMPLATE_LOCATION = app.getAppPath()+"/templates/";

		//We check that the VLE adaptor supports live deployments. If not, we exit
		VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory(app);
		IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(deploy.getLearningEnvironment());
		if(!(adaptor instanceof IDynamicVLEDeployer)) throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, "The VLE adaptor does not support live deployments");

   		//We check that the Deploy is complete before getting the deploy
		if(!deploy.isComplete()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "There is missing data in the deploy. It cannot be deployed dynamically");

		//We check that the deploy is not in process, and that the request does not try to override the (faulty) in process mechanism
		String override = this.getQuery()!=null ? this.getQuery().getValues("override") : null;
		boolean deployInProcess = GLUEPSManagerServerMain.ips.checkDeployInProcess(new InProcessInfo(deploy.getId(), deploy.getLearningEnvironment().getAccessLocation().toString(), deploy.getCourse().getId()));
		if(deployInProcess && override==null) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "The deployment is in process. Please wait for a while and try again");
		
		//If it is complete, we check whether the live form of the deploy already exists
		if(deploy.getLiveDeployURL()!=null){		
			//Supposedly, the deployment has been created
			//TODO Make a GET to the new URL
		
			
			//If everything is OK, we redirect to the URL
			//getResponse().redirectTemporary(this.deploy.getLiveDeployURL().toString());
			//return new StringRepresentation((CharSequence) "You are being redirected to the actual location of the deployment");
			
			//Now, we return an HTML with the redirection, so that Dojo can get the reply (instead of the browser going there directly)
			String htmlContent = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" dir=\"ltr\"><head><meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n<title>" + this.deploy.getName() + "</title>\n</head>\n<body onload=\"window.location.replace('"+this.deploy.getLiveDeployURL().toString()+"')\">\n</body>\n</html>\n";
			StringRepresentation rep = new StringRepresentation((CharSequence) htmlContent, MediaType.TEXT_HTML);
			rep.setCharacterSet(CharacterSet.UTF_8);
			
			/*Some log info concerning to the live deploy */
	   		JpaManager dbmanager = JpaManager.getInstance();
	   		long version = dbmanager.getLastValidVersionDeploy(deploy.getId());
			if (this.getRequest().getChallengeResponse()!=null){
				String login = this.getRequest().getChallengeResponse().getIdentifier();
				logger.info(login + " is getting the live deploy " + this.deploy.getLiveDeployURL().toString() + " (" + deploy.getDesign().getId() + ";" + deploy.getId() + ";" + version + ";" + new Date().getTime() + ")");
			}else{
				logger.info("An unknown user" + " is getting the live deploy " + this.deploy.getLiveDeployURL().toString() + " (" + deploy.getDesign().getId() + ";" + deploy.getId() + ";" + version + ";" + new Date().getTime() + ")");
			}
			
			return rep;
		}else if (adaptor instanceof MoodleAdaptor){
			//we check the credentials of the user in Moodle
			MoodleAdaptor ma = (MoodleAdaptor)adaptor;
			LearningEnvironment vle = deploy.getLearningEnvironment();
			Boolean auth = ma.moodleAuth(vle.getAccessLocation().toString(), vle.getCreduser(), vle.getCredsecret());
			if (auth==false){
				throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The user is not allowed to deploy in the VLE. Please, check your credentials for the VLE");
			}else{
				throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The live deployment does not exist yet");
			}
		}
		else throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The live deployment does not exist yet");

	}


    private Representation getStaticDeploy() {
	
   		logger.info("** GET DEPLOY (static) received");

		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
		String tmpDir = UPLOAD_DIRECTORY+"/temp/zips/";
		String TEMPLATE_LOCATION = app.getAppPath()+"/templates/";

		//We check that the VLE adaptor supports static deployments. If not, we exit
		VLEAdaptorFactory adaptorFactory = new VLEAdaptorFactory(app);
		IVLEAdaptor adaptor = adaptorFactory.getVLEAdaptor(deploy.getLearningEnvironment());
		if(!(adaptor instanceof IStaticVLEDeployer)) throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, "The VLE adaptor does not support static deployments");

   		//We check that the Deploy is complete before getting the deploy
		if(!deploy.isComplete()) throw new ResourceException(Status.CLIENT_ERROR_FAILED_DEPENDENCY, "There is missing data in the deploy. Its static representation cannot be generated");

		//If it is complete, we check whether the static form of the deploy already exists
   		File file = new File(UPLOAD_DIRECTORY+this.deployId+File.separator+this.deployId+".zip");
		
		//If file was already generated, we just return it. If not, we generate it
		if(!file.exists()) throw new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "The static representation of the deployment has not been created yet");
   		
    	//We redirect the browser to the actual file (with a random number appended to avoid caching issues)
		getResponse().redirectTemporary(app.getAppExternalUri()+"uploaded/"+this.deployId+"/"+this.deployId+".zip?"+(new Date()).getTime());
		    		
		//We try to deactivate the cache for the response, just in case
		ArrayList<CacheDirective> caching = new ArrayList<CacheDirective>();
		caching.add(CacheDirective.noCache());
		getResponse().setCacheDirectives(caching);
	
		/*Some log info concerning to the download */
   		JpaManager dbmanager = JpaManager.getInstance();
   		long version = dbmanager.getLastValidVersionDeploy(deploy.getId());
		if (this.getRequest().getChallengeResponse()!=null){
	    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
			String login = this.getRequest().getChallengeResponse().getIdentifier();
			logger.info(login + " is downloading the static deploy file " + app.getAppExternalUri()+"uploaded/"+this.deployId+"/"+this.deployId+".zip" + " (" + deploy.getDesign().getId() + ";" + deploy.getId() + ";" + version + ";" + new Date().getTime() + ")");
		}else{
			logger.info("An unknown user" + " is downloading the static deploy file " + app.getAppExternalUri()+"uploaded/"+this.deployId+"/"+this.deployId+".zip" + " (" + deploy.getDesign().getId() + ";" + deploy.getId() + ";" + version + ";" + new Date().getTime() + ")");
		}
		
		return new StringRepresentation((CharSequence) "You are being redirected to the actual location of the file");


	}
    
    
	protected boolean supportLiveDeploy(Deploy newDeploy) {		
   		VLEAdaptorFactory fact = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
   		IVLEAdaptor adaptor = fact.getVLEAdaptor(newDeploy.getLearningEnvironment());
   		//If the adaptor does not support 
   		if(adaptor instanceof IDynamicVLEDeployer){
   			return true;
   		}else{
   			return false;
   		}
	}

	protected Deploy doLiveDeploy(Deploy newDeploy, boolean isRedeploy) {
		
		//We check that the deploy is complete
		if(newDeploy==null || !newDeploy.isComplete()) throw new ResourceException(Status.CLIENT_ERROR_FAILED_DEPENDENCY, "There is missing data in the deploy. It cannot be deployed dynamically");
		
		//generate the static deploy			
   		VLEAdaptorFactory fact = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
   		IVLEAdaptor adaptor = fact.getVLEAdaptor(newDeploy.getLearningEnvironment());
   		//If the adaptor does not support 
   		if(!(adaptor instanceof IDynamicVLEDeployer)) throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, "The VLE does not support live deployments");
   		
		//Since we support live deployments, we try to generate the live deployment (e.g. wiki pages)
		IDynamicVLEDeployer dynAdaptor = (IDynamicVLEDeployer) adaptor;		
		
   		if (!dynAdaptor.canBeDeployed(newDeploy.getLearningEnvironment().getAccessLocation().toString(), newDeploy)){
   			throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "You are not allowed to deploy into that course. Please, check your credentials and permissions in that VLE and course");
   		}
			
		//Supposedly, this method already updates the live deploy URL to the right value
		//newDeploy = dynAdaptor.deploy(newDeploy.getLearningEnvironment().getAccessLocation().toString(), newDeploy);
		newDeploy.setInProcess(true);
		//We call the background thread to do the live deploy
		Future<Deploy> process = GLUEPSManagerServerMain.pool.submit(new BackgroundLiveDeployer(newDeploy, dynAdaptor, isRedeploy));
		GLUEPSManagerServerMain.ips.startsProcess(new InProcessInfo(newDeploy.getId(), newDeploy.getLearningEnvironment().getAccessLocation().toString(), newDeploy.getCourse().getId()), process);


		logger.info("The live deploy has been sent to background deploying");
		
		return newDeploy;

	}


	private Deploy doStaticDeploy(Deploy newDeploy) {
		//We check that the deploy is complete
		if(newDeploy==null || !newDeploy.isComplete()) throw new ResourceException(Status.CLIENT_ERROR_FAILED_DEPENDENCY, "There is missing data in the deploy. Its static representation cannot be generated");
		
		//generate the static deploy			
   		VLEAdaptorFactory fact = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
   		IVLEAdaptor adaptor = fact.getVLEAdaptor(newDeploy.getLearningEnvironment());
   		//If the adaptor does not support static deployments, we quit...
   		//if(!(adaptor instanceof IStaticVLEDeployer)) throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, "The VLE does not support static deployments");
   		//TODO This is a temporary fix for the dynamic Moodle deployer - in reality, it is both static and dynamic, but the GUI currently does not support that. We make that adaptor dynamic-only
   		if(!(adaptor instanceof IStaticVLEDeployer) || (adaptor instanceof IDynamicVLEDeployer)) throw new ResourceException(Status.CLIENT_ERROR_METHOD_NOT_ALLOWED, "The VLE does not support static deployments");
   		
   		
   		File file;  		
		//Since we support static deployments, we try to generate the static deployment (e.g. Moodle zip)
		try {
			IStaticVLEDeployer staticAdaptor = (IStaticVLEDeployer) adaptor;
			
			file = staticAdaptor.generateStaticDeploy(newDeploy);
		} catch (JAXBException e) {
			e.printStackTrace();
			//Some error ocurred while generating the zipfile
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "JAXB error generating Moodle backup");  
		} catch (IOException e) {
			e.printStackTrace();
			//Some error ocurred while generating the zipfile
			throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown error generating Moodle backup");  
		}			

		logger.info("The moodle zip is now in "+file.toString());
		
		logger.info("Setting the static deploy URL to "+newDeploy.getId()+"/static");
		try {
			newDeploy.setStaticDeployURL(new URL(newDeploy.getId()+"/static"));
		} catch (MalformedURLException e) {
			//e.printStackTrace();
			//old method - does not play well with redirections in the ports
			//newDeploy.setStaticDeployURL(getReference().toUrl());
			try {
				newDeploy.setStaticDeployURL(new URL(doGluepsUriSubstitution(getReference().getBaseRef().toString())));
			} catch (MalformedURLException e1) {
				newDeploy.setStaticDeployURL(getReference().toUrl());
			}
		}
		
		return newDeploy;

	}
	
	private boolean supportStaticDeploy(Deploy newDeploy) {		
   		VLEAdaptorFactory fact = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
   		IVLEAdaptor adaptor = fact.getVLEAdaptor(newDeploy.getLearningEnvironment());
   		if(!(adaptor instanceof IStaticVLEDeployer) || (adaptor instanceof IDynamicVLEDeployer)){
   			return false;
   		}else{
   			return true;
   		}
	}


    @Put("json")
    public Representation putJsonDeploy(Representation entity)  {
    
       		try {
       			
       	    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
       	    	if (app.getLdShakeMode()==false){
       		    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
       				String login = this.getRequest().getChallengeResponse().getIdentifier();
       				if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
       			}else{
       				if (this.getRequest().getChallengeResponse()!=null){
       			    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
       					String login = this.getRequest().getChallengeResponse().getIdentifier();
       					if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
       				}
       				else{
       					//Check the sectoken
       					Reference ref = getReference();
       					Form query = ref.getQueryAsForm();
       					String sectoken = query.getFirstValue("sectoken");
       					if (sectoken!=null){
       						JpaManager dbmanager = JpaManager.getInstance();
       						SectokenEntity ste = dbmanager.findSectokenById(deployId);
       						if (ste==null || !ste.getSectoken().equals(sectoken)){
       							throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
       						}
       					}
       					else{
       						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
       					}
       				}
       			}
       	    	
       	    	if (getReference().getIdentifier().contains("/undo")){
       	    		return undoJsonDeploy();
       	    	}
       	    	else if (getReference().getIdentifier().contains("/redo")){
       	    		return redoJsonDeploy();
       	    	}

    			//WE check that the deploy is not in process, and that the request does not try to override the (faulty) in process mechanism
       	    	String override = this.getQuery()!=null ? this.getQuery().getValues("override") : null;
       	    	boolean deployInProcess = GLUEPSManagerServerMain.ips.askDeployInProcess(new InProcessInfo(deploy.getId(),deploy.getLearningEnvironment().getAccessLocation().toString(),deploy.getCourse().getId()));
    			if(deployInProcess && override==null) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "The deployment is in process. Please try modifying it again later");
    			//If it is a dynamic deploy We also check that there is not currently a put deploy request into the same VLE and course from a different deploy in GLUE!-PS
    			if (getReference().getIdentifier().contains("/live")){
    				boolean courseInProcess = GLUEPSManagerServerMain.ips.askCourseInProcess(deploy.getLearningEnvironment().getAccessLocation().toString(), deploy.getCourse().getId());
    				if(courseInProcess && override==null) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "There is deployment in process in the same VLE and course. Please try deploying it again later");
    			}

				logger.info("** PUT JSON DEPLOY received");

				Representation answer = null;

				Gson gson = new Gson();
				 
				//We should introduce the received json, converted to xml,  into the database
				//GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
				String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
				String uploadDir = UPLOAD_DIRECTORY+"/temp/";

				String deployData = null;
				
				File jsonFile;
				if(this.deployId!=null){
					//We get the xml that we have received, and write it to a temp file
					try{
						entity.setCharacterSet(CharacterSet.UTF_8);
						InputStream entrada= entity.getStream();
						//Write the xml to the uploads folder, with a random number to avoid concurrency problems
					   jsonFile=new File(uploadDir, this.deployId+".json"+"."+(new Date()).getTime());
					   OutputStream salida=new FileOutputStream(jsonFile);
					   byte[] buf =new byte[1024];
					   int len;
					   while((len=entrada.read(buf))>0){
					      salida.write(buf,0,len);
					   }
					   salida.close();
					   entrada.close();
					   System.out.println("Upload successful");
					}catch(IOException e){
					    System.out.println("There was an error : "+e.toString());
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown error uploading the file", e);  
					}

					BufferedReader br;
					try {
						//br = new BufferedReader(new FileReader(jsonFile));
						br = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));
					} catch (FileNotFoundException e1) {
						// We could not find the file we just created!! This is really wrong
						e1.printStackTrace();
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown error uploading the file", e1);  
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown error uploading the file", e);  
					}

					//convert the json string back to object
					Deploy updatedDeploy = gson.fromJson(br, Deploy.class);

					if(!updatedDeploy.isValid()) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "The received deploy data is wrongly formatted or incoherent");  
					
					if( (getReference().getIdentifier().contains("/static") && supportStaticDeploy(updatedDeploy)) || (getReference().getIdentifier().contains("/live") && supportLiveDeploy(updatedDeploy)) ){
						//Check if we can do the deploy
						checkDeployable(updatedDeploy);
						boolean success = completeToolCreation(updatedDeploy);
						//We store the deploy in DB to make sure that the last changes won't be lost
						//We convert the deploy into one where ids are NOT urls - to store in database
						JpaManager dbmanager = JpaManager.getInstance();
						try {

							Boolean created = instancesCreated(deURLifyDeploy(updatedDeploy));							
							if (created == true){								
								dbmanager.insertDeploy(deURLifyDeploy(updatedDeploy));
								DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
								newDeployVersion.setUndoalert(true);
								dbmanager.updateDeployVersion(newDeployVersion);
							}
							
						} catch (Exception e) {
							throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert deploy in the DB", e);
						}
						if (!success){
							throw new ResourceException(Status.CLIENT_ERROR_FAILED_DEPENDENCY, "There are still some external tool instances without creating");
						}
					}
					
					//do the deploy if the url was to do the deployment??
					if(getReference().getIdentifier().contains("/static")){
				    	//here, we check the users for all gluelets, and update their lists of users
						reconfigureGlueletUsers(updatedDeploy);				    	
						updatedDeploy = doStaticDeploy(updatedDeploy);//This function should work whether it is a deploy or redeploy - WARNING!, restoring this will probably delete work done by students if the activities had already started
				    }
				    else if (getReference().getIdentifier().endsWith("/live/newdeploy")){
				    	//here, we check the users for all gluelets, and update their lists of users
						reconfigureGlueletUsers(updatedDeploy);
				    	updatedDeploy = doLiveDeploy(updatedDeploy, false);
				    }
				    else if (getReference().getIdentifier().endsWith("/live/redeploy")){
				    	//here, we check the users for all gluelets, and update their lists of users
						reconfigureGlueletUsers(updatedDeploy);
						if (updatedDeploy.getLiveDeployURL()==null){
							throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "You can not redeploy a deploy that has not been deployed yet");
						}
				    	updatedDeploy = doLiveDeploy(updatedDeploy, true);
				    }
					else if (getReference().getIdentifier().contains("/live")){
				    	//here, we check the users for all gluelets, and update their lists of users
						reconfigureGlueletUsers(updatedDeploy);
						if(updatedDeploy.getLiveDeployURL()!=null) updatedDeploy = doLiveDeploy(updatedDeploy, true);//if the deploy had already been done, it is a redeploy!
						else updatedDeploy = doLiveDeploy(updatedDeploy, false);//it is the first live deploy
				    }

					//We store the deploy in DB
					//We convert the deploy into one where ids are NOT urls - to store in database
					JpaManager dbmanager = JpaManager.getInstance();
					try {
						
						Boolean created = instancesCreated(deURLifyDeploy(updatedDeploy));
						
						dbmanager.insertDeploy(deURLifyDeploy(updatedDeploy));
						
						if (created == true){
							DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
							newDeployVersion.setUndoalert(true);
							dbmanager.updateDeployVersion(newDeployVersion);
						}
						
						
						
					} catch (Exception e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert deploy in the DB", e);
					}
				    
				    
					//try returning updated json
		    		// convert java object to JSON format,
		    		// and returned as JSON formatted string
		    		String json = gson.toJson(updatedDeploy);
		    		
					//we delete the temporary files
					jsonFile.delete();
					
					//answer = new StringRepresentation((CharSequence) json, MediaType.TEXT_XML);
					answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
					answer.setCharacterSet(CharacterSet.UTF_8);
					//if it was a live deploy which is now in process, we return 202, not 200
					deployInProcess = GLUEPSManagerServerMain.ips.askDeployInProcess(new InProcessInfo(updatedDeploy.getId(),updatedDeploy.getLearningEnvironment().getAccessLocation().toString(),updatedDeploy.getCourse().getId()));
					if(deployInProcess) setStatus(Status.SUCCESS_ACCEPTED);

					return answer;		


					
				}else{
					//We do not known which deploy to modify! bad request...
					throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Did not specify a deployId");  
					
				}
			} catch (ResourceException e) {
				throw e;
			} finally {
	            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
			}	
    }



	private void reconfigureGlueletUsers(Deploy updatedDeploy) {
		if(updatedDeploy.getToolInstances()==null) return;
		logger.info("reconfiguring gluelet users...");
		for(ToolInstance instance: updatedDeploy.getToolInstances()){
			if(instance.getLocation()!=null && !instance.isRedirection(updatedDeploy)){//if the instance has been created and is not a redirection
				if(updatedDeploy.getDesign().findResourceById(instance.getResourceId()).getToolKind().equals(Resource.TOOL_KIND_EXTERNAL)){//if it is external - a gluelet
					if(updatedDeploy.getInstancedActivitiesForToolInstance(instance.getId())!=null){//if the gluelet is used in any activity
						try {
							FormattedEntry answerEntry = null;
							String toolTypeURL = updatedDeploy.getInstanceToolType(instance.getId());
							String toolType = toolTypeURL
									.substring(toolTypeURL.lastIndexOf("/") + 1);
							String[] participants = updatedDeploy
									.getParticipantUsernamesForToolInstance(instance.getId());
							String glueletId = instance.getLocation().toString().substring(instance.getLocation().toString().lastIndexOf("/")+1);
							answerEntry = this.modifyToolInstanceGM(glueletId, toolType, updatedDeploy.getStaffUsernames(),
									participants);
						} catch (Exception e) {
							System.out.println("Error re-configuring instance "+instance.getId()+". we continue with the other gluelets...");
							e.printStackTrace();
						}
						
					}
					
				}
				
			}
			
			
		}
		logger.info("gluelet users reconfigured!");
	}

	
	
	

	private FormattedEntry modifyToolInstanceGM(String glueletId, String toolType,
			String[] teachers, String[] participants) throws Exception {
		
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this
				.getApplication();

		
		System.out.println("trying to modify the instance of type "+toolType+" at URL "+app.getGmurlinternal() + "instance/" + glueletId);
		try {
			String response = doPostToUrl(app.getGmurlinternal() + "instance/" + glueletId, buildStringRepresentation(toolType, teachers, participants), "application/atom+xml");
			FormattedEntry answerEntry = new FormattedEntry(response);
			return answerEntry;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		
	}


	private String buildStringRepresentation(String toolType,
			String[] teachers, String[] participants) throws IOException {
				try {
					return (buildRepresentation(toolType, teachers, participants)).getText();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw e;
				}
	}


	private Representation buildRepresentation(String toolId, String[] teachersNames, String[] usersNames) {

		FormattedEntry entry = new FormattedEntry();
		entry.addExtendedTextChild("tool", toolId);

		//we put no configuration data
		entry.addExtendedTextChild("configuration", "");

		String[] newUsers = null;
		String teacher = null;
		if(teachersNames!=null && teachersNames.length>0){
			teacher = teachersNames[0];
			//If there are teachers, we just add all of them to the users list
			int numTeachers = teachersNames.length;
			
			if(usersNames!=null) newUsers = new String[usersNames.length+numTeachers];
			else newUsers = new String[numTeachers];
			
			if(usersNames!=null){
				for(int i = 0; i<usersNames.length; i++) newUsers[i] = usersNames[i];
				for(int i = 0; i<numTeachers; i++) newUsers[i+usersNames.length]=teachersNames[i];
			}else{
				for(int i = 0; i<numTeachers; i++) newUsers[i]=teachersNames[i];
			}
			
		}else{//No teachers, we just put the first user as callerUser - if there are no users, we put nothing (but it will probably fail!)
			if(usersNames!=null){
				teacher = usersNames[0];
				newUsers = usersNames;
			}else{
				teacher = null;
				newUsers = null;
			}
			
		}
		
		if(newUsers!=null) entry.addExtentedStructuredList("users", "user", newUsers);
		if(teacher!=null) entry.addExtendedTextChild("callerUser", teacher);			
		
		return entry.getRepresentation();
	}


	/**
	 * PUT DeployResource
	 * 
	 * Modifies Design in Glueps.
	 * 
	 *
	 * 
	 * @return	URI to new Deploy resource, or null.
	 */
	@Put("xml")
    public Representation putDeploy(Representation entity)  {
    	
		try {
			
   	    	GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
   	    	if (app.getLdShakeMode()==false){
   		    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
   				String login = this.getRequest().getChallengeResponse().getIdentifier();
   				if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
   			}else{
   				if (this.getRequest().getChallengeResponse()!=null){
   			    	//Default authorization: if the caller is not the owner, we do not allow actions on the resource
   					String login = this.getRequest().getChallengeResponse().getIdentifier();
   					if(!deploy.getAuthor().equals(login)) throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The caller is not the author of the deploy");
   				}
   				else{
   					//Check the sectoken
   					Reference ref = getReference();
   					Form query = ref.getQueryAsForm();
   					String sectoken = query.getFirstValue("sectoken");
   					if (sectoken!=null){
   						JpaManager dbmanager = JpaManager.getInstance();
   						SectokenEntity ste = dbmanager.findSectokenById(deployId);
   						if (ste==null || !ste.getSectoken().equals(sectoken)){
   							throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken is not valid for this deploy");
   						}
   					}
   					else{
   						throw new ResourceException(Status.CLIENT_ERROR_FORBIDDEN, "The sectoken parameter is missing");
   					}
   				}
   			}
   	    	
   	    	if (getReference().getIdentifier().contains("/undo")){
   	    		return undoDeploy();
   	    	}
   	    	else if (getReference().getIdentifier().contains("/redo")){
   	    		return redoDeploy();
   	    	}
			
			logger.info("** PUT DEPLOY received");

			//WE check that the deploy is not in process, and that the request does not try to override the (faulty) in process mechanism
			String override = this.getQuery()!=null ? this.getQuery().getValues("override") : null;
			boolean deployInProcess = GLUEPSManagerServerMain.ips.askDeployInProcess(new InProcessInfo(deploy.getId(),deploy.getLearningEnvironment().getAccessLocation().toString(),deploy.getCourse().getId()));
			if(deployInProcess && override==null) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "The deployment is in process. Please try modifying it again later");
			//If it is a dynamic deploy We also check that there is not currently a put deploy request into the same VLE and course from a different deploy in GLUE!-PS
			if (getReference().getIdentifier().contains("/live")){
				boolean courseInProcess = GLUEPSManagerServerMain.ips.askCourseInProcess(deploy.getLearningEnvironment().getAccessLocation().toString(), deploy.getCourse().getId());
				if(courseInProcess && override==null) throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "There is deployment in process in the same VLE and course. Please try deploying it again later");
			}
			
			//We should introduce the received xml into the database
			//GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
			String UPLOAD_DIRECTORY = app.getAppPath()+"/uploaded/";
			String uploadDir = UPLOAD_DIRECTORY+"/temp/";
			
			String deployData = null;
			
			File f;
			if(this.deployId!=null){
				//We get the xml that we have received, and write it to a temp file
				try{
					entity.setCharacterSet(CharacterSet.UTF_8);
					InputStream entrada= entity.getStream();
					//Write the xml to the uploads folder, with a random number to avoid concurrency problems
				   f=new File(uploadDir, this.deployId+".xml"+"."+(new Date()).getTime());
				   OutputStream salida=new FileOutputStream(f);
				   byte[] buf =new byte[1024];
				   int len;
				   while((len=entrada.read(buf))>0){
				      salida.write(buf,0,len);
				   }
				   salida.close();
				   entrada.close();
				   System.out.println("Upload successful");
				}catch(IOException e){
				    System.out.println("There was an error : "+e.toString());
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Unknown error uploading the file", e);  
				}

				//We check whether we are also supposed to execute the deploy (in which case, updatedDeploy will contain the updated deploy with the final URL
				Deploy updatedDeploy = createDeployObject(f);
				
				if(!updatedDeploy.isValid()) throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "The received deploy data is wrongly formatted or incoherent"); 
				
				if( (getReference().getIdentifier().contains("/static") && supportStaticDeploy(updatedDeploy)) || (getReference().getIdentifier().contains("/live") && supportLiveDeploy(updatedDeploy)) ){
					//Check if we can do the deploy
					checkDeployable(updatedDeploy);
					boolean success = completeToolCreation(updatedDeploy);
					//We store the deploy in DB to make sure that the last changes won't be lost
					//We convert the deploy into one where ids are NOT urls - to store in database
					JpaManager dbmanager = JpaManager.getInstance();
					try {
						Boolean created = instancesCreated(deURLifyDeploy(updatedDeploy));
						
						if (created == true){
							dbmanager.insertDeploy(deURLifyDeploy(updatedDeploy));
							DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
							newDeployVersion.setUndoalert(true);
							dbmanager.updateDeployVersion(newDeployVersion);
						}
						
					} catch (Exception e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert deploy in the DB", e);
					}
					if (!success){
						throw new ResourceException(Status.CLIENT_ERROR_FAILED_DEPENDENCY, "There are still some external tool instances without creating");
					}
				}
				
				if(getReference().getIdentifier().contains("/static")){
			    	//here, we check the users for all gluelets, and update their lists of users
					reconfigureGlueletUsers(updatedDeploy);				    	
					updatedDeploy = doStaticDeploy(updatedDeploy);//This function should work whether it is a deploy or redeploy - WARNING!, restoring this will probably delete work done by students if the activities had already started
			    }
			    else if (getReference().getIdentifier().endsWith("/live/newdeploy")){
			    	//here, we check the users for all gluelets, and update their lists of users
					reconfigureGlueletUsers(updatedDeploy);
			    	updatedDeploy = doLiveDeploy(updatedDeploy, false);
			    }
			    else if (getReference().getIdentifier().endsWith("/live/redeploy")){
			    	//here, we check the users for all gluelets, and update their lists of users
					reconfigureGlueletUsers(updatedDeploy);
					if (updatedDeploy.getLiveDeployURL()==null){
						throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE, "You can not redeploy a deploy that has not been deployed yet");
					}
			    	updatedDeploy = doLiveDeploy(updatedDeploy, true);
			    }
				else if (getReference().getIdentifier().contains("/live")){
			    	//here, we check the users for all gluelets, and update their lists of users
					reconfigureGlueletUsers(updatedDeploy);
					if(updatedDeploy.getLiveDeployURL()!=null) updatedDeploy = doLiveDeploy(updatedDeploy, true);//if the deploy had already been done, it is a redeploy!
					else updatedDeploy = doLiveDeploy(updatedDeploy, false);//it is the first live deploy
			    }
  
			    //We construct a Deploy object with non-url ids to store it in DB
			    Deploy deUrlifiedDeploy = null;
			    
			    
				//if we updated the deploy info (with the deploy URL), we convert the deploy into a xml file so that we can put it in the DB
			    if(updatedDeploy!=null){
			    	
			    	try {
						deUrlifiedDeploy = deURLifyDeploy(updatedDeploy);
					} catch (Exception e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to process the updated deploy", e);
					}
			    	
			    	//set the data to be returned (updatedDeploy)
			    	deployData = generateXML(updatedDeploy, glueps.core.model.Deploy.class);
			    }else{//if not, the File f still contains the received deploy, which we want to store in database
			    	//set the data to be returned (updatedDeploy)
			    	try {
						deployData = FileUtils.readFileToString(f, "UTF-8");
					
						deUrlifiedDeploy = deURLifyDeploy((Deploy) generateObject(deployData, Deploy.class));
			    	} catch (IOException e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to process the updated deploy", e);
					} catch (Exception e) {
						throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to process the updated deploy", e);
					}
			    	
				    
			    }
			    
			    JpaManager dbmanager = JpaManager.getInstance();
			    try {
					Boolean created = instancesCreated(deURLifyDeploy(updatedDeploy));
					
					dbmanager.insertDeploy(deURLifyDeploy(updatedDeploy));
					
					if (created == true){
						DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
						newDeployVersion.setUndoalert(true);
						dbmanager.updateDeployVersion(newDeployVersion);
					}
					
				} catch (Exception e) {
					throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Error while trying to insert the deploy into the database", e);
				}
			    
				//we delete the temporary file
				f.delete();
				
				//try returning introduced xml
				Representation answer = new StringRepresentation((CharSequence) deployData, MediaType.TEXT_XML);
				answer.setCharacterSet(CharacterSet.UTF_8);
				//if it was a live deploy which is now in process, we return 202, not 200
				deployInProcess = GLUEPSManagerServerMain.ips.askDeployInProcess(new InProcessInfo(deUrlifiedDeploy.getId(),deUrlifiedDeploy.getLearningEnvironment().getAccessLocation().toString(),deUrlifiedDeploy.getCourse().getId()));
				if(deployInProcess) setStatus(Status.SUCCESS_ACCEPTED);
				
				return answer;		

				
			}else{
				//We do not known which deploy to modify! bad request...
				throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Did not specify a deployId");  
				
			}
		} catch (ResourceException e) {
			throw e;
		} finally {
            discardRepresentation(entity);        // fix to mimick GLUElet Manager corrections for connection stability
		}
		
   		
   		
   		
   		
    }
    
	
	

	
	private void reconfigureGlueletUsers(File f) {
		Deploy newDeploy = null;
		
		//We create the deploy object with the provided deploy XML
		try{
	        JAXBContext jc = JAXBContext.newInstance( glueps.core.model.Deploy.class );
	        Unmarshaller u = jc.createUnmarshaller();
	        newDeploy = (Deploy)u.unmarshal(f);
	        System.out.println( "\nThe unmarshalled objects are:\n" + newDeploy.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid deploy data");
		}
		
		reconfigureGlueletUsers(newDeploy);
		
	}

	private Deploy doLiveDeploy(File f) {
		Deploy newDeploy = null;
		
		//We create the deploy object with the provided deploy XML
		try{
	        JAXBContext jc = JAXBContext.newInstance( glueps.core.model.Deploy.class );
	        Unmarshaller u = jc.createUnmarshaller();
	        newDeploy = (Deploy)u.unmarshal(f);
	        System.out.println( "\nThe unmarshalled objects are:\n" + newDeploy.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid deploy data");
		}
		
		/*newDeploy.setInProcess(true);
		GLUEPSManagerServerMain.ips.startsProcess(new InProcessInfo(newDeploy.getId(), newDeploy.getLearningEnvironment().getAccessLocation().toString(), newDeploy.getCourse().getId()));*/
		doLiveDeploy(newDeploy, false);
		
		//We return the deploy with the inProcess flag
		return newDeploy;

	}
	
	private Deploy createDeployObject(File f) {
		Deploy newDeploy = null;
		
		//We create the deploy object with the provided deploy XML
		try{
	        JAXBContext jc = JAXBContext.newInstance( glueps.core.model.Deploy.class );
	        Unmarshaller u = jc.createUnmarshaller();
	        newDeploy = (Deploy)u.unmarshal(f);
	        System.out.println( "\nThe unmarshalled objects are:\n" + newDeploy.toString());
		} catch (JAXBException e) {
			e.printStackTrace();
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid deploy data");
		}
		
		return newDeploy;
		
	}



    
    public void setDeploy(String deployId, String feedReference)  {
    	
   		// get just the deployId without extension, if it had any
    	this.deployId = trimId(deployId);

        this.feedReference=feedReference;
        
		// Maybe this part is too inefficient?? but we need it to get the design id from the deploy (other means could be thought of)
   		JpaManager dbmanager = JpaManager.getInstance();
   		this.deploy = dbmanager.findDeployObjectById(this.deployId);
        
   		//Since we are creating the resource internally, we set authentication to false
   		this.doAuthentication = false;
   		
   		// does it exist?
		setExisting(this.deploy != null);	// setting 'false' implies that REST methods won't start; server will respond with 404

    }
    
    
    public FormattedEntry getDeployEntry() {
    	FormattedEntry entry = null;
    	java.util.Date fecha = new Date();
        if(this.isExisting()) {
            entry = new FormattedEntry();

            /// build URI
            String uri = null;
            Reference ref = getReference();
            if(ref!=null)
            	uri = ref.getIdentifier();
            else
            	uri =feedReference+"/" + this.deployId;

            uri = doGluepsUriSubstitution(uri);
                      
            /// Atom standard elements
            entry.setId(uri);
            entry.setTitle(this.deploy.getName());
            entry.setUpdated(fecha);
            entry.setAlternateLink("Description of Deploy"+ this.deployId, uri);
           	entry.addAuthor(FormatStatic.GSIC_NAME, null, null);
            entry.setRelatedLink("Provider", uri);
           	//Glueps specific elements
           	entry.addExtendedTextChild("name",this.deploy.getName());
           	entry.addExtendedTextChild("complete",""+this.deploy.isComplete());
           	if(this.deploy.isComplete()){

           		//we get the learningEnvironment of the deploy, and check whether the deployment is static, dynamic or both, by looking at its adaptor
           		VLEAdaptorFactory fact = new VLEAdaptorFactory((GLUEPSManagerApplication) this.getApplication());
           		IVLEAdaptor adaptor = fact.getVLEAdaptor(this.deploy.getLearningEnvironment());
           		
           		if(this.deploy.getStaticDeployURL()!=null && adaptor instanceof IStaticVLEDeployer) entry.addExtendedTextChild("staticDeploy",uri+"/static");
           		if(this.deploy.getLiveDeployURL()!=null && adaptor instanceof IDynamicVLEDeployer) entry.addExtendedTextChild("liveDeploy",uri+"/live");
           	}
           	
           	//set the design tag with the URL of the design resource
           	String designUri = (new Reference(feedReference)).getParentRef().getIdentifier()+"designs/"+trimId(this.deploy.getDesign().getId());
           	designUri = doGluepsUriSubstitution(designUri);
           	
           	entry.addExtendedTextChild("design", designUri);
           	
           	
        } else {
        	setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }
        
        return entry;
    }  
    
    
	/*
	 * Check if we can do the deploy
	 * 
	 */
	private void checkDeployable(Deploy newDeploy) {

		Design design = newDeploy.getDesign();

		// Check if there are undefined tools
		List<Resource> resources = design.getResources();
		if (resources!=null){
			for (Iterator<Resource> it = resources.iterator(); it.hasNext();) {
				Resource resource = it.next();
				if (resource.isInstantiable()) {
					String toolValue = null;
					if(newDeploy.getLearningEnvironment().getInternalTools()!=null) toolValue = newDeploy.getLearningEnvironment().getInternalTools().get(resource.getToolType());
					if (toolValue == null || toolValue.length() == 0) {
						toolValue = newDeploy.getLearningEnvironment().getExternalTools().get(resource.getToolType());
					}
					if (toolValue == null || toolValue.length() == 0) {
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid deploy data to be deployed. There are undefined tools");
					}
				}
			}
		}
		
		// Check if there are tool instances of Web Content that haven't been instantiated yet
		ArrayList<ToolInstance> toolInstances = newDeploy.getToolInstances();
		if (toolInstances != null) {
			for (Iterator<ToolInstance> it = toolInstances.iterator(); it.hasNext();) {
				ToolInstance instance = it.next();
				Resource r = design.findResourceById(instance.getResourceId());
				if (newDeploy.getInstanceToolKind(instance.getId()).equals("external")) {
					String toolValue = newDeploy.getLearningEnvironment().getExternalTools().get(r.getToolType());
					if (toolValue.toLowerCase().equals("web content") && instance.getLocation()==null) {
						throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid deploy data. Some instances of web content haven't been configured yet");
					}
				}
			}
		}
	}
	
	private boolean instancesCreated(Deploy newDeploy){
		ArrayList<ToolInstance> instances = newDeploy.getToolInstances();
		if(instances!=null){
			for(Iterator<ToolInstance> it = instances.iterator();it.hasNext();){
				ToolInstance instanceNew = it.next();
				if(instanceNew.getLocation()!=null){
					ToolInstance instanceOld = deploy.getToolInstanceById(instanceNew.getId());
					//Check if the tool instance has been created
					if((deploy.getInstanceToolKind(instanceOld.getId())!=null && deploy.getInstanceToolKind(instanceOld.getId()).equals("external")) && instanceOld.getInternalReference()==null && (instanceOld.getLocation()==null || instanceOld.getLocation().toString().length()==0)){
						return true;
					}
				}				
			}
		}
		return false;
		
	}
    
	/*
	 * If there are tool instances that haven't been created yet, it tries to create them
	 * 
	 */
	private boolean completeToolCreation(Deploy newDeploy){
		boolean completed = true;
		Design design = newDeploy.getDesign();
		ArrayList<ToolInstance> toolInstances = newDeploy.getToolInstances();
		if(toolInstances!=null){
			//Second, we check that the external instances have been created and defined
			for(Iterator<ToolInstance> it = toolInstances.iterator(); it.hasNext(); ){
				ToolInstance instance = it.next();
				if((newDeploy.getInstanceToolKind(instance.getId())!=null && newDeploy.getInstanceToolKind(instance.getId()).equals("external")) && instance.getInternalReference()==null && (instance.getLocation()==null || instance.getLocation().toString().length()==0)){//the toolInstance is undefined... is it used?
					if(newDeploy.getInstancedActivitiesForToolInstance(instance.getId())!=null){
						//if some instancedActivity uses the toolinstance, it should be defined... the deploy is not complete!
						Resource r = design.findResourceById(instance.getResourceId());
						try {
							// Get configuration form for the tool from Glue
							GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
							String gmUrl = app.getGmurlinternal();
							String response = doGetFromURL(gmUrl + "tools/" + trimId(r.getToolType()) + "/configuration");
							if (response != null) {
								Representation answer = new StringRepresentation(response);
								// Create the tool instance using the
								// configuration form obtained
								ToolInstanceResource tir = new ToolInstanceResource();
								tir.setToolInstance(newDeploy.getId(), instance.getId());
								answer = tir.createToolInstance(answer);
								// Get the location for the created instance
								FormattedEntry answerEntry = new FormattedEntry(answer.getStream());
								URL location = new URL(answerEntry.getId());
								instance.setLocation(location);
							}
						} catch (Exception e) {
							completed = false;
						}
					}
				}				
			}
		}
		//If none of the above happened, everything is defined, the design is complete
		//If it is complete but no zip has been generated, it will be generated/deployed when the deployed zip or link is GET
		return completed;
	}
 
    
	/**
	 * Does the live batch deployment in a background thread.
	 *
	 * @param 	Deploy the deployment data.
	 */
	private class BackgroundLiveDeployer implements Callable<Deploy> {
		
		private Deploy deployable;
		private IDynamicVLEDeployer adaptor;
		private boolean isRedeploy;
		
		public BackgroundLiveDeployer(Deploy dep, IDynamicVLEDeployer adap, boolean redeploy) {
			this.deployable = dep;
			this.adaptor = adap;
			this.isRedeploy = redeploy;
		}
		
		public Deploy call() {
			
			Deploy newDeploy = deployable;
			try {
				if(this.isRedeploy) newDeploy = adaptor.redeploy(newDeploy.getLearningEnvironment().getAccessLocation().toString(), newDeploy);//it is an already deployed deploy
				else newDeploy = adaptor.deploy(newDeploy.getLearningEnvironment().getAccessLocation().toString(), newDeploy);//it is a new deploy process
				newDeploy.setInProcess(false);
				GLUEPSManagerServerMain.ips.finishesProcess(new InProcessInfo(newDeploy.getId(), newDeploy.getLearningEnvironment().getAccessLocation().toString(), newDeploy.getCourse().getId()));
			} 
			catch (Exception e) {
				e.printStackTrace();
				//If something went wrong, we store the deploy in the DB, with inprocess=false y location=null
				newDeploy.setInProcess(false);
				GLUEPSManagerServerMain.ips.finishesProcess(new InProcessInfo(newDeploy.getId(), newDeploy.getLearningEnvironment().getAccessLocation().toString(), newDeploy.getCourse().getId()));
				newDeploy.setLiveDeployURL(null);
			} finally {
				//We store the final state in DB
				Deploy deUrlifiedDeploy=null;
		    	try {
					deUrlifiedDeploy = deURLifyDeploy(newDeploy);
					JpaManager dbmanager = JpaManager.getInstance();
					
					Boolean created = instancesCreated(deURLifyDeploy(deUrlifiedDeploy));
					
					dbmanager.insertDeploy(deURLifyDeploy(deUrlifiedDeploy));
					
					if (created == true){
						DeployVersionEntity newDeployVersion = dbmanager.findLastValidDeployVersion(deployId);
						newDeployVersion.setUndoalert(true);
						dbmanager.updateDeployVersion(newDeployVersion);
					}
					
				} catch (Exception e) {
					System.err.println("Argh, we could not insert the processed deploy in DB! the deploy will become unmodifiable");
					e.printStackTrace();
				}
			}
			return newDeploy;
		}
	}
    
}
