/**
 This file is part of GlueletManager.

 GlueletManager is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2011 GSIC (UVA).

 GlueletManager is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use GlueletManager for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when GlueletManager is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute GlueletManager and/or modify it under the
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
