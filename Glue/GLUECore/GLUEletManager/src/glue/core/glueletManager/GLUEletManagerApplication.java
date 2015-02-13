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

package glue.core.glueletManager;  

import glue.core.resources.ConfigurationResource;
import glue.core.resources.InstanceFactoryResource;
import glue.core.resources.InstanceResource;
import glue.core.resources.ToolImplementationResource;
import glue.core.resources.ToolImplementationsListResource;

import org.restlet.Application;  
import org.restlet.Restlet;  
import org.restlet.routing.Router;  

/**
 * RESTlet application class.
 * 
 * URIs to every resource are defined and bound.
 *
 * @author  David A. Velasco <davivel@gsic.uva.es>
 * @version 2012092501
 * @package glue.core.glueletManager
 */

public class GLUEletManagerApplication extends Application {  

	/** 
	 * Creates a root Restlet that will receive all incoming calls. 
	 */  
	@Override  
	public synchronized Restlet createInboundRoot() {  
		// Create a router Restlet that defines routes.  
		Router router = new Router(getContext());
		
		// Defines a route for the resource list of tools  
		router.attach("/tools", ToolImplementationsListResource.class);					// TODO MAYBE change to non-hardcoded path  
		// Defines a route for the resource tool  
		router.attach("/tools/{toolId}", ToolImplementationResource.class);				// TODO MAYBE change to non-hardcoded path  
		// Defines a route for the resource tool configuration 
		router.attach("/tools/{toolId}/configuration", ConfigurationResource.class);	// TODO MAYBE change to non-hardcoded path
		// Defines a route for the resource Gluelet instance factory
		router.attach("/instance", InstanceFactoryResource.class);						// TODO MAYBE change to non-hardcoded path
		// Defines a route for the resource Gluelet instance
		router.attach("/instance/{instanceId}", InstanceResource.class);				// TODO MAYBE change to non-hardcoded path


		return router;  
	}

}  
