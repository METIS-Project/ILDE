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
