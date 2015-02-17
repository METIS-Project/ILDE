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
package glueps.core.gluepsManager;  

import java.util.Properties;

import org.restlet.Application;  
import org.restlet.Restlet;  
import org.restlet.data.CharacterSet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;  

import glueps.core.resource.ActionResource;
import glueps.core.resource.ConfigurationResource;
import glueps.core.resource.CourseResource;
import glueps.core.resource.DeployListResource;
import glueps.core.resource.DeployResource;
import glueps.core.resource.DesignListResource;
import glueps.core.resource.DesignResource;
import glueps.core.resource.LearningEnvironmentListResource;
import glueps.core.resource.LearningEnvironmentResource;
import glueps.core.resource.PoiListResource;
import glueps.core.resource.ToolInstanceListResource;
import glueps.core.resource.ToolInstanceResource;




/**
 * RESTlet application class.
 * 
 * URIs to the unguarded resources are defined and bound. Currently, only the GETs to toolInstances
 *
 * @author  Juan Carlos A. <jcalvarez@gsic.uva.es>
 * @version 
 * @package glueps.core.gluepsManager
 */

public class GLUEPSManagerUnguardedApplication extends GLUEPSManagerApplication {  

	public GLUEPSManagerUnguardedApplication(Properties configuration) {
		super(configuration);
	}
	
	/** 
	 * Creates a root Restlet that will receive all incoming calls. 
	 */  
	@Override  
	public synchronized Restlet createInboundRoot() {  
		// Create a router Restlet that defines routes.  
		Router router = new Router(getContext());
		
		// We set the encoding of files to UTF-8, especially for static files to prevent problems in windows??
		this.getMetadataService().setDefaultCharacterSet(CharacterSet.UTF_8);
		
		// Defines a route for the resource ToolInstance
		router.attach("",ToolInstanceResource.class);	
		// Defines a route for the resource ToolInstance
		router.attach("/hexconv",ToolInstanceResource.class);	
		// Defines a route for the resource ToolInstance
		router.attach("/64conv",ToolInstanceResource.class);	
		// Defines a route for the resource list of POIs (currently for arbrowsers
		router.attach("/{appType}/pois/{operation}/",PoiListResource.class);
		router.attach("/{appType}/pois/{operation}",PoiListResource.class);
		router.attach("/{appType}/pois/{operation}/{deployId}/{username}",PoiListResource.class);
		router.attach("/{appType}/filter/deploy/{deployId}/pois/{operation}/",PoiListResource.class);
		//TODO Usar /junaio/pois/login, y también reflejar el appType en los logs

		//First hack into an Atomic Action Server
		router.attach("/deploys/{deployId}/actions/participant/{participantId}",ActionResource.class);
		router.attach("/deploys/{deployId}/actions/group/{groupId}",ActionResource.class);
		router.attach("/deploys/{deployId}/actions/activity/{activityId}",ActionResource.class);
		router.attach("/deploys/{deployId}/actions/resource/{toolId}",ActionResource.class);
		router.attach("/deploys/{deployId}/actions/deployVLE",ActionResource.class);
		router.attach("/deploys/{deployId}/actions/OK",ActionResource.class);
		router.attach("/deploys/{deployId}/actions/NOK",ActionResource.class);
		router.attach("/deploys/{deployId}/actions/QuestionMark",ActionResource.class);
		
		
		return router;  
	}



}  
