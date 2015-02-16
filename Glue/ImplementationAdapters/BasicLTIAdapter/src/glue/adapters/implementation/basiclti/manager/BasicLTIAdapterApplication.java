/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of Basic LTI Adapter.
 * 
 * Basic LTI Adapter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * Basic LTI Adapter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.adapters.implementation.basiclti.manager;

import org.restlet.routing.Router;

import glue.adapters.implementation.basiclti.resources.InstanceFactoryResource;
import glue.adapters.implementation.basiclti.resources.InstanceResource;
import glue.adapters.implementation.basiclti.resources.FormResource;
import glue.common.resources.ConfigurationResource;
import glue.common.server.Application;

/**
 * RESTlet applicaction class.
 * 
 * @author  	Javier Enrique Hoyos Torio
 * @version 	2012092501
 * @package 	glue.adapters.implementation.basiclti.manager
 */
public class BasicLTIAdapterApplication extends Application{

	@Override
	protected void attachResources(Router router) {
		// Defines a route for the resource instance factory  
		router.attach("/instance", InstanceFactoryResource.class);
		// Defines a route for the resource instance
		router.attach("/instance/{instanceId}", InstanceResource.class);
		// Defines a route for the resource configuration (default one)  
		router.attach("/configuration", ConfigurationResource.class);
		// Defines a route for the resource configuration (required one) 
		router.attach("/configuration/{configId}", ConfigurationResource.class); 
		
		//attach resource to get the post form for the instance
		router.attach("/form", FormResource.class);
	}

}
