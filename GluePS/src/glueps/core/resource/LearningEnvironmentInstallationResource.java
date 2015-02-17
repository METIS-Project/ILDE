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

import java.util.Date;
import java.util.logging.Logger;

import glue.common.format.FormatStatic;
import glue.common.format.FormattedEntry;
import glueps.core.model.LearningEnvironmentInstallation;
import glueps.core.persistence.JpaManager;

import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import com.google.gson.Gson;


/**
 * Resource Learning Environment Installation
 * 
 * Information about a Learning Environment Installation
 * 
 * @author	 	Javier E. Hoyos
 * @version 	2013260200
 * @package 	glueps.core.resources
 */

public class LearningEnvironmentInstallationResource extends GLUEPSResource {
	
/// attributes ///
	
	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
 
	
	/** Local id. Integer used as identifier in table of learning_environment_installations */
    protected String leInstId;
    
    protected LearningEnvironmentInstallation leInst;
    
    protected String feedReference;
    
    
    @Override
    protected void doInit() throws ResourceException {
    	
   		// get the "LEId" attribute value taken from the URI template /learningEnvironmentInstallation/{LEInstId}
    	this.leInstId = trimId((String) getRequest().getAttributes().get("LEInstId"));	

   		logger.info("Initializing resource "+this.leInstId);

   		JpaManager dbmanager = JpaManager.getInstance();
   		leInst = dbmanager.findLEInstObjectById(leInstId);
   		
   		// does it exist?
		setExisting(this.leInst != null);	// setting 'false' implies that REST methods won't start; server will respond with 404
    }
    

 
    @Get("xml|html")
	public Representation getLearningEnvironmentInstallation()  {
    		
   		logger.info("** GET learning environment installation received");
   		Representation answer = null;
		
   		LearningEnvironmentInstallation urlifiedLEInst = URLifyLearningEnvironmentInstallation(leInst, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
   		
		//We generate the xml on-the-fly and respond with it
		String xmlfile = generateXML(urlifiedLEInst, glueps.core.model.LearningEnvironmentInstallation.class);

		if (xmlfile != null){	   		
				answer = new StringRepresentation((CharSequence) xmlfile, MediaType.TEXT_XML);
				answer.setCharacterSet(CharacterSet.UTF_8);
   		}
    	
   		logger.info("** GET Learning Environment Installation answer: \n" + (answer != null ? xmlfile : "NULL"));
   		return answer;
    	
    }
    
    @Get("json")
	public Representation getJsonLearningEnvironmentInstallation()  {
    		
   		logger.info("** GET JSON learning environment installation received");
   		Representation answer = null;
		
   		LearningEnvironmentInstallation urlifiedLEInst = URLifyLearningEnvironmentInstallation(leInst, doGluepsUriSubstitution(getReference().getParentRef().getParentRef().getIdentifier()));
   		
		// convert java object to JSON format,
		// and returned as JSON formatted string
		Gson gson = new Gson();
		String json = gson.toJson(urlifiedLEInst);
   		 		
   		if (json != null){
   			answer = new StringRepresentation((CharSequence) json, MediaType.APPLICATION_JSON);
			answer.setCharacterSet(CharacterSet.UTF_8);
   		}
    	
   		logger.info("** GET JSON learning environment installation answer: \n" + (answer != null ? json : "NULL"));
   		return answer;
    	
    }
    
	public void setLearningEnvironmentInstallation(String leInstId, String feedReference)  {
		
		this.leInstId = leInstId;
        
        this.feedReference=feedReference;
		
   		JpaManager dbmanager = JpaManager.getInstance();
   		leInst = dbmanager.findLEInstObjectById(leInstId);
   		
   		// does it exist?
		setExisting(this.leInst != null);	// setting 'false' implies that REST methods won't start; server will respond with 404

    }    
    
    public FormattedEntry getLearningEnvironmentInstallationEntry() {
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
            	uri =feedReference+"/" + this.leInstId;

            uri = doGluepsUriSubstitution(uri);
            
            /// Atom standard elements
            entry.setId(uri);
            entry.setTitle(this.leInst.getName());
            entry.setUpdated(fecha);
            entry.setAlternateLink("Description of learning environment installation "+ this.leInstId, uri);
           	entry.addAuthor(FormatStatic.GSIC_NAME, null, null);
            entry.setRelatedLink("Provider", uri);
           	
            //Glueps specific elements
           	entry.addExtendedTextChild("id",String.valueOf(this.leInstId));
           	entry.addExtendedTextChild("name",this.leInst.getName());
           	entry.addExtendedTextChild("type", this.leInst.getType());
           	entry.addExtendedTextChild("accessLocation", this.leInst.getAccessLocation().toString());          	
           	
        } else {
        	setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }
        
        return entry;
    } 
    
}
