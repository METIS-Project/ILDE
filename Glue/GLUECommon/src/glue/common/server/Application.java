/*******************************************************************************
 * Copyright (C) 2015 Intelligent & Cooperative Systems Research Group/Education,
 * Media, Computing & Culture (GSIC-EMIC). University of Valladolid(UVA). 
 * Valladolid, Spain. https://www.gsic.uva.es/
 * 
 * This file is part of GlueletCommon.
 * 
 * GlueletCommon is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 * 
 * GlueletCommon is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package glue.common.server;  

import org.restlet.Restlet;  
import org.restlet.routing.Router;  

/**
 * RESTlet application class.
 * 
 * URIs to every resource are defined and bound.
 *
 * @author  David A. Velasco
 * @version 2011012500
 * @package glue.common.server
 */

public abstract class Application extends org.restlet.Application {  

	/** 
	 * Creates a root Restlet that will receive all incoming calls. 
	 */  
	@Override  
	public synchronized Restlet createInboundRoot() {  
		// Create a router Restlet that defines routes.  
		Router router = new Router(getContext());
		this.attachResources(router);
		return router;  
	}
	
	
	/**
	 * Method to attach every valid URL scheme to a resource class
	 * 
	 * @param router	Router responsible for driving HTTP request to the proper resource class
	 */
	protected abstract void attachResources(Router router);
}  
