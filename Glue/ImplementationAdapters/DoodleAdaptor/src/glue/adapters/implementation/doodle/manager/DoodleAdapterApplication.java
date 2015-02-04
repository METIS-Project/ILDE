/**
 This file is part of DoodleAdapter.

 DoodleAdapter is a property of the Intelligent & Cooperative Systems
 Research Group (GSIC) from the University of Valladolid (UVA).

 Copyright 2012 GSIC (UVA).

 DoodleAdapter is licensed under the GNU General Public License (GPL)
 EXCLUSIVELY FOR NON-COMMERCIAL USES. Please, note this is an additional
 restriction to the terms of GPL that must be kept in any redistribution of
 the original code or any derivative work by third parties.

 If you intend to use DoodleAdapter for any commercial purpose you can
 contact to GSIC to obtain a commercial license at <glue@gsic.tel.uva.es>.

 If you have licensed this product under a commercial license from GSIC,
 please see the file LICENSE.txt included.

 The next copying permission statement (between square brackets []) is
 applicable only when GPL is suitable, this is, when DoodleAdapter is
 used and/or distributed FOR NON COMMERCIAL USES.

 [ You can redistribute DoodleAdapter and/or modify it under the
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
package glue.adapters.implementation.doodle.manager;  

import glue.adapters.implementation.doodle.resources.InstanceFactoryResource;
import glue.adapters.implementation.doodle.resources.InstanceResource;
import glue.common.resources.ConfigurationResource;
import glue.common.server.Application;

import org.restlet.routing.Router;  

/**
 * RESTlet applicaction class.
 * 
 * URIs to every resource are defined and bound.
 *
 * @author  	Carlos Alario
 * @contributor	Javier Enrique Hoyos Torio
 * @contributor	David A. Velasco Villanueva
 * @version 	2012092501
 * @package glue.adapters.implementation.doodle.manager
 */
public class DoodleAdapterApplication extends Application {  
	
	/**
	 * Method to attach every valid URL scheme to a resource class
	 * 
	 * @param router	Router responsible for driving HTTP request to the proper resource class
	 */
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
	}
	
}  
