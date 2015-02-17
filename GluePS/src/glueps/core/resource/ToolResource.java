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
import glueps.adaptors.vle.moodle.MoodleAdaptor;
import glueps.core.gluepsManager.GLUEPSManagerApplication;

import java.io.File;
import java.util.logging.Logger;

import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

public class ToolResource extends GLUEPSResource {

	/** 
	 * Logger 
	 * 
	 * TODO - define our own "glue.core.glueletManager" Logger, for instance
	 **/
	private static Logger logger = Logger.getLogger("org.restlet");
    
    protected String localConfiguration;


	@Get
	public Representation getConfiguration(){

		
   		logger.info("** GET CONFIGURATION received");
   		Representation answer = null;
   				
		/// Searchs for parameters in the URL
		Reference ref = getReference();
		Form query = ref.getQueryAsForm();

		String toolId = query.getFirstValue("id");
		String toolKind = query.getFirstValue("toolKind");  // additional URL decoding is not needed!!; getFirstValue(...) applies it
		String vleType = query.getFirstValue("vleType");
		
		/// Check mandatory parameter values
		String missingParameters = "";
		if (toolId == null || toolId.length() == 0)
			missingParameters += "id, ";
		if (toolKind == null || toolKind.length() == 0) 
			missingParameters += "toolKind, ";
		if (toolKind!=null && toolKind.toLowerCase().equals("internal") && (vleType == null || vleType.length() == 0)){ 
			missingParameters += "vleType, ";
		}
		if (missingParameters.length() > 0) {
			missingParameters = missingParameters.substring(0, missingParameters.length() - 2);			
			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing parameters: " + missingParameters);
		}
   		
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();

   		if(toolKind.toLowerCase().equals("external")){//It is an external tool
   			
   	   		logger.info(toolId + " is an external tool. We ask GLUE");
   			this.localConfiguration = getToolInstanceConfiguration(toolId);
   			
   		}else if(toolKind.toLowerCase().equals("internal")){//It is an internal tool
   			
   	   		logger.info(toolId + " is an internal tool");
   	   		if (vleType.toLowerCase().equals("moodle")){
	   			//TODO select the tool type. For now, only Moodle
	   			MoodleAdaptor adaptor = new MoodleAdaptor();
	   			adaptor.setTemplateDir(app.getAppPath()+File.separator+"templates");
	   			this.localConfiguration = adaptor.getToolConfiguration(toolId);
   	   		}else{
   	   			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, vleType + " is not a vleType valid value");
   	   		}
   			
   		}else{
   			throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, toolKind + " is not a toolKind valid value");
   		}

   		if(localConfiguration!=null) answer = new StringRepresentation(localConfiguration);

   		return answer;
		
	}

	private String getToolInstanceConfiguration(String toolId) {
		
     	//Get the GlueletManager URL from app properties
		GLUEPSManagerApplication app = (GLUEPSManagerApplication) this.getApplication();
		String gmUrl = app.getGmurlinternal();
		
		String response;
		try {
			response = doGetFromURL(gmUrl+"tools/"+toolId+"/configuration");
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}


	
}
